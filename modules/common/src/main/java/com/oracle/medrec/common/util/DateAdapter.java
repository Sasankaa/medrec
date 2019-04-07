package com.oracle.medrec.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @author Xiaojun Wu. <br>
 *         Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public class DateAdapter extends XmlAdapter<String, Date> {

  private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

  @Override
  public String marshal(Date d) throws Exception {
    return dateFormat.format(d);
  }

  @Override
  public Date unmarshal(String d) throws Exception {
    return dateFormat.parse(d);
  }

}
