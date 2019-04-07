package com.oracle.medrec.common.messaging;

import javax.jms.Destination;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public class OutgoingMessageImpl implements OutgoingMessage {

  private Object payload;

  private byte[] jmsCorrelationIdAsBytes;

  private String jmsCorrelationId;

  private Destination jmsReplyTo;

  private int jmsDeliveryMode;

  private Map<String, Object> properties = new HashMap<String, Object>();

  public Object getPayload() {
    return payload;
  }

  public void setPayload(Object payload) {
    this.payload = payload;
  }

  public void clearPayload() {
    payload = null;
  }

  public byte[] getJmsCorrelationIdAsBytes() {
    return jmsCorrelationIdAsBytes;
  }

  public void setJmsCorrelationIdAsBytes(byte[] jmsCorrelationIdAsBytes) {
    this.jmsCorrelationIdAsBytes = jmsCorrelationIdAsBytes;
  }

  public String getJmsCorrelationId() {
    return jmsCorrelationId;
  }

  public void setJmsCorrelationId(String jmsCorrelationId) {
    this.jmsCorrelationId = jmsCorrelationId;
  }

  public Destination getJmsReplyTo() {
    return jmsReplyTo;
  }

  public void setJmsReplyTo(Destination jmsReplyTo) {
    this.jmsReplyTo = jmsReplyTo;
  }

  public int getJmsDeliveryMode() {
    return jmsDeliveryMode;
  }

  public void setJmsDeliveryMode(int jmsDeliveryMode) {
    this.jmsDeliveryMode = jmsDeliveryMode;
  }

  public void clearProperties() {
    properties.clear();
  }

  public Set<String> getPropertyNames() {
    return properties.keySet();
  }

  public boolean propertyExists(String propertyName) {
    return properties.containsKey(propertyName);
  }

  public Object getProperty(String propertyName) {
    return properties.get(propertyName);
  }

  public void setProperty(String propertyName, Object propertyValue) {
    properties.put(propertyName, propertyValue);
  }
}
