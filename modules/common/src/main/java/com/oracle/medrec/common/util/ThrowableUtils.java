package com.oracle.medrec.common.util;

import org.apache.commons.lang.exception.ExceptionUtils;

/**
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public abstract class ThrowableUtils {

  public static String getStackTrace(Throwable throwable) {
    return ExceptionUtils.getStackTrace(throwable);
  }
}
