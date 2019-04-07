package com.oracle.medrec.common.util;

import com.oracle.medrec.common.core.SystemException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public abstract class ClassUtils {

  public static Class getGenericArgumentType(Class currentClass, Class genericSuperClass, int argumentIndex) {
    Type superType = currentClass.getGenericSuperclass();
    if (superType == null) {
      throw new IllegalArgumentException("Class '" + currentClass + "' doesn't have correct generic super class");
    }
    if (!(superType instanceof ParameterizedType) || genericSuperClass != null && ((ParameterizedType) superType)
        .getRawType() != genericSuperClass) {
      return getGenericArgumentType(currentClass.getSuperclass(), genericSuperClass, argumentIndex);
    }
    Object[] args = ((ParameterizedType) superType).getActualTypeArguments();
    if (argumentIndex >= args.length) {
      throw new IllegalArgumentException("Invalid argument index: " + argumentIndex);
    }
    return cast(Class.class, args[argumentIndex]);
  }

  public static Class getGenericArgumentType(Class currentClass, Class genericSuperClass) {
    return getGenericArgumentType(currentClass, genericSuperClass, 0);
  }

  public static Class getGenericArgumentType(Class currentClass, int argumentIndex) {
    return getGenericArgumentType(currentClass, null, argumentIndex);
  }

  public static Class getGenericArgumentType(Class currentClass) {
    return getGenericArgumentType(currentClass, null, 0);
  }

  public static <T> T instantiateClass(Class<T> clazz) {
    try {
      return clazz.newInstance();
    } catch (InstantiationException e) {
      throw new SystemException("Cannot instantiate class: " + clazz.getName(), e);
    } catch (IllegalAccessException e) {
      throw new SystemException("Cannot instantiate class: " + clazz.getName(), e);
    }
  }

  @SuppressWarnings("unchecked")
  public static <T> T cast(Class<T> clazz, Object object) {
    if (clazz.isAssignableFrom(object.getClass())) {
      return clazz.cast(object);
    }
    throw new ClassCastException("Cannot cast class " + object.getClass().getName() + ", " +
        "whose classloader is " + object.getClass().getClassLoader() + ", to class " + clazz + ", " +
        "whose classloader is " + clazz.getClassLoader());
  }

  public static Class forName(String className) {
    try {
      return Class.forName(className, false, Thread.currentThread().getContextClassLoader());
    } catch (ClassNotFoundException e) {
      throw new SystemException(e);
    }
  }

}
