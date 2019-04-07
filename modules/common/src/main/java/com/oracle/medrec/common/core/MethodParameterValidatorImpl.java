package com.oracle.medrec.common.core;

import javax.enterprise.context.ApplicationScoped;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * An implementation of method parameter validator supporting {@link MethodParameterValidatingInterceptor}.
 *
 * @author : xiaojwu.
 *         Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@ApplicationScoped
public class MethodParameterValidatorImpl implements MethodParameterValidator {

  public void validateParameters(Method method, Object[] params) {
    if (params == null) {
      throw new IllegalArgumentException("Param array is null");
    }
    int i = 0;
    for (Object param : params) {
      if (param == null) {
        boolean nullable = false;
        for (Annotation annotation : method.getParameterAnnotations()[i]) {
          if (Nullable.class == annotation.annotationType()) {
            nullable = true;
          }
        }
        if (!nullable) {
          throw new IllegalArgumentException("The number " + (i + 1) + " parameter of type '" + method
              .getParameterTypes()[i].getName() + "' in method '" + method.getDeclaringClass().getName() + "." +
              method.getName() + "()' is null");
        }
      }
      i++;
    }
  }
}
