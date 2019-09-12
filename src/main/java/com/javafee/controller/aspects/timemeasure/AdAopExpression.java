package com.javafee.controller.aspects.timemeasure;

import org.aspectj.lang.annotation.Pointcut;

public class AdAopExpression {
	@Pointcut("execution(* com.javafee.controller.algorithm.decisionrules.StandardDecisionRulesGenerator.generate(..))")
	//@Pointcut("execution(* com.ghag.rnd.aspects.ltw.SampleService.doService(..))")
	public void execInitializeListeners() {
	}

	@Pointcut("execInitializeListeners()")
	public void execPointcut() {
	}
}
