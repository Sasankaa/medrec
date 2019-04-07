package com.oracle.medrec.common.core;

import junit.framework.JUnit4TestAdapter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public class MethodParameterValidatorImplTestCase {

  private MethodParameterValidator methodParameterValidator = new MethodParameterValidatorImpl();

  public static junit.framework.Test suite() {
    return new JUnit4TestAdapter(MethodParameterValidatorImplTestCase.class);
  }

  @Test
  public void validateObjectParameter() throws NoSuchMethodException {
    try {
      methodParameterValidator.validateParameters(String.class.getMethod("equals", Object.class), new Object[]{null});
    } catch (IllegalArgumentException e) {
      String message = "The number 1 parameter of type 'java.lang.Object' in method 'java.lang.String.equals()' is " +
          "null";
      assertEquals(message, e.getMessage());
    }

    try {
      methodParameterValidator.validateParameters(String.class.getMethod("equals", Object.class), new Object[]{"a"});
    } catch (IllegalArgumentException e) {
      fail();
    }
  }

  @Test
  public void validatePrimitiveParameter() throws NoSuchMethodException {
    try {
      methodParameterValidator.validateParameters(Object.class.getMethod("wait", Long.TYPE), new Object[]{0});
    } catch (IllegalArgumentException e) {
      fail();
    }
  }

  @Test
  public void validateNullableParameter() throws NoSuchMethodException {
    try {
      methodParameterValidator.validateParameters(Foo.class.getMethod("foo", String.class, String.class),
          new Object[]{"", null});
    } catch (IllegalArgumentException e) {
      fail();
    }

    try {
      methodParameterValidator.validateParameters(Foo.class.getMethod("foo", String.class, String.class),
          new Object[]{null, ""});
      fail();
    } catch (IllegalArgumentException e) {
    }
  }

  public static class Foo {
    public void foo(String s1, @Nullable String s2) {
    }
  }
}
