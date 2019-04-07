package com.oracle.medrec.common.core;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.util.logging.Logger;

/**
 * A CDI Interceptor for validating method's parameters legal or not.
 *
 * @author : xiaojwu.
 *         Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@Interceptor
@MethodParameterValidated
public class MethodParameterValidatingInterceptor {

  private static final Logger LOGGER = Logger.getLogger(MethodParameterValidatingInterceptor.class.getName());

  @Inject
  private MethodParameterValidator methodParameterValidator;

  public void setMethodParameterValidator(MethodParameterValidator methodParameterValidator) {
    this.methodParameterValidator = methodParameterValidator;
  }

  @AroundInvoke
  public Object validateParameters(InvocationContext invocationContext) throws Exception {
    LOGGER.info("Validating method " + invocationContext.getMethod().getName() + " parameters...");
    methodParameterValidator.validateParameters(invocationContext.getMethod(), invocationContext.getParameters());
    return invocationContext.proceed();
  }
}
