package com.betterjr.modules.rule.service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.Proxy;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedCaseInsensitiveMap;

import com.betterjr.common.annotation.AnnotRuleService;
import com.betterjr.common.annotation.RuleServiceType;
import com.betterjr.common.exception.BettjerRuleException;
import com.betterjr.common.mapper.BeanMapper;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.modules.rule.RuleCheckResult;
import com.betterjr.modules.rule.annotation.AnnotRuleFunc;
import com.betterjr.modules.rule.annotation.AnnotRuleProcess;
import com.betterjr.modules.rule.dao.RuleBusinessMapper;
import com.betterjr.modules.rule.entity.BaseRuleFace;
import com.betterjr.modules.rule.entity.RuleBusinStub;
import com.betterjr.modules.rule.entity.RuleBusiness;
import com.betterjr.modules.rule.entity.RuleFace;
import com.betterjr.modules.rule.entity.RuleFunction;
import com.betterjr.modules.rule.entity.RuleInfo;
import com.betterjr.modules.rule.entity.RuleObjectFace;
import com.betterjr.modules.rule.entity.WorkRuleInfo;
import com.betterjr.modules.rule.entity.WorkRuleValidator;
import com.ql.util.express.ExpressRunner;

/**
 * 
 * 规则调度-执行<br>
 * 
 * @author zhoucy
 */
@Service
public class BusinRuleService extends BaseService<RuleBusinessMapper, RuleBusiness> {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	public static String METHOD = "method";
	public static String BUSIN_INFO = "businInfo";

	@Autowired
	private RuleBusinessMapper ruleMapper;

	@Autowired
	private RuleValidatorService validatorService;

	private Map<String, RuleBusiness> businMap = new LinkedCaseInsensitiveMap();

	private Map<String, RuleBusiness> businByMap = new LinkedCaseInsensitiveMap();

	/**
	 * 初始化业务规则
	 */
	public List<RuleFunction> initRule(Map<String, Object> ruleClasses) {
		Map<String, RuleBusiness> tmpBusinMap = new LinkedCaseInsensitiveMap();
		Map<String, RuleFace> ruleMap = new LinkedCaseInsensitiveMap();

		// 初始化验证器
		validatorService.init();

		for (RuleInfo rule : ruleMapper.findAllBusinRule()) {
			ruleMap.put(rule.getRuleNo(), rule);
			rule.clearRule();
		}

		List<RuleFunction> funcMap = annotBuild(ruleClasses, tmpBusinMap, ruleMap);
		Map<String, RuleBusiness> tmpBusinByMap = new LinkedCaseInsensitiveMap();

		for (RuleBusiness busin : this.selectAll()) {
			tmpBusinMap.put(busin.getEnterFunc(), busin);
			tmpBusinByMap.put(busin.getBusinName(), busin);
			composeRule(busin, ruleMap);
			busin.setValidators(validatorService.findValidator(busin.getBusinName()));
		}
		synchronized (businMap) {
			this.businMap = tmpBusinMap;
			this.businByMap = tmpBusinByMap;
		}
		logger.info(businMap.toString());
		return funcMap;
	}

	// 通过注解，构造功能信息
	private RuleFunction buildRuleFunc(String anClass, AnnotRuleFunc anRuleFunc, Method anM, String ruleServiceValue) {
		StringBuilder sb = new StringBuilder();
		StringBuilder sb1 = new StringBuilder();
		sb1.append(anClass).append(".").append(anM.getName()).append("(");
		Parameter[] params = anM.getParameters();
		for (Parameter pp : params) {
			sb.append(pp.getType().getName()).append(";");
			sb1.append(pp.getName()).append(",");
			sb1.append(")");
		}
		if (sb.length() > 1) {
			sb.setLength(sb.length() - 1);
			sb1.setLength(sb.length() - 1);
		}

		RuleFunction fun = new RuleFunction(anRuleFunc, anM.getName(), sb.toString(), sb1.toString());
		fun.setClassName(ruleServiceValue);
		return fun;
	}

	public RuleBusiness buildRuleBusin(AnnotRuleProcess anRuleProc, String anEnterName) {
		RuleBusiness ruleBusin = new RuleBusiness(anRuleProc, anEnterName);

		return ruleBusin;
	}

	public <T> T buildRequest(T request, Map<String, String> anData, String anBusinName) {
		RuleBusiness busin = findRuleByBusinName(anBusinName);
		if (busin != null) {
			String tmpValue;
			String fieldName;

			Map<String, Object> map = new HashMap();
			for (WorkRuleValidator validator : busin.getValidators()) {
				fieldName = validator.getFieldName();
				tmpValue = anData.get(fieldName);
				if (tmpValue != null) {
					map.put(fieldName, tmpValue);
				}
			}
			System.out.println("this is data Map :" + map);
			if (request instanceof Map) {
				request =(T) map;
			}
			else {
				BeanMapper.copy(map, request);
			}
		}

		return request;
	}

	/**
	 * 
	 * 创建注释的规则库信息
	 * 
	 * @param 入参说明及前提条件
	 * @param1 入参说明及前提条件
	 * @return 出参说明，结果条件
	 * @throws 异常情况
	 */
	public List<RuleFunction> annotBuild(Map<String, Object> ruleClasses, Map<String, RuleBusiness> tmpBusinMap,
			Map<String, RuleFace> anRuleMap) {
		List<RuleFunction> funcMap = new LinkedList();
		RuleFunction ruleFunc;
		BaseRuleFace rf = null;

		// 首先处理规则信息
		for (final Object rule : ruleClasses.values()) {
			// final Class<? extends Object> ruleClass = rule.getClass();
			final Class<? extends Object> ruleClass = AopProxyUtils.ultimateTargetClass(rule);
			final AnnotRuleService annot = ruleClass.getAnnotation(AnnotRuleService.class);
			if (annot.type() == RuleServiceType.RULE) {
				if (rule instanceof BasicRule) {
					rf = (BasicRule) rule;
				} else if (rule instanceof RuleObjectFace) {
					rf = RuleProxy.asRule(rule);
					String tmpStr = annot.value();
					if (BetterStringUtils.isNotBlank(tmpStr) == false) {
						tmpStr = ruleClass.getSimpleName();
						tmpStr = tmpStr.toLowerCase().substring(0, 1).concat(tmpStr.substring(1));
					}
					RuleProxy rpp = (RuleProxy) Proxy.getInvocationHandler(rf);
					rpp.setRuleNo(tmpStr);
					rpp.setGroupName(annot.groupName());
				} else {
					throw new BettjerRuleException(40100,
							String.format("Annotation Rule Class '%s', Must implements Interface RuleObjectFace",
									ruleClass.getName()));
				}
				anRuleMap.put(rf.getRuleNo(), rf);
			}
		}

		// 然後處理功能和過程
		for (final Object rule : ruleClasses.values()) {
			// final Class<? extends Object> ruleClass = rule.getClass();
			final Class<? extends Object> ruleClass = AopProxyUtils.ultimateTargetClass(rule);
			final AnnotRuleService annot = ruleClass.getAnnotation(AnnotRuleService.class);
			for (Method mm : ruleClass.getMethods()) {
				for (Annotation subAnnot : mm.getAnnotations()) {
					switch (annot.type()) {
					case FUNC:
						if (subAnnot instanceof AnnotRuleFunc) {
							ruleFunc = buildRuleFunc(ruleClass.getName(), (AnnotRuleFunc) subAnnot, mm, annot.value());
							funcMap.add(ruleFunc);
						}
						break;
					case PROCESS:
						if (subAnnot instanceof AnnotRuleProcess) {
							RuleBusiness ruleBusin = buildRuleBusin((AnnotRuleProcess) subAnnot,
									ruleClass.getSimpleName().concat(".").concat(mm.getName()));
							List ruleList = new LinkedList();
							int workOrder = 0;
							Object ruleObj = null;
							AnnotRuleProcess arp = (AnnotRuleProcess) subAnnot;
							for (String tmpStr : arp.rules()) {
								ruleObj = anRuleMap.get(tmpStr);
								if (ruleObj != null) {
									if (ruleObj instanceof BaseRuleFace) {
										rf = (BaseRuleFace) ruleObj;
										rf.setPriority(new Integer(workOrder));
										ruleObj = rf.clone();
									} else {
										ruleObj = new WorkRuleInfo((RuleInfo) ruleObj, workOrder);
									}
								} else {
									throw new BettjerRuleException(40100,
											String.format(
													"Annotation Rule Process Class '%s', Work Method '%s' use Rule '%s' not define, please checking ",
													ruleClass.getName(), mm.getName(), tmpStr));
								}
								ruleList.add(ruleObj);
							}
							ruleBusin.setRules(ruleList);
							tmpBusinMap.put(ruleBusin.getEnterFunc(), ruleBusin);
						}
						break;

					default:
						break;
					}
				}
			}
		}

		return funcMap;
	}

	public List<RuleFunction> findAllFunc() {

		return ruleMapper.findAllRuleFun();
	}

	public RuleBusiness findRuleByBusinName(String anName) {
		RuleBusiness rb = this.businByMap.get(anName);
		if (logger.isInfoEnabled()) {
			logger.info("this invoke service.method is " + anName + "," + rb);
		}

		return rb;
	}

	public RuleBusiness findRuleBusin(String anName) {
		RuleBusiness rb = this.businMap.get(anName);
		if (logger.isInfoEnabled()) {
			logger.info("this invoke service.method is " + anName + "," + rb);
		}
		return rb;
	}

	public static String getProcessName(String anClass, String anMethod) {

		return anClass.concat(".").concat(anMethod);
	}

	public RuleCheckResult execute(RuleBusiness ruleBusin, ExpressRunner runner, QLExpressContext context)
			throws Exception {
		RuleCheckResult result = new RuleCheckResult();
		if (ruleBusin != null) {
			result = this.validatorService.evaluate(context, result, ruleBusin);
			result = executeRules(result, runner, ruleBusin.getRules(), context);
			if (result.workContinue(ruleBusin.getExecContent())) {
				Object resultObj = runner.execute(ruleBusin.getExecContent(), context, result.getErrorList(), true,
						false);
				result.addRuleResult(ruleBusin.getBusinName(), resultObj);
			}
		}

		return result;
	}

	/**
	 * 
	 * 执行定义的规则信息
	 * 
	 * @param 规则表达式
	 * @param1 规则列表
	 * @param2 规则执行上下文信息
	 * @return 出参说明，结果条件
	 * @throws 异常情况
	 */
	private RuleCheckResult executeRules(RuleCheckResult result, ExpressRunner runner, List<RuleFace> rules,
			QLExpressContext ruleContext) {
		try {
			boolean hasOk = false;
			for (Object obj : rules) {
				hasOk = false;
				// 如果规则匹配，则执行
				if (obj instanceof WorkRuleInfo) {
					if (workSingleRule(result, obj, runner, ruleContext) == false) {

						return result;
					}
				} else if (obj instanceof Collection) {
					for (Object subObj : (Collection) obj) {
						if (workSingleRule(result, subObj, runner, ruleContext)) {
							hasOk = true;
							break;
						}
					}
					if (hasOk == false) {

						return result;
					}
				}

			}
		} catch (Exception ex) {
			throw new BettjerRuleException(49999, "Rule Execute Has error!", ex);
		}
		return result;
	}

	private boolean workSingleRule(RuleCheckResult anResult, Object subObj, ExpressRunner runner,
			QLExpressContext ruleContext) throws Exception {
		WorkRuleInfo rule;
		if (subObj instanceof WorkRuleInfo) {
			rule = (WorkRuleInfo) subObj;
			// 检查规则的有效性
			if (rule.checkValid()) {
				Boolean result = (Boolean) runner.execute(rule.getMatchCond(), ruleContext, anResult.getErrorList(),
						true, false);
				if (result != null && result.booleanValue()) {
					Object resultObj = runner.execute(rule.getExecContent(), ruleContext, anResult.getErrorList(), true,
							false);
					anResult.addRuleResult(rule.getRuleNo(), resultObj);
				} else {
					return false;
				}
			}
		} else if (subObj instanceof BaseRuleFace) {

			// 分解为两种情况，一种是直接对象实现，另外一种使用代理模式实现。
			BaseRuleFace br = (BaseRuleFace) subObj;
			br = (BaseRuleFace) br.clone();
			br.setContext(ruleContext);
			if (br.evaluate()) {
				anResult.addRuleResult(br.getRuleNo(), br.execute());
			} else {
				return false;
			}
		}

		return true;

	}

	/**
	 * 在此处把一些中文符号替换成英文符号
	 * 
	 * @param statement
	 * @return
	 */
	public static String initStatement(String statement) {
		if (BetterStringUtils.isNotBlank(statement)) {
			return statement.replace("（", "(").replace("）", ")").replace("；", ";").replace("，", ",").replace("“", "\"")
					.replace("”", "\"").replace("‘", "'");
		} else {
			return statement;
		}
	}

	/**
	 * 组合规则
	 * 
	 * @param anBusin
	 *            业务过程
	 * @param anRuleMap
	 *            规则列表
	 */
	public void composeRule(RuleBusiness anBusin, Map<String, RuleFace> anRuleMap) {
		List<RuleFace> workRuleList = new LinkedList<>();
		RuleFace ruleInfo = null;
		Map<String, List<RuleFace>> tmpGroup = new HashMap();
		String tmpGroupName;
		Object obj = null;
		List<RuleFace> tmpList = null;
		for (RuleBusinStub stub : this.ruleMapper.findRulesByBusinName(anBusin.getBusinName())) {
			obj = anRuleMap.get(stub.getRuleNo());
			if (obj != null && obj instanceof RuleInfo) {
				ruleInfo = new WorkRuleInfo((RuleInfo) obj, stub);
			} else if (obj != null && obj instanceof BaseRuleFace) {
				ruleInfo = (RuleFace) obj;
			} else {
				// 定义业务流程定义中没有业务规则，属于异常，需要告知管理员
				logger.warn("not find declare Rule In RuleList", stub);
				throw new BettjerRuleException(40001, "not find declare Rule In RuleList " + stub);
			}

			tmpGroupName = ruleInfo.getGroupName();
			// 检查并行判断的规则是否存在
			if (BetterStringUtils.isNotBlank(tmpGroupName)) {
				if (tmpGroup.containsKey(tmpGroupName)) {
					obj = tmpGroup.get(tmpGroupName);
					tmpList = (List) obj;
					tmpList.add(ruleInfo);
					continue;
				} else {
					tmpList = new ArrayList();
					tmpList.add(ruleInfo);
				}
				tmpGroup.put(tmpGroupName, tmpList);
			}
			workRuleList.add(ruleInfo);
		}

		Collections.sort(workRuleList);

		List resultList = new LinkedList<>();
		// 解决并行的问题
		for (RuleFace tmpRule : workRuleList) {
			tmpGroupName = tmpRule.getGroupName();
			if (BetterStringUtils.isNotBlank(tmpGroupName)) {
				tmpList = tmpGroup.get(tmpGroupName);
			} else {
				tmpList = null;
			}
			if (tmpList == null || tmpList.size() == 1) {
				resultList.add(tmpRule);
			} else {
				resultList.add(tmpList);
			}
		}
		anBusin.setRules(resultList);
		workRuleList.clear();
		tmpGroup.clear();
	}

}
