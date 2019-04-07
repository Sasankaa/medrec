package com.oracle.medrec.facade.broker.rmi;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Test suite of com.oracle.medrec.facade.broker.rmi package.
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
    suite.addTest(RmiPatientFacadeBrokerTestCase.suite());
    return suite;
  }
}
