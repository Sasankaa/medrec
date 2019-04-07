package com.oracle.physician.web.controller;

import com.oracle.medrec.facade.model.RecordDetail;
import com.oracle.physician.service.RecordService;
import com.oracle.physician.web.Constants;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import java.util.logging.Logger;

/**
 * ViewingRecordDetailController is a JSF ManagedBean that is responsible for
 * showing details of record.
 *
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@Model
public class ViewingRecordDetailController extends BasePhysicianPageController {

  private static final Logger LOGGER = Logger.getLogger(ViewingRecordDetailController.class.getName());

  private String recordId;

  private RecordDetail recordDetail;

  @Inject
  private RecordService recordService;

  public void setRecordId(String recordId) {
    LOGGER.finest("Set record id: " + recordId);
    this.recordId = recordId;
  }

  public RecordDetail getRecordDetail() {
    return recordDetail;
  }

  public String viewRecordDetail() {
    setRecordId(getPageContext().getRequestParam("recordId"));

    LOGGER.finer("Record ID: " + recordId);

    // find record detail by id
    recordDetail = recordService.getRecordDetail(Long.parseLong(recordId));

    LOGGER.finer("Got record detail");
    LOGGER.finer("Symptoms: " + recordDetail.getSymptoms());
    LOGGER.finer("Temperature: " + recordDetail.getVitalSigns().getTemperature());
    LOGGER.finer("Number of prescriptions: " + recordDetail.getPrescriptions().size());
    if (recordDetail.getPrescriptions().size() > 0) {
      LOGGER.finer("Drug of the 1st prescription: " + recordDetail.getPrescriptions().get(0).getDrug());
    }
    return Constants.VIEW_RECORD_DETAIL;
  }
}
