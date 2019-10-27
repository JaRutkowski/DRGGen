package com.javafee.controller.aspects.aopexpression;

import org.aspectj.lang.annotation.Pointcut;

public class AtAopExpression {
	@Pointcut("execution(* com.javafee.controller.algorithm..*(..))")
	public void execFor_allAlgorithmPackage() {
	}

	@Pointcut("execFor_allAlgorithmPackage()")
	public void execPointcut() {
	}
}
