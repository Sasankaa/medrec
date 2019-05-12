package com.oracle.medrec.facade.impl;

import com.oracle.medrec.common.core.MethodParameterValidated;
import com.oracle.medrec.common.core.ThrowableLogged;
import com.oracle.medrec.facade.RecordFacade;
import com.oracle.medrec.facade.model.RecordDetail;
import com.oracle.medrec.facade.model.RecordSummary;
import com.oracle.medrec.facade.model.RecordToCreate;
import com.oracle.medrec.model.Record;
import com.oracle.medrec.model.Xray;
import com.oracle.medrec.service.RecordService;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@MethodParameterValidated
@ThrowableLogged
public class RecordFacadeImpl implements RecordFacade {
	
	private static final Logger LOGGER = Logger.getLogger(RecordFacadeImpl.class.getName());

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
    RecordDetail recordDetail = new RecordDetail(record);
  	LOGGER.info("Get xrays for record id:" + id);	    
    recordDetail.setXrays(getXrays(id));
    return recordDetail;
  }
  
  private List<Xray> getXrays (Long recordId) {
		List<Xray> xrays = new ArrayList<Xray>();
		Xray xray = new Xray();
		xray.setId(new Long(1));
		xray.setPatientId(new Long(2));
		xray.setDateCreated(new Date());
		xray.setXrayArea("chest");
		xrays.add(xray);
		xrays.add(xray);
  	return xrays;
  }
}
