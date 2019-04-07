package com.oracle.medrec.web.controller;

import com.oracle.medrec.model.Patient;
import com.oracle.medrec.model.Prescription;
import com.oracle.medrec.model.Record;
import com.oracle.medrec.service.RecordService;
import com.oracle.medrec.web.Constants;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@Model
public class ViewingRecordSummaryController extends BaseMedRecPageController {

  private static final Logger LOGGER = Logger.getLogger(ViewingRecordSummaryController.class.getName());

  @Inject
  private RecordService recordService;

  @SuppressWarnings("unchecked")
  public List<Record> getRecords() {
    LOGGER.finest("getRecords() called");
    return (List<Record>) getPageContext().getSessionMap().get(Constants.RECORDS_SESSION_KEY);
  }

  @SuppressWarnings("unchecked")
  public List<Prescription> getPrescriptions() {
    LOGGER.finest("getPrescriptions() called");
    return (List<Prescription>) getPageContext().getSessionMap().get(Constants.PRESCRIPTIONS_SESSION_KEY);
  }

  public String viewRecordSummary() {
    Patient patient = (Patient) getPageContext().getSessionMap().get(Constants.AUTHENTICATED_USER_SESSION_KEY);
    List<Record> records = recordService.getRecordsByPatientId(patient.getId());
    List<Prescription> prescriptions = new LinkedList();
    for (Record record : records) {
      prescriptions.addAll(record.getPrescriptions());
    }
    LOGGER.finer("Number of prescriptions: " + prescriptions.size());
    LOGGER.finer("Number of records: " + records.size());
    getPageContext().getSessionMap().put(Constants.RECORDS_SESSION_KEY, records);
    getPageContext().getSessionMap().put(Constants.PRESCRIPTIONS_SESSION_KEY, prescriptions);
    return Constants.VIEW_RECORDS_SUM;
  }
}
