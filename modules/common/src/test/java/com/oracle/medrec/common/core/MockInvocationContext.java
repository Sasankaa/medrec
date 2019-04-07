package com.oracle.medrec.common.core;

import javax.interceptor.InvocationContext;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author : xiaojwu.
 *         Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public class MockInvocationContext implements InvocationContext {

  private Object result;

  public MockInvocationContext(Object result) {
    this.result = result;
  }

  @Override
  public Object getTarget() {
    return null;
  }

  @Override
  public Object getTimer() {
    return null;
  }

  @Override
  public Method getMethod() {
    try {
      return Object.class.getMethod("toString");  //To change body of implemented methods use File | Settings | File
      // Templates.
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public Constructor<?> getConstructor() {
    return null;
  }

  @Override
  public Object[] getParameters() {
    return new Object[0];  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public void setParameters(Object[] params) {

  }

  @Override
  public Map<String, Object> getContextData() {
    return null;
  }

  @Override
  public Object proceed() throws Exception {
    return result;
  }
}
