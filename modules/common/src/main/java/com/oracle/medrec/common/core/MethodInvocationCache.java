package com.oracle.medrec.common.core;

import java.lang.reflect.Method;

/**
 * Method invocation cache interface supporting {@link MethodInvocationCachingInterceptor}.
 *
 * @author : xiaojwu.
 *         Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public interface MethodInvocationCache {

  /**
   * Check method invocation cache and return the result if it exists.
   *
   * @param method
   * @param parameters
   * @return
   * @throws ResultNotCachedException
   */
  Object findResult(Method method, Object[] parameters) throws ResultNotCachedException;

  /**
   * Add result into method invocation cache.
   *
   * @param method
   * @param returnValue
   * @param parameters
   */
  void addResult(Method method, Object returnValue, Object[] parameters);

  /**
   * Empty the whole contents of cache.
   */
  void invalidateAllResults();

  /**
   * Empty the content of cache by method.
   *
   * @param method
   */
  void invalidateResultsByMethod(Method method);

  /**
   * Empty the content of cache by method and its parameters.
   *
   * @param method
   * @param parameters
   */
  void invalidateResultsByMethodAndParameters(Method method, Object[] parameters);

  /**
   * Check whether the cache is available.
   *
   * @return
   */
  boolean isCacheEffective();
}
