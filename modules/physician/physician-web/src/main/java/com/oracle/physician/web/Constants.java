package com.oracle.physician.web;

import com.oracle.medrec.common.util.ServerPropertiesUtils;

/**
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public abstract class Constants {

  // ===================== session param key ============================

  public final static String AUTHENTICATED_USER_SESSION_KEY = "user";

  public final static String PATIENT_SESSION_KEY = "patient";

  public final static String PATIENTS_SESSION_KEY = "patients";

  // ======================= path ========================================

  public final static String PHYSICIAN_BASE_PATH = "/physician/";

  // ===================== navigation ====================================

  public final static String LOGIN_PATH = "/login.xhtml";

  public final static String LOGIN_PATH_RDT = "http://" + ServerPropertiesUtils.getRegion() + "medrec/index.xhtml";

  public final static String PHYSICIAN_HOME_RDT = "physicianHome?faces-redirect=true";

  public final static String VIEW_RECORD_DETAIL = "viewRecordDetail";

  public final static String VIEW_RECORD_CREATION_RESULT = "viewRecordCreationResult";

}
