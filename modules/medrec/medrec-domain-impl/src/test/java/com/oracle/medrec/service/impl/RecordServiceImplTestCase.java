package com.oracle.medrec.service.impl;

import com.oracle.medrec.common.testing.EntityRepositoryTestCaseSupport;
import com.oracle.medrec.model.Drug;
import com.oracle.medrec.model.Patient;
import com.oracle.medrec.model.Physician;
import com.oracle.medrec.model.Record;
import junit.framework.JUnit4TestAdapter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * {@link RecordServiceImpl} test case.
 *
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 * @since Jul 17, 2007
 */
public class RecordServiceImplTestCase extends EntityRepositoryTestCaseSupport<RecordServiceImpl> {

  private Record record;
  private Patient patient;
  private Physician physician;
  private Drug drug;

  public static junit.framework.Test suite() {
    return new JUnit4TestAdapter(RecordServiceImplTestCase.class);
  }

  @Before
  public void before() {
    // patient
    patient = EntitiesPreparation.createPatient();
    service.getEntityManager().persist(patient);

    // physician
    physician = EntitiesPreparation.createPhysician();
    service.getEntityManager().persist(physician);

    // drug
    drug = EntitiesPreparation.createDrug();
    service.getEntityManager().persist(drug);

    // record
    record = EntitiesPreparation.createRecord(patient, physician, drug);
    service.getEntityManager().persist(record);
  }

  @After
  public void after() {
    service.getEntityManager().remove(record);
    service.getEntityManager().remove(drug);
    service.getEntityManager().remove(patient);
    service.getEntityManager().remove(physician);
  }

  @Test
  public void testGetRecord() {
    Record rd = service.getRecord(record.getId());
    assertEquals(rd.getId(), record.getId());
  }

  @Test
  public void testGetRecordsByPatientId() {
    List<Record> rd = service.getRecordsByPatientId(patient.getId());
    assertEquals(rd.get(0).getId(), record.getId());
  }

  @Test
  public void testCreateRecord() {
    entityTransactionCommit();
    startTransaction();
    Record rd = EntitiesPreparation.createRecord(patient, physician, drug);
    service.createRecord(rd, physician.getId(), patient.getId());
    service.getEntityManager().remove(rd);
    setRollbackOnly(false);
  }

  @Test
  public void testGetRecordsByDateInterval() {
    Date startDate = Date.valueOf("2006-6-1");
    List<Record> records = service.getRecordsByDateInterval(startDate, record.getDate());
    assertEquals(1, records.size());
    Date endDate = Date.valueOf("2007-6-1");
    records = service.getRecordsByDateInterval(startDate, endDate);
    assertEquals(0, records.size());
  }

}
