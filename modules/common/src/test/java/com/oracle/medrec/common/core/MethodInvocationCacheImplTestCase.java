package com.oracle.medrec.common.core;

import junit.framework.JUnit4TestAdapter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public class MethodInvocationCacheImplTestCase {

  private MethodInvocationCache methodInvocationCache;
  private String str = "aaa";

  public static junit.framework.Test suite() {
    return new JUnit4TestAdapter(MethodInvocationCacheImplTestCase.class);
  }

  @Before
  public void setup() throws Exception {
    methodInvocationCache = new MethodInvocationCacheImpl();
    ((MethodInvocationCacheImpl) methodInvocationCache).initDefaultCache();
    methodInvocationCache.addResult(String.class.getMethod("hashCode"), str.hashCode(), new Object[0]);
    methodInvocationCache.addResult(String.class.getMethod("startsWith", String.class), str.startsWith("a"),
        new Object[]{"a"});
  }

  @Test
  public void findResult() throws NoSuchMethodException {
    try {
      int hashCode = (Integer) methodInvocationCache.findResult(String.class.getMethod("hashCode"), new Object[0]);
      assertEquals(str.hashCode(), hashCode);
    } catch (ResultNotCachedException e) {
      fail();
    }
    try {
      boolean result = (Boolean) methodInvocationCache.findResult(String.class.getMethod("startsWith", String.class),
          new Object[]{"a"});
      assertEquals(str.startsWith("a"), result);
    } catch (ResultNotCachedException e) {
      fail();
    }
  }

  @Test
  public void findNonExistingResult() throws NoSuchMethodException {
    try {
      methodInvocationCache.findResult(String.class.getMethod("endsWith", String.class), new Object[]{"a"});
      fail();
    } catch (ResultNotCachedException e) {
    }
    try {
      methodInvocationCache.findResult(String.class.getMethod("startsWith", String.class), new Object[]{"b"});
      fail();
    } catch (ResultNotCachedException e) {
    }
  }

  @Test
  public void invalidateResultsByMethod() throws NoSuchMethodException {
    methodInvocationCache.invalidateResultsByMethod(String.class.getMethod("startsWith", String.class));

    try {
      methodInvocationCache.findResult(String.class.getMethod("startsWith", String.class), new Object[]{"a"});
      fail();
    } catch (ResultNotCachedException e) {
    }

    try {
      methodInvocationCache.findResult(String.class.getMethod("hashCode"), new Object[0]);
    } catch (ResultNotCachedException e) {
      fail();
    }
  }

  @Test
  public void invalidateAllResults() throws NoSuchMethodException {
    methodInvocationCache.invalidateAllResults();

    try {
      methodInvocationCache.findResult(String.class.getMethod("hashCode"), null);
      fail();
    } catch (ResultNotCachedException e) {
    }

    try {
      methodInvocationCache.findResult(String.class.getMethod("startsWith", String.class), new Object[]{"a"});
      fail();
    } catch (ResultNotCachedException e) {
    }
  }
}
