package com.oracle.medrec.common.messaging;

import javax.jms.Destination;
import java.util.Set;

/**
 * Standard JMS message must be created by session, but with the current
 * architecture, we won't have a session in the invocation context before
 * sending message, so we just create this value class. This class converts the
 * checked JMSException to unchecked exception and updates some old API (e.g.
 * legacy collection class). Moreover, with it's subclass hierachy, for now we
 * explicitly distinguish incoming and outgoing message to eliminate some
 * unnecessary methods.
 * <p/>
 * <p/>
 * We won't do input validation here, and instead, validation will take palce
 * when sending message.
 *
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public interface JmsMessage {

  Object getPayload();

  byte[] getJmsCorrelationIdAsBytes();

  void setJmsCorrelationIdAsBytes(byte[] jmsCorrelationIdAsBytes);

  String getJmsCorrelationId();

  void setJmsCorrelationId(String jmsCorrelationId);

  Destination getJmsReplyTo();

  void setJmsReplyTo(Destination jmsReplyTo);

  void clearProperties();

  Set<String> getPropertyNames();

  boolean propertyExists(String propertyName);

  Object getProperty(String propertyName);

  /**
   * Only string and primitive types are allowed for propertyValue per JMS.
   */
  void setProperty(String propertyName, Object propertyValue);
}
