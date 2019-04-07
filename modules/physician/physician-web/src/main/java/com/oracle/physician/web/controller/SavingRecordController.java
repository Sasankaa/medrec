package com.oracle.physician.web.controller;

import com.oracle.physician.service.RecordService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * CreatingRecordController is a JSF ManagedBean that is responsible for
 * creating diagnostic record of a patient by physician.
 *
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@Named
@RequestScoped
public class SavingRecordController {

  @Inject
  private RecordService recordService;

  @Inject
  private CreatingRecordController creatingRecordController;

  public String saveRecord() {
    return creatingRecordController.saveRecord(recordService);
  }
}
