package com.oracle.medrec.common.mail;

import com.oracle.medrec.common.core.MethodParameterValidated;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Customers using MedRec don't have to use real mail systems. So by default, it is a dummy impl.
 * If you want to send actual mail message, you have to configure mail session on weblogic first.
 *
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@MethodParameterValidated
public class MailClientImpl implements MailClient {

  private static final Logger LOGGER = Logger.getLogger(MailClientImpl.class.getName());

  @Resource(name = "mail/MedRecMailSession")
  private Session session;

  /**
   * This env-entry define if mail server is available or not. The default value is false.
   * If you want to experience a real one,
   * you need update the property in application.xml or use Development Plan function of Weblogic.
   */
  @Resource(name = "java:app/com.oracle.medrec.IsEmailEnabled")
  private boolean isEmailEnabled;

  public void setSession(Session session) {
    this.session = session;
  }

  public void sendMail(Mail mail) {
    // Real mail will be sent if only isEmailEnabled == true.
    if (isEmailEnabled) {
      try {
        Message message = new MimeMessage(session);
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mail.getTo()));
        message.setSubject(mail.getSubject());
        message.setSentDate(new Date());
        message.setContent(mail.getContent(), "text/plain");
        Transport.send(message);
      } catch (Exception e) {
        throw new MailException("Cannot send mail", e);
      }
    }
    LOGGER.info("The mail sent successfully");
  }

  public Mail createMail() {
    return new MailImpl();
  }
}
