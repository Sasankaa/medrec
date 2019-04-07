package com.oracle.medrec.service.impl;

import com.oracle.medrec.service.FinanceService;
import com.oracle.medrec.service.batch.BatchConstants;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

/**
 * A Singleton Session Bean to handle finance statistic.
 *
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.<br>
 *         Created by xiaojwu on 14-2-13.
 */
@Singleton
@Startup
public class FinanceServiceImpl implements FinanceService {

  private static final Logger LOGGER = Logger.getLogger(FinanceServiceImpl.class.getName());

  private AtomicLong nextSeqId = new AtomicLong(0);

  @Override
  public String createFinanceReport(String startDate, String endDate, String adminName) {
    LOGGER.info("Preparing batch job of finance, startDate=" + startDate + ", endDate=" + endDate +", " +
        "admin username=" + adminName);
    // init job operator
    JobOperator jobOperator = BatchRuntime.getJobOperator();
    // prepare job properties
    String seq = "job" + String.valueOf(nextSeqId.getAndIncrement());
    Properties props = new Properties();
    props.setProperty(BatchConstants.PARAM_START_DATE, startDate);
    props.setProperty(BatchConstants.PARAM_END_DATE, endDate);
    props.setProperty(BatchConstants.PARAM_SEQ_ID, seq);
    props.setProperty(BatchConstants.PARAM_ADMIN_NAME, adminName);
    String dir = startDate + "-" + endDate;
    props.setProperty(BatchConstants.PARAM_DIR, adminName + File.separator + dir + File.separator);
    // start job async
    jobOperator.start(BatchConstants.FINANCE_REPORT, props);
    LOGGER.info("Having started batch job of finance, startDate=" + startDate + ", endDate=" + endDate +", " +
        "admin username=" + adminName);
    return seq;
  }

  @Override
  @Schedule(dayOfMonth = "1", hour = "1")   // a monthly batch job on 1st day of every month.
  public void createFinanceLastMonthReport() {
    LOGGER.info("Preparing monthly batch job of finance");
    Calendar calendar = Calendar.getInstance();
    if (calendar.get(Calendar.MONTH) == Calendar.JANUARY) {
      calendar.set(Calendar.MONTH, calendar.getMaximum(Calendar.MONTH));
      calendar.add(Calendar.YEAR, -1);
    } else {
      calendar.add(Calendar.MONTH, -1);
    }
    calendar.set(Calendar.DAY_OF_MONTH, calendar.getMinimum(Calendar.DAY_OF_MONTH));
    Date date1 = calendar.getTime();
    calendar.set(Calendar.DAY_OF_MONTH, calendar.getMaximum(Calendar.DAY_OF_MONTH));
    Date date2 = calendar.getTime();
    SimpleDateFormat format = new SimpleDateFormat(BatchConstants.DATE_FORMAT);
    createFinanceReport(format.format(date1), format.format(date2), BatchConstants.MONTHLY_DIR);
  }

}
