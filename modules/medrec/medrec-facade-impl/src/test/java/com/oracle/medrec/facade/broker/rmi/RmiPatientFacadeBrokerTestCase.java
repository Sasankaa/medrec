package com.oracle.medrec.facade.broker.rmi;

import com.oracle.medrec.facade.PatientFacade;
import com.oracle.medrec.facade.model.FoundPatient;
import com.oracle.medrec.model.Patient;
import junit.framework.JUnit4TestAdapter;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * {@link RmiPatientFacadeBroker} test case.
 *
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 * @since Jul 17, 2007
 */
public class RmiPatientFacadeBrokerTestCase {

  private PatientFacade patientFacade;
  private RmiPatientFacadeBroker broker;

  public static junit.framework.Test suite() {
    return new JUnit4TestAdapter(RmiPatientFacadeBrokerTestCase.class);
  }

  @Before
  public void setUp() {
    patientFacade = createMock(PatientFacade.class);
    broker = new RmiPatientFacadeBroker();
    broker.setPatientFacade(patientFacade);
  }

  @Test
  public void testGetPatient() {
    Patient patient = new Patient();
    patientFacade.getPatient(1L);
    expectLastCall().andReturn(patient);
    replay(patientFacade);
    assertSame(patient, broker.getPatient(1L));
    verify(patientFacade);
  }

  @Test
  public void testQueryPatientBySsn() {
    FoundPatient qp = new FoundPatient(new Patient());
    patientFacade.findApprovedPatientBySsn("SSN");
    expectLastCall().andReturn(qp);
    replay(patientFacade);
    assertSame(qp, broker.findApprovedPatientBySsn("SSN"));
    verify(patientFacade);
  }

  @Test
  public void testQueryPatientsByLastName() {
    List<FoundPatient> qps = Arrays.asList(new FoundPatient(new Patient()), new FoundPatient(new Patient()));
    patientFacade.findApprovedPatientsByLastName("last");
    expectLastCall().andReturn(qps);
    replay(patientFacade);
    assertSame(qps, broker.findApprovedPatientsByLastName("last"));
    verify(patientFacade);
  }
}
