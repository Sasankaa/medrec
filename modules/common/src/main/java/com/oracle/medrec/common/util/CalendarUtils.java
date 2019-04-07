package com.oracle.medrec.common.util;

import com.oracle.medrec.common.core.SystemException;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public abstract class CalendarUtils {

  public static Date convertXMLGregorianCalendarToDate(XMLGregorianCalendar xmlGregorianCalendar) {
    return xmlGregorianCalendar.toGregorianCalendar().getTime();
  }

  public static XMLGregorianCalendar convertDateToXMLGregorianCalendar(Date date) {
    GregorianCalendar gregorianCalendar = new GregorianCalendar();
    gregorianCalendar.setTime(date);
    try {
      return DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
    } catch (DatatypeConfigurationException e) {
      throw new SystemException(e);
    }
  }

  public static Calendar convertDateToCalendar(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    return calendar;
  }
}
