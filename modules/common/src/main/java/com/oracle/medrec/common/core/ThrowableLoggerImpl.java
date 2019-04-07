package com.oracle.medrec.common.core;

import com.oracle.medrec.common.util.ThrowableUtils;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@MethodParameterValidated
public class ThrowableLoggerImpl implements ThrowableLogger {

  private static final Logger LOGGER = Logger.getLogger(ThrowableLoggerImpl.class.getName());

  public void log(Throwable t) {
    LOGGER.log(Level.SEVERE, "Exception occurred", t);
    LOGGER.log(Level.SEVERE, ThrowableUtils.getStackTrace(t));
  }
}
