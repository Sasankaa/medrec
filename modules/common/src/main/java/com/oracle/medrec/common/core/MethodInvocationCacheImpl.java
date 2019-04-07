package com.oracle.medrec.common.core;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import com.oracle.medrec.common.util.ServerPropertiesUtils;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.cache.TypeAssertion;

/**
 * An implementation of method invocation cache supporting {@link MethodInvocationCachingInterceptor}.
 *
 * @author : xiaojwu.
 *         Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@MethodParameterValidated
@ApplicationScoped
public class MethodInvocationCacheImpl implements MethodInvocationCache {

  private static final Logger LOGGER = Logger.getLogger(MethodInvocationCacheImpl.class.getName());

  private final NullObject NULL_RESULT = new NullObject();

  private Map<String, CacheValue> methodCache;

  private boolean isEffective = true;

  @Resource(name = "java:app/com.oracle.medrec.target")
  private String target;

  @PostConstruct
  public void initCache() {
    ServerPropertiesUtils.checkTarget(target);
    if (ServerPropertiesUtils.isOnCoherence()) {
      methodCache = CacheFactory.getTypedCache(
          "MethodCache", TypeAssertion.withTypes(String.class, CacheValue.class));
      LOGGER.info("Method Invocation Coherence Cache is available");
    } else if (ServerPropertiesUtils.isOnServer()) {
      methodCache = new ConcurrentHashMap<>();
      LOGGER.info("Method Invocation default Cache is available");
    } else {
      LOGGER.info("None of Method Invocation Cache is available");
      methodCache = new ConcurrentHashMap<>(0);
      isEffective = false;
    }
  }

  public void initDefaultCache() {
    methodCache = new ConcurrentHashMap<>();
  }

  public boolean isCacheEffective() {
    return isEffective;
  }

  public Object findResult(Method method, Object[] parameters) throws ResultNotCachedException {
    // check method
    CacheValue mapResults = methodCache.get(method.getName());
    if (mapResults == null) {
      throw new ResultNotCachedException();
    }

    // check parameters
    Object result = mapResults.get(new ParameterList(parameters));
    if (result == null) {
      throw new ResultNotCachedException();
    }
    LOGGER.info("Found result in cache");
    LOGGER.info("Method: " + method.getName());
    LOGGER.info("Parameters: " + Arrays.toString(parameters));
    LOGGER.info("Result: " + result);
    if (result.getClass() == NullObject.class) {
      return null;
    }
    return result;
  }

  public void addResult(Method method, @Nullable Object result, Object[] parameters) {
    if (result == null) {
      result = NULL_RESULT;
    }

    // check if it has existed in cache
    CacheValue mapResults = methodCache.get(method.getName());
    if (mapResults== null) {
      mapResults = new CacheValue();
    }
    // cache result
    mapResults.put(new ParameterList(parameters), result);
    methodCache.put(method.getName(), mapResults);
    LOGGER.info("Added result to cache");
    LOGGER.info("Method: " + method.getName());
    LOGGER.info("Parameters: " + Arrays.toString(parameters));
    LOGGER.info("Result: " + result);
  }

  public void invalidateAllResults() {
    methodCache.clear();
  }

  public void invalidateResultsByMethod(Method method) {
    methodCache.remove(method.getName());
  }

  public void invalidateResultsByMethodAndParameters(Method method, Object[] parameters) {
    CacheValue mapResults = methodCache.get(method.getName());
    if (mapResults != null) {
      mapResults.remove(new ParameterList(parameters));
      methodCache.put(method.getName(), mapResults);
    }
  }

  /**
   * Observer {@link MethodInvocationCacheUpdateEvent} and then empty relevant content.
   *
   * @param event
   */
  public void handleCacheUpdateEvent(@Observes MethodInvocationCacheUpdateEvent event) {
    if (isCacheEffective()) {
      LOGGER.info("Receive " + event.getMethod() + " cache update event.");
      invalidateResultsByMethodAndParameters(event.getMethod(), event.getParameters());
    }
  }

  public static class ParameterList implements Serializable {
    private Object[] parameters;

    public ParameterList(Object[] parameters) {
      this.parameters = parameters;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      ParameterList that = (ParameterList) o;

      // Probably incorrect - comparing Object[] arrays with Arrays.equals
      if (!Arrays.equals(parameters, that.parameters)) {
        return false;
      }

      return true;
    }

    @Override
    public int hashCode() {
      return parameters != null ? Arrays.hashCode(parameters) : 0;
    }
  }

  static class NullObject implements Serializable {

  }

  static class CacheValue extends ConcurrentHashMap<ParameterList,Object> implements Serializable {

  }
}
