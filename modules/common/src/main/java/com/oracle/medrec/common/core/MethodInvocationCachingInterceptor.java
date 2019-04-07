package com.oracle.medrec.common.core;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.util.logging.Logger;

/**
 * A CDI Interceptor for caching the return data depending on method's name and parameters.
 * First invocation does the real method
 * and then the return will be cached in according to the method's name and parameters at this time.
 * After that, invoke the method with same parameters is going to retrieve return value from cache.
 *
 * @author : xiaojwu.
 *         Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 * @see MethodInvocationCached
 */
@Interceptor
@MethodInvocationCached
public class MethodInvocationCachingInterceptor {

  private static final Logger LOGGER = Logger.getLogger(MethodInvocationCachingInterceptor.class.getName());

  @Inject
  private MethodInvocationCache methodInvocationCache;

  public void setMethodReturnValueCache(MethodInvocationCache methodInvocationCache) {
    this.methodInvocationCache = methodInvocationCache;
  }

  @AroundInvoke
  public Object checkInCache(InvocationContext invocationContext) throws Exception {
    if (!methodInvocationCache.isCacheEffective()) {
      return invocationContext.proceed();
    }
    LOGGER.info("Checking method " + invocationContext.getMethod() + " invocation cache...");
    Object result;
    try {
      result = methodInvocationCache.findResult(invocationContext.getMethod(), invocationContext.getParameters());
      return result;
    } catch (ResultNotCachedException e) {
      LOGGER.finer("Not find the result.");
    }
    result = invocationContext.proceed();
    LOGGER.info("Caching the result in method " + invocationContext.getMethod().getName() + " invocation cache....");
    methodInvocationCache.addResult(invocationContext.getMethod(), result, invocationContext.getParameters());
    return result;
  }
}
