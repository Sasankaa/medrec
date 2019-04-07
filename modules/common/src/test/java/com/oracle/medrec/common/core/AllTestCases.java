package com.oracle.medrec.common.core;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Test suite of com.oracle.medrec.common.core package.
 *
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 * @since Jul 17, 2007
 */
public class AllTestCases extends TestCase {
  public AllTestCases(String name) {
    super(name);
  }

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTest(MethodParameterValidatorImplTestCase.suite());
    suite.addTest(MethodParameterValidatingInterceptorTestCase.suite());
    suite.addTest(MethodInvocationCacheImplTestCase.suite());
    suite.addTest(MethodInvocationCachingInterceptorTestCase.suite());
    suite.addTest(ThrowableLoggingInterceptorTestCase.suite());
    return suite;
  }
}
