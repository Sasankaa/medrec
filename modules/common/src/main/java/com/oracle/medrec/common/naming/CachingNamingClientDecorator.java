package com.oracle.medrec.common.naming;

import com.oracle.medrec.common.core.MethodInvocationCached;

import javax.annotation.Priority;
import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.enterprise.inject.Any;
import javax.inject.Inject;

import static javax.interceptor.Interceptor.Priority.APPLICATION;

/**
 * A CDI Decorator. Cache any implementations of {@link NamingClient} to improve performance.
 *
 * @author : xiaojwu.
 *         Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 * @see MethodInvocationCached
 */
@Decorator
public class CachingNamingClientDecorator implements NamingClient {

  @Inject
  @Delegate
  @Any
  private NamingClient namingClient;

  public void setNamingClient(NamingClient namingClient) {
    this.namingClient = namingClient;
  }

  @MethodInvocationCached
  public <T> T lookup(Class<T> clazz, String name) {
    return namingClient.lookup(clazz, name);
  }
}
