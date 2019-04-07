package com.oracle.medrec.service.batch;

import java.io.File;

/**
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.<br>
 *         Created by xiaojwu on 14-2-17.
 */
public abstract class BatchConstants {

  public final static String FINANCE_REPORT = "financeReportJob";

  public final static String MONTHLY_DIR = "monthly" + File.separator;

  public final static String PARAM_START_DATE = "startDate";

  public final static String PARAM_END_DATE = "endDate";

  public final static String PARAM_ADMIN_NAME = "adminName";

  public final static String PARAM_DIR = "dir";

  public final static String PARAM_SEQ_ID = "id";

  public final static String PARAM_FILE_PATH = "filePath";

  public final static String DATE_FORMAT = "yyyy.MM.dd";

  public final static String SUFFIX = ".txt";
}
