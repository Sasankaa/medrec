package com.oracle.medrec.common.messaging;

import javax.jms.Destination;

/**
 * JMS-specific message client.
 *
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public interface JmsClient {

  // Methods for the default configurations of message delivery
  // ------------------------------------

  boolean isDefaultPersistent();

  void setDefaultPersistent(boolean persistent);

  int getDefaultPriority();

  void setDefaultPriority(int priority);

  long getDefaultTimeToLive();

  void setDefaultTimeToLive(long timeToLive);

  // Methods to produce message with different MEPs
  // ------------------------------------------------

  String send(Destination destination, Object payload);

  String send(Destination destination, Object payload, int deliveryMode, int priority, long timeToLive);

  String send(Destination destination, OutgoingMessage outgoingMessage);

  String send(Destination destination, OutgoingMessage outgoingMessage, int deliveryMode, int priority,
              long timeToLive);

  /**
   * Factory method to create an outgoing message with the default
   * implementation.
   */
  OutgoingMessage createOutgoingMessage(Object payload);

}
