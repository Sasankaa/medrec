package com.oracle.medrec.common.mail;

import java.io.Serializable;

/**
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public class MailImpl implements Serializable, Mail {

  private static final long serialVersionUID = -1037771141232265993L;

  private String subject;

  private String content;

  private String to;

  private String from;

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getTo() {
    return to;
  }

  public void setTo(String to) {
    this.to = to;
  }

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    MailImpl mail = (MailImpl) o;

    if (content != null ? !content.equals(mail.content) : mail.content != null) {
      return false;
    }
    if (from != null ? !from.equals(mail.from) : mail.from != null) {
      return false;
    }
    if (subject != null ? !subject.equals(mail.subject) : mail.subject != null) {
      return false;
    }
    if (to != null ? !to.equals(mail.to) : mail.to != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = subject != null ? subject.hashCode() : 0;
    result = 31 * result + (content != null ? content.hashCode() : 0);
    result = 31 * result + (to != null ? to.hashCode() : 0);
    result = 31 * result + (from != null ? from.hashCode() : 0);
    return result;
  }
}
