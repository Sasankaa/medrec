package com.oracle.medrec.common.naming;

import junit.framework.JUnit4TestAdapter;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * {@link CachingNamingClientDecorator} test case.
 *
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 * @since Jul 18, 2007
 */
public class CachingNamingClientDecoratorTestCase {

  public static junit.framework.Test suite() {
    return new JUnit4TestAdapter(CachingNamingClientDecoratorTestCase.class);
  }

  @Test
  public void lookup() {
    NamingClient namingClient = createMock(NamingClient.class);
    CachingNamingClientDecorator decorator = new CachingNamingClientDecorator();
    decorator.setNamingClient(namingClient);

    namingClient.lookup(String.class, "foo");
    expectLastCall().andReturn("foo");

    replay(namingClient);
    assertEquals("foo", decorator.lookup(String.class, "foo"));
    verify(namingClient);
  }
}
