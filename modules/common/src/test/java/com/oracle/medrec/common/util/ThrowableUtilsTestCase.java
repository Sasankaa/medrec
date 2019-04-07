package com.oracle.medrec.common.util;

import junit.framework.JUnit4TestAdapter;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * {@link ThrowableUtils} test case.
 *
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 * @since Jul 13, 2007
 */
public class ThrowableUtilsTestCase {

  public static junit.framework.Test suite() {
    return new JUnit4TestAdapter(ThrowableUtilsTestCase.class);
  }

  @Test
  public void testGetStackTrace() {
    Exception ex = new Exception();
    assertNotNull(ThrowableUtils.getStackTrace(ex));
  }

  @Test(expected = NullPointerException.class)
  public void testGetStackTraceWithNullException() {
    ThrowableUtils.getStackTrace(null);
  }
}
