package com.oracle.medrec.common.core;

import junit.framework.JUnit4TestAdapter;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.junit.Test;

import javax.interceptor.InvocationContext;

/**
 * {@link ThrowableLoggingInterceptor} test case.
 *
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 * @since Jul 17, 2007
 */
public class ThrowableLoggingInterceptorTestCase {

  public static junit.framework.Test suite() {
    return new JUnit4TestAdapter(ThrowableLoggingInterceptorTestCase.class);
  }

  @Test(expected = FooException.class)
  public void logException() throws Throwable {
    ThrowableLogger logger = createMock(ThrowableLogger.class);
    InvocationContext ctx = createMock(InvocationContext.class);

    ThrowableLoggingInterceptor interceptor = new ThrowableLoggingInterceptor();
    interceptor.setExceptionLogger(logger);

    FooException ex = new FooException();
    ctx.proceed();
    expectLastCall().andThrow(ex);
    logger.log(ex);
    expectLastCall().once();

    replay(logger);
    replay(ctx);
    interceptor.logException(ctx);
    verify(logger);
    verify(ctx);
  }

  public static class FooException extends Exception {
    public FooException() {
    }
  }
}
