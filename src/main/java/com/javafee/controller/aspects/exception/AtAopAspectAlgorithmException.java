package com.javafee.controller.aspects.exception;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;

import com.javafee.controller.utils.SystemProperties;
import com.javafee.forms.utils.Utils;

@Aspect
public class AtAopAspectAlgorithmException {
	@AfterThrowing(pointcut = "com.javafee.controller.aspects.aopexpression.AtAopExpression.execPointcut()",
			throwing = "exception")
	public void exec(JoinPoint joinPoint, Throwable exception) {
		Utils.displayErrorOptionPane(SystemProperties.getResourceBundle().getString("optionPane.errorOptionPaneTitle"),
				SystemProperties.getResourceBundle().getString("optionPane.errorOptionPaneMessage"), exception, null);
	}
}
