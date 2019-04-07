package com.oracle.medrec.web.controller;

import com.oracle.medrec.model.Record;
import com.oracle.medrec.service.RecordService;
import com.oracle.medrec.web.Constants;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import java.util.logging.Logger;

/**
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@Model
public class ViewingRecordDetailController extends BaseMedRecPageController {

  private static final Logger LOGGER = Logger.getLogger(ViewingRecordDetailController.class.getName());

  @Inject
  private RecordService recordService;

  private String recordId;

  private Record record;

  public void setRecordId(String recordId) {
    LOGGER.finest("setRecordId() called");
    this.recordId = recordId;
  }

  public Record getRecord() {
    LOGGER.finest("getRecord() called: " + record);
    return record;
  }

  public String viewRecordDetail() {
    recordId = this.getPageContext().getRequestParam("recordId");
    LOGGER.fine("Record ID: " + recordId);
    record = recordService.getRecord(Long.parseLong(recordId));
    LOGGER.finer("Got record detail");
    LOGGER.finer("Symptoms: " + record.getSymptoms());
    LOGGER.finer("Temperature: " + record.getVitalSigns().getTemperature());
    LOGGER.finer("Number of prescriptions: " + record.getPrescriptions().size());
    if (record.getPrescriptions().size() > 0) {
      LOGGER.finer("Drug of the 1st prescription: " + record.getPrescriptions().get(0).getDrug());
    }
    return Constants.VIEW_RECORD_DETAIL;
  }

}
