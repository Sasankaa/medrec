package com.oracle.medrec.service.impl;

import com.oracle.medrec.common.testing.EntityRepositoryTestCaseSupport;
import com.oracle.medrec.model.Patient;
import com.oracle.medrec.service.DuplicateSsnException;
import com.oracle.medrec.service.DuplicateUsernameException;
import junit.framework.JUnit4TestAdapter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * {@link PatientServiceImpl} test case.
 *
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 * @since Jul 17, 2007
 */
public class PatientServiceImplTestCase extends EntityRepositoryTestCaseSupport<PatientServiceImpl> {

  private Patient patient;

  public static junit.framework.Test suite() {
    return new JUnit4TestAdapter(PatientServiceImplTestCase.class);
  }

  @Before
  public void before() {
    patient = EntitiesPreparation.createPatient();
    service.getEntityManager().persist(patient);
    service.initBuilder();
  }

  @After
  public void after() {
    service.getEntityManager().remove(patient);
  }

  @Test(expected = DuplicateSsnException.class)
  public void testCreatePatientWithDuplicatedSsn() throws Exception {
    Patient user = EntitiesPreparation.createPatient("b@oracle.com", "SSN000000");
    service.createPatient(user);
  }

  @Test(expected = DuplicateUsernameException.class)
  public void testCreatePatientWithDuplicatedUsername() throws Exception {
    Patient user = EntitiesPreparation.createPatient("a@oracle.com", "SSN000001");
    service.createPatient(user);
  }

  @Test
  public void testCreatePatient() throws Exception {
    Patient user = EntitiesPreparation.createPatient("b@oracle.com", "SSN000001");
    service.createPatient(user);
    service.getEntityManager().remove(user);
  }

  @Test
  public void testGetPatient() {
    entityTransactionCommit();
    startTransaction();
    Patient user = service.getPatient(patient.getId());
    assertEquals(patient.getUsername(), user.getUsername());
    setRollbackOnly(false);
  }

  @Test
  public void testFindApprovedPatientBySsn() {
    Patient user = service.findApprovedPatientBySsn(patient.getSsn());
    assertEquals(patient.getUsername(), user.getUsername());
  }

  @Test
  public void testFindApprovedPatientsByLastName() {
    List<Patient> user = service.findApprovedPatientsByLastName(patient.getName().getLastName());
    assertEquals(patient.getUsername(), user.get(0).getUsername());
  }

  @Test
  public void testAuthenticatePatient() {
    boolean result = service.authenticatePatient(patient.getUsername(), patient.getPassword());
    assertTrue(result);

    result = service.authenticatePatient("bogus", patient.getPassword());
    assertFalse(result);

    result = service.authenticatePatient(patient.getUsername(), "bogus");
    assertFalse(result);
  }

  @Test
  public void testAuthenticateAndReturnPatient() {
    Patient user = service.authenticateAndReturnPatient(patient.getUsername(), patient.getPassword());
    assertNotNull(user);

    user = service.authenticateAndReturnPatient("bogus", patient.getPassword());
    assertNull(user);

    user = service.authenticateAndReturnPatient(patient.getUsername(), "bogus");
    assertNull(user);
  }

  // @Test
  // public void testApprovePatient() {
  // patient.setStatus(Patient.Status.REGISTERED);
  // service.update(patient);
  // service.approvePatient(patient.getId());
  // assertEquals(Patient.Status.APPROVED, patient.getStatus());
  // }
  //
  // @Test
  // public void testDenyPatient() {
  // patient.setStatus(Patient.Status.REGISTERED);
  // service.update(patient);
  // service.denyPatient(patient.getId());
  // assertEquals(Patient.Status.DENIED, patient.getStatus());
  // }

  @Test
  public void testGetNewlyRegisteredPatients() {
    patient.setStatus(Patient.Status.REGISTERED);
    service.getEntityManager().merge(patient);
    List<Patient> user = service.getNewlyRegisteredPatients();
    assertEquals(patient.getId(), user.get(0).getId());
  }

  @Test(expected = DuplicateSsnException.class)
  public void testUpdatePatientDuplicateSSN() throws DuplicateSsnException {
    patient.setPhone("1234567");
    patient.setSsnChanged(true);
    service.updatePatient(patient);
  }

  @Test
  public void testUpdatePatientSSNNoChanged() throws DuplicateSsnException {
    patient.setPhone("1234567");
    Patient user = service.updatePatient(patient);
    assertEquals("1234567", user.getPhone());
  }

  @Test
  public void testFuzzyFindApprovedPatientsByLastNameAndSsn() {
    List<Patient> user = service.fuzzyFindApprovedPatientsByLastNameAndSsn("la", "");
    assertEquals(patient.getId(), user.get(0).getId());

    user = service.fuzzyFindApprovedPatientsByLastNameAndSsn("", "SS");
    assertEquals(patient.getId(), user.get(0).getId());

    user = service.fuzzyFindApprovedPatientsByLastNameAndSsn("la", "S");
    assertEquals(patient.getId(), user.get(0).getId());

    user = service.fuzzyFindApprovedPatientsByLastNameAndSsn("ee", "we");
    assertEquals(0, user.size());
  }

  //    private Patient createPatient(String email, String ssn) {
  //        Patient user = new Patient();
  //        user.setEmail(email);
  //        user.setPassword("password");
  //        user.setGender(Patient.Gender.FEMALE);
  //        user.setDob(new Date());
  //        user.setSsn(ssn);
  //        user.setPhone("11111111");
  //        user.approve();
  //
  //        Address address = new Address();
  //        address.setCity("city");
  //        address.setCountry("country");
  //        address.setState("state");
  //        address.setStreet1("street1");
  //        address.setStreet2("street2");
  //        address.setZip("11111");
  //        user.setAddress(address);
  //
  //        PersonName name = new PersonName();
  //        name.setFirstName("firstname");
  //        name.setMiddleName("middlename");
  //        name.setLastName("lastname");
  //        user.setName(name);
  //        return user;
  //    }
}
