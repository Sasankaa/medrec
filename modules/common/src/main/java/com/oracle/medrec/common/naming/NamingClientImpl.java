package com.oracle.medrec.common.naming;

import com.oracle.medrec.common.core.MethodParameterValidated;
import com.oracle.medrec.common.util.ClassUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.naming.InitialContext;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author : xiaojwu.
 *         Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@ApplicationScoped
public class NamingClientImpl implements NamingClient {

  private static final Logger LOGGER = Logger.getLogger(NamingClientImpl.class.getName());

  private InitialContext context;

  @PostConstruct
  public void init() throws NamingException {
    try {
      this.context = new InitialContext();
    } catch (javax.naming.NamingException e) {
      throw new NamingException("Cannot create JNDI context", e);
    }
  }

  @PreDestroy
  public void destroy() {
    if (context != null) {
      try {
        context.close();
      } catch (javax.naming.NamingException e) {
        LOGGER.log(Level.WARNING, "Cannot close JNDI context", e);
      }
    }
  }

  @MethodParameterValidated
  public <T> T lookup(Class<T> clazz, String name) {

    try {
      return ClassUtils.cast(clazz, context.lookup(name));
    } catch (javax.naming.NamingException e) {
      throw new NamingException("Cannot do JNDI lookup", e);
    }
  }
}
