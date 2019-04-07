package com.oracle.medrec.common.messaging;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Test suite of com.oracle.medrec.common.messaging package.
 *
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 * @since Jul 18, 2007
 */
public class AllTestCases extends TestCase {
  public AllTestCases(String name) {
    super(name);
  }

  public static Test suite() {
    TestSuite suite = new TestSuite();
    // suite.addTest(new JUnit4TestAdapter(TestMessageConverterImpl.class));
    // suite.addTest(new JUnit4TestAdapter(TestMessageGatewayImpl.class));
    return suite;
  }
}
