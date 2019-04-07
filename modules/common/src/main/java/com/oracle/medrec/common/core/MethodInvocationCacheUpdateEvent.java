package com.oracle.medrec.common.core;

import java.lang.reflect.Method;

/**
 * A CDI Event for data update in cache.
 * Some other methods might cause the real data changed.
 * So the method annotated @{@link MethodInvocationCached} should get the real data again not from cache.
 * Fire this event at the cause position, meanwhile, the implementation of cache should observer it.
 *
 * @author : xiaojwu.
 *         Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public class MethodInvocationCacheUpdateEvent {

  private Method method;

  private Object[] parameters;

  public Method getMethod() {
    return method;
  }

  public void setMethod(Method method) {
    this.method = method;
  }

  public Object[] getParameters() {
    return parameters;
  }

  public void setParameters(Object[] parameters) {
    this.parameters = parameters;
  }
}
