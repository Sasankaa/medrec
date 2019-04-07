package com.oracle.medrec.common.util;

/**
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public abstract class StringUtils {

  public static boolean isEmpty(String string) {
    return (string == null || string.equals(""));
  }

  public static boolean isNotEmpty(String string) {
    return !isEmpty(string);
  }
}
