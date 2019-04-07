package com.oracle.medrec.service.batch;

/**
 * A CDI Event data for batch use-case.
 *
 * @author : xiaojwu.
 *         Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public class BatchFinishedUpEvent {

  private String filename;

  private String startDate;

  private String endDate;

  private String seqId;

  private String adminName;

  private Type type;

  public BatchFinishedUpEvent(String filename, String startDate, String endDate, String seqId, String adminName,
                              Type type) {
    this.filename = filename;
    this.startDate = startDate;
    this.endDate = endDate;
    this.seqId = seqId;
    this.adminName = adminName;
    this.type = type;
  }

  public String getFilename() {
    return filename;
  }

  public String getStartDate() {
    return startDate;
  }

  public String getEndDate() {
    return endDate;
  }

  public String getSeqId() {
    return seqId;
  }

  public String getAdminName() {
    return adminName;
  }

  public Type getType() {
    return type;
  }

  public enum Type {
    RECORD, PHYSICIAN, DRUG
  }
}
