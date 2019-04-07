package com.oracle.medrec.service.impl;

import com.oracle.medrec.common.testing.EntityRepositoryTestCaseSupport;
import com.oracle.medrec.model.Physician;
import junit.framework.JUnit4TestAdapter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * {@link PhysicianServiceImpl} test case.
 *
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 * @since Jul 17, 2007
 */
public class PhysicianServiceImplTestCase extends EntityRepositoryTestCaseSupport<PhysicianServiceImpl> {

  private Physician physician;

  public static junit.framework.Test suite() {
    return new JUnit4TestAdapter(PhysicianServiceImplTestCase.class);
  }

  @Before
  public void before() {
    physician = EntitiesPreparation.createPhysician();
    service.getEntityManager().persist(physician);
    service.initBuilder();
  }

  @After
  public void after() {
    service.getEntityManager().remove(physician);
  }

  @Test
  public void testAuthenticateAndReturnAdministrator() {
    Physician user = service.authenticateAndReturnPhysician(physician.getUsername(), physician.getPassword());
    assertNotNull(user);

    user = service.authenticateAndReturnPhysician("bogus", physician.getPassword());
    assertNull(user);

    user = service.authenticateAndReturnPhysician(physician.getUsername(), "bogus");
    assertNull(user);
  }
}
