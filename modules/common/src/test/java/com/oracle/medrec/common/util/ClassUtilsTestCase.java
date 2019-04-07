package com.oracle.medrec.common.util;

import junit.framework.JUnit4TestAdapter;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * {@link ClassUtils} test case.
 *
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 * @since Jul 13, 2007
 */
public class ClassUtilsTestCase {

  public static junit.framework.Test suite() {
    return new JUnit4TestAdapter(ClassUtilsTestCase.class);
  }

  @Test(expected = ClassCastException.class)
  public void cast() {
    Object obj = new Child();
    ClassUtils.cast(Parent.class, obj);
    ClassUtils.cast(Child.class, obj);
    ClassUtils.cast(String.class, obj);
  }

  @Test(expected = Exception.class)
  public void instantiateClass() {
    assertNotNull(ClassUtils.instantiateClass(Child.class));

    ClassUtils.instantiateClass(Parent.class);
    ClassUtils.instantiateClass(AnotherChild.class);
  }

  public static interface Parent {
  }

  public static class Child implements Parent {
  }

  public static class AnotherChild implements Parent {
    private final String name;

    public AnotherChild(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }
  }
}
