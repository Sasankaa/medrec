package com.oracle.medrec.common.core;

import java.lang.reflect.Method;

/**
 * Method parameter validator interface supporting {@link MethodParameterValidatingInterceptor}.
 *
 * @author : xiaojwu.
 *         Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public interface MethodParameterValidator {

  void validateParameters(Method method, Object[] params);
}
