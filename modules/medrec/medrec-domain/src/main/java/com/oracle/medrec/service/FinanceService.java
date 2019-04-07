package com.oracle.medrec.service;

import javax.ejb.Local;

/**
 * Cost statistic business service interface.
 *
 * @author Xiaojun Wu. <br>
 *         Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@Local
public interface FinanceService {

  /**
   * According to the start date and end date, reports sale of drugs.
   *
   * @param startDate yyyy-MM-dd
   * @param endDate   yyyy-MM-dd
   * @param adminName admin adminName
   * @return job sequence id
   */
  String createFinanceReport(String startDate, String endDate, String adminName);

  /**
   * According to month, reports sale of drugs.
   */
  void createFinanceLastMonthReport();

}
