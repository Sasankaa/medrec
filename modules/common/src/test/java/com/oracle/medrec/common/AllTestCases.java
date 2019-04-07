package com.oracle.medrec.common;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Common module test suite.
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
    suite.addTest(com.oracle.medrec.common.util.AllTestCases.suite());
    suite.addTest(com.oracle.medrec.common.core.AllTestCases.suite());
    suite.addTest(com.oracle.medrec.common.naming.AllTestCases.suite());
    suite.addTest(com.oracle.medrec.common.messaging.AllTestCases.suite());
    return suite;
  }
}
