package com.oracle.medrec.common.util;

import junit.framework.JUnit4TestAdapter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * {@link StringUtils} test case.
 *
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 * @since Jul 13, 2007
 */
public class StringUtilsTestCase {

  public static junit.framework.Test suite() {
    return new JUnit4TestAdapter(StringUtilsTestCase.class);
  }

  @Test
  public void isEmptyAndIsNotEmpty() {
    assertTrue(StringUtils.isEmpty(null));
    assertTrue(StringUtils.isEmpty(""));
    assertFalse(StringUtils.isEmpty("foo"));

    assertFalse(StringUtils.isNotEmpty(null));
    assertFalse(StringUtils.isNotEmpty(""));
    assertTrue(StringUtils.isNotEmpty("foo"));
  }
}
