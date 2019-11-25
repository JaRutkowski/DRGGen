package com.javafee.controller.aspects.aopexpression;

import org.aspectj.lang.annotation.Pointcut;

public class AdAopExpression {
	@Pointcut("execution(* com.javafee.controller.algorithm.decisionrules.StandardDecisionRulesGenerator.generate(..))")
	public void execFor_StandardDecisionRulesGenerator_generate() {
	}

	@Pointcut("execution(* com.javafee.controller.algorithm.decisionrules.InconsistentDataDecisionRulesGenerator.generate(..))")
	public void execFor_InconsistentDataDecisionRulesGenerator_generate() {
	}

	@Pointcut("execFor_StandardDecisionRulesGenerator_generate() || execFor_InconsistentDataDecisionRulesGenerator_generate()")
	public void execPointcut() {
	}
}
