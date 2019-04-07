package com.oracle.medrec.common.messaging;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;
import java.io.Serializable;

/**
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class MessageComposerImpl implements MessageComposer {

  public Object extractPayload(Message message) throws JMSException {
    if (message instanceof ObjectMessage) {
      return ((ObjectMessage) message).getObject();
    } else if (message instanceof TextMessage) {
      return ((TextMessage) message).getText();
    }
    throw new IllegalArgumentException("Unsupported message type: " + message.getClass().getName());
  }

  public Message composeMessage(JMSContext context, Object payload) throws JMSException {

    if (payload instanceof String) {
      return context.createTextMessage(payload.toString());
    } else {
      if (payload instanceof Serializable) {
        return context.createObjectMessage((Serializable) payload);
      }
      throw new IllegalArgumentException("The instance to be wrapped in ObjectMessage isn't serializable: " + payload
          .getClass());
    }
  }
}
