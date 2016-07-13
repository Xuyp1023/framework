package com.betterjr.common.mq.config;

import java.lang.reflect.Method;

import com.betterjr.common.mq.core.RocketMQConsumer;

/**
 *  RocketMQListener 注解对象包装
 *  @author liuwl
 */
public class MethodRocketMQListenerEndpoint extends AbstractRocketMQListenerEndpoint{

	private Object bean;

	private Method method;

	private RocketMQConsumer consumer;

	public void setBean(Object anBean) {
		this.bean = anBean;
	}

	public Object getBean() {
		return this.bean;
	}

	public void setMethod(Method anMethod) {
		this.method = anMethod;
	}

	public Method getMethod() {
		return this.method;
	}

	@Override
	protected StringBuilder getEndpointDescription() {
		return super.getEndpointDescription()
				.append(" | bean='").append(this.bean).append("'")
				.append(" | method='").append(this.method).append("'");
	}

    @Override
	public void setConsumer(final RocketMQConsumer anConsumer) {
		this.consumer = anConsumer;
	}

	@Override
	public RocketMQConsumer getConsumer() {
		return this.consumer;
	}


}
