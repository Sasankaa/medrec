package com.oracle.medrec.facade.impl;

import com.oracle.medrec.common.core.MethodParameterValidated;
import com.oracle.medrec.common.core.ThrowableLogged;
import com.oracle.medrec.facade.RecordFacade;
import com.oracle.medrec.facade.model.RecordDetail;
import com.oracle.medrec.facade.model.RecordSummary;
import com.oracle.medrec.facade.model.RecordToCreate;
import com.oracle.medrec.model.Record;
import com.oracle.medrec.service.RecordService;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.util.List;

/**
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@MethodParameterValidated
@ThrowableLogged
public class RecordFacadeImpl implements RecordFacade {

  @Inject
  private RecordService recordService;

  public void setRecordService(RecordService recordService) {
    this.recordService = recordService;
  }

  public void createRecord(RecordToCreate recordToCreate) {
    recordService.createRecord(recordToCreate.toRecord(), recordToCreate.getPhysicianId(),
        recordToCreate.getPatientId());
  }

  public RecordSummary getRecordSummaryByPatientId(Long patientId) {
    List<Record> records = recordService.getRecordsByPatientId(patientId);
    return new RecordSummary(patientId, records);
  }

  public RecordDetail getRecordDetail(Long id) {
    Record record = recordService.getRecord(id);
    if (record == null) {
      throw new AssertionError("Invalid record id: " + id);
    }
    return new RecordDetail(record);
  }
}
