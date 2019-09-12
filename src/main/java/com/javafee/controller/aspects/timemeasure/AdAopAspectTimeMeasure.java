package com.javafee.controller.aspects.timemeasure;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import com.javafee.controller.algorithm.decisionrules.DecisionRulesGenerator;

import lombok.extern.java.Log;

@Aspect
@Log
public class AdAopAspectTimeMeasure {
	@Around("com.javafee.controller.aspects.timemeasure.AdAopExpression.execPointcut()")
	public Object execTimeMeasure(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		long begin = System.currentTimeMillis(), duration = 0L;

		Object result = null;
		try {
			result = proceedingJoinPoint.proceed();
			duration = System.currentTimeMillis() - begin;
			((DecisionRulesGenerator) proceedingJoinPoint.getTarget()).setTimeMeasure(duration);
		} catch (Exception e) {
			log.severe("[ [AdAopAspectTimeMeasure] EXCEPTION execTimeMeasure ]");
			//throw e;
		}

		System.out.println("[ [AdAopAspectTimeMeasure] execTimeMeasure : duration " + duration / 1000 + "s. ]");
		return result;
	}
}
