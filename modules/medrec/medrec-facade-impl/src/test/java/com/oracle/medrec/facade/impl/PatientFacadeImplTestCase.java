package com.oracle.medrec.facade.impl;

import com.oracle.medrec.facade.model.FoundPatient;
import com.oracle.medrec.model.Patient;
import com.oracle.medrec.model.PersonName;
import com.oracle.medrec.service.PatientService;
import junit.framework.JUnit4TestAdapter;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

/**
 * {@link PatientFacadeImpl} test case.
 *
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 * @since Jul 17, 2007
 */
public class PatientFacadeImplTestCase {

  private PatientService ps;
  private PatientFacadeImpl impl;

  public static junit.framework.Test suite() {
    return new JUnit4TestAdapter(PatientFacadeImplTestCase.class);
  }

  @Before
  public void setUp() {
    ps = createMock(PatientService.class);
    impl = new PatientFacadeImpl();
    impl.setPatientService(ps);
  }

  @Test
  public void testGetPatient() {
    Patient patient = new Patient();
    ps.getPatient(1L);
    expectLastCall().andReturn(patient);
    ps.getPatient(2L);
    expectLastCall().andReturn(null);
    replay(ps);
    assertSame(patient, impl.getPatient(1L));
    assertNull(impl.getPatient(2L));
    verify(ps);
  }

  @Test
  public void testQueryPatientBySsn() {
    ps.findApprovedPatientBySsn("SSN1");
    expectLastCall().andReturn(null);
    Patient patient = new Patient();
    patient.setName(new PersonName());
    patient.setSsn("SSN2");
    patient.setDob(new Date());
    ps.findApprovedPatientBySsn("SSN2");
    expectLastCall().andReturn(patient);

    replay(ps);
    assertNull(impl.findApprovedPatientBySsn("SSN1"));
    FoundPatient qp = impl.findApprovedPatientBySsn("SSN2");
    assertNotNull(qp);
    assertEquals(patient.getId(), qp.getId());
    assertEquals(patient.getName(), qp.getName());
    assertEquals(patient.getSsn(), qp.getSsn());
    assertEquals(patient.getDob(), qp.getDob());
    verify(ps);
  }
}
