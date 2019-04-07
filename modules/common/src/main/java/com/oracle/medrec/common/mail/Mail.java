package com.oracle.medrec.common.mail;

/**
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public interface Mail {

  String getSubject();

  void setSubject(String subject);

  String getContent();

  void setContent(String content);

  String getTo();

  void setTo(String to);

  String getFrom();

  void setFrom(String from);
}
