package com.oracle.medrec.common.messaging;

import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;

/**
 * A util composing and extracting JMS message.
 *
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public interface MessageComposer {

  Message composeMessage(JMSContext context, Object payload) throws JMSException;

  Object extractPayload(Message message) throws JMSException;
}
