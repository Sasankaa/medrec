package com.oracle.medrec.web;

/**
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public abstract class Constants {

  // ===================== session param key ============================

  public final static String AUTHENTICATED_USER_SESSION_KEY = "user";

  public final static String PATIENTS_SESSION_KEY = "patients";

  public final static String RECORDS_SESSION_KEY = "records";

  public final static String PRESCRIPTIONS_SESSION_KEY = "prescriptions";

  // ======================= path ========================================

  public final static String ADMIN_BASE_PATH = "/admin/";

  public final static String PATIENT_BASE_PATH = "/patient/";

  // ===================== navigation ====================================

  public final static String INDEX_PATH = "/index.xhtml";

  public final static String INDEX_PATH_RDT = "/index.xhtml?faces-redirect=true";

  public final static String ADMIN_HOME_RDT = "adminHome?faces-redirect=true";

  public final static String PATIENT_HOME_RDT = "patientHome?faces-redirect=true";

  public final static String VIEW_NEW_PATIENT = "viewNewlyRegisteredPatient";

  public final static String VIEW_APPROVAL_RESULT = "viewApprovalResult";

  public final static String REGISTER_PATIENT = "registerPatient";

  public final static String VIEW_NEW_PATIENTS = "viewNewlyRegisteredPatients";

  public final static String VIEW_PATIENT = "viewPatient";

  public final static String VIEW_RECORD_DETAIL = "viewRecordDetail";

  public final static String VIEW_RECORDS_SUM = "viewRecordSummary";

}
