package com.betterjr.common.mq.annotation;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.access.BootstrapException;
import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.BeanExpressionResolver;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.expression.StandardBeanExpressionResolver;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import com.betterjr.common.mq.config.MethodRocketMQListenerEndpoint;
import com.betterjr.common.mq.core.RocketMQConsumer;
import com.betterjr.common.mq.core.RocketMQMessageListener;

/**
 * @author liuwl
 */
public class RocketMQAnnotationBeanPostProcessor
        implements BeanPostProcessor, Ordered, BeanFactoryAware, SmartInitializingSingleton {

    private final Set<Class<?>> nonAnnotatedClasses = Collections
            .newSetFromMap(new ConcurrentHashMap<Class<?>, Boolean>(64));

    private final Log logger = LogFactory.getLog(getClass());

    private BeanFactory beanFactory;

    private BeanExpressionResolver resolver = new StandardBeanExpressionResolver();

    private BeanExpressionContext expressionContext;

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }

    /**
     */
    @Override
    public void setBeanFactory(BeanFactory anBeanFactory) {
        this.beanFactory = anBeanFactory;
        if (anBeanFactory instanceof ConfigurableListableBeanFactory) {
            this.resolver = ((ConfigurableListableBeanFactory) anBeanFactory).getBeanExpressionResolver();
            this.expressionContext = new BeanExpressionContext((ConfigurableListableBeanFactory) anBeanFactory, null);
        }
    }

    @Override
    public void afterSingletonsInstantiated() {}

    @Override
    public Object postProcessBeforeInitialization(Object anBean, String anBeanName) throws BeansException {
        return anBean;
    }

    /**
     * @param anBean
     * @param anBeanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(final Object anBean, final String anBeanName) throws BeansException {
        if (!this.nonAnnotatedClasses.contains(anBean.getClass())) {
            Class<?> targetClass = AopUtils.getTargetClass(anBean);
            final Map<Method, Set<RocketMQListener>> annotatedMethods = MethodIntrospector.selectMethods(targetClass,
                    new MethodIntrospector.MetadataLookup<Set<RocketMQListener>>() {

                        @Override
                        public Set<RocketMQListener> inspect(Method method) {
                            Set<RocketMQListener> listenerMethods = findListenerAnnotations(method);
                            return (!listenerMethods.isEmpty() ? listenerMethods : null);
                        }

                    });

            if (annotatedMethods.isEmpty()) {
                this.nonAnnotatedClasses.add(anBean.getClass());
                if (this.logger.isTraceEnabled()) {
                    this.logger.trace("No @RocketMQListener annotations found on anBean type: " + anBean.getClass());
                }
            } else {
                // Non-empty set of methods
                for (Map.Entry<Method, Set<RocketMQListener>> entry : annotatedMethods.entrySet()) {
                    Method method = entry.getKey();
                    for (RocketMQListener listener : entry.getValue()) {
                        try {
                            processRocketMQListener(listener, method, anBean, anBeanName);
                        }
                        catch (Exception e) {
                            throw new BootstrapException("Process RocketMQListener error.", e);
                        }
                    }
                }
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug(annotatedMethods.size() + " @RocketMQListener methods processed on anBean '"
                            + anBeanName + "': " + annotatedMethods);
                }
            }
        }
        return anBean;
    }

    /*
     * AnnotationUtils.getRepeatableAnnotations does not look at interfaces
     */
    private Set<RocketMQListener> findListenerAnnotations(Method anMethod) {
        Set<RocketMQListener> listeners = new HashSet<RocketMQListener>();
        RocketMQListener ann = AnnotationUtils.findAnnotation(anMethod, RocketMQListener.class);
        if (ann != null) {
            listeners.add(ann);
        }
        return listeners;
    }

    protected void processRocketMQListener(RocketMQListener anRocketMQListener, Method anMethod, Object anBean,
            String anBeanName) throws Exception {
        Method methodToUse = checkProxy(anMethod, anBean);
        MethodRocketMQListenerEndpoint endpoint = new MethodRocketMQListenerEndpoint();
        endpoint.setMethod(methodToUse);
        endpoint.setBeanFactory(this.beanFactory);
        processListener(endpoint, anRocketMQListener, anBean);
    }

    private Method checkProxy(Method anMethod, Object anBean) {
        Method method = anMethod;
        if (AopUtils.isJdkDynamicProxy(anBean)) {
            try {
                // Found a @RocketMQListener method on the target class for this
                // JDK proxy ->
                // is it also present on the proxy itself?
                method = anBean.getClass().getMethod(method.getName(), method.getParameterTypes());
                Class<?>[] proxiedInterfaces = ((Advised) anBean).getProxiedInterfaces();
                for (Class<?> iface : proxiedInterfaces) {
                    try {
                        method = iface.getMethod(method.getName(), method.getParameterTypes());
                        break;
                    }
                    catch (NoSuchMethodException noMethod) {}
                }
            }
            catch (SecurityException ex) {
                ReflectionUtils.handleReflectionException(ex);
            }
            catch (NoSuchMethodException ex) {
                throw new IllegalStateException(String.format(
                        "@RocketMQListener method '%s' found on anBean target class '%s', "
                                + "but not found in any interface(s) for anBean JDK proxy. Either "
                                + "pull the method up to an interface or switch to subclass (CGLIB) "
                                + "proxies by setting proxy-target-class/proxyTargetClass " + "attribute to 'true'",
                        method.getName(), method.getDeclaringClass().getSimpleName()), ex);
            }
        }
        return method;
    }

    protected void processListener(MethodRocketMQListenerEndpoint anEndpoint, RocketMQListener anRocketMQListener,
            Object anBean) throws Exception {
        anEndpoint.setBean(anBean);
        RocketMQConsumer consumer = resolveConsumer(anRocketMQListener);
        anEndpoint.setConsumer(consumer);
        anEndpoint.setTopic(resolveTopic(anRocketMQListener));

        RocketMQMessageListener listener = new RocketMQMessageListener(anEndpoint);
        consumer.addSubscriber(listener);
    }

    private RocketMQConsumer resolveConsumer(RocketMQListener rocketMQListener) {
        String consumer = rocketMQListener.consumer();
        if (!StringUtils.isEmpty(consumer)) {
            return (RocketMQConsumer) this.beanFactory.getBean(consumer);
        }
        return null;
    }

    private String resolveTopic(RocketMQListener anRocketMQListener) {
        String topic = anRocketMQListener.topic();
        return topic;
    }

    @SuppressWarnings("unchecked")
    private void resolveAsString(Object anResolvedValue, List<String> anResult) {
        if (anResolvedValue instanceof String[]) {
            for (Object object : (String[]) anResolvedValue) {
                resolveAsString(object, anResult);
            }
        }
        if (anResolvedValue instanceof String) {
            anResult.add((String) anResolvedValue);
        } else if (anResolvedValue instanceof Iterable) {
            for (Object object : (Iterable<Object>) anResolvedValue) {
                resolveAsString(object, anResult);
            }
        } else {
            throw new IllegalArgumentException(
                    String.format("@RocketMQListener can't resolve '%s' as a String", anResolvedValue));
        }
    }

    private Object resolveExpression(String anValue) {
        String resolvedValue = resolve(anValue);

        if (!(resolvedValue.startsWith("#{") && anValue.endsWith("}"))) {
            return resolvedValue;
        }

        return this.resolver.evaluate(resolvedValue, this.expressionContext);
    }

    /**
     * Resolve the specified anValue if possible.
     *
     * @param anValue
     *            the anValue to resolve
     * @return the resolved anValue
     * @see ConfigurableBeanFactory#resolveEmbeddedValue
     */
    private String resolve(String anValue) {
        if (this.beanFactory != null && this.beanFactory instanceof ConfigurableBeanFactory) {
            return ((ConfigurableBeanFactory) this.beanFactory).resolveEmbeddedValue(anValue);
        }
        return anValue;
    }

}
