package com.javafee.controller.aspects.aopexpression;

import org.aspectj.lang.annotation.Pointcut;

public class AdAopExpression {
	@Pointcut("execution(* com.javafee.controller.algorithm.decisionrules.StandardDecisionRulesGenerator.generate(..))")
	public void execFor_StandardDecisionRulesGenerator_generate() {
	}

	@Pointcut("execFor_StandardDecisionRulesGenerator_generate()")
	public void execPointcut() {
	}
}
