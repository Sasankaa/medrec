package com.oracle.medrec.common.naming;

/**
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public interface NamingClient {

  <T> T lookup(Class<T> clazz, String name);
}
