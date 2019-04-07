package com.oracle.medrec.service.impl.notification;

import com.oracle.medrec.common.messaging.OneWayPayloadHandlerSupport;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Now we won't start transaction here.
 *
 * @author Copyright (c) 2007,2013, Oracle and/or its affiliates. All rights
 *         reserved.
 */
@MessageDriven(activationConfig = {@ActivationConfigProperty(propertyName = "destinationLookup",
    propertyValue = "com.oracle.medrec.jms.PatientNotificationQueue"), @ActivationConfigProperty(propertyName =
    "destinationType",
    propertyValue = "javax.jms.Queue")})
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class PatientNotifierBroker extends OneWayPayloadHandlerSupport<PatientToNotify> {

  private static final Logger LOGGER = Logger.getLogger(PatientNotifierBroker.class.getName());

  @EJB(beanName = "PatientNotifierImpl")
  private PatientNotifier patientNotifierImpl;

  public void handlePayload(PatientToNotify patientToNotify) {
    LOGGER.info("Received JMS message of patient notification");
    try {
      patientNotifierImpl.notifyPatient(patientToNotify.toPatient());
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Notify patient " + patientToNotify.getEmail() + " failed.", e);
    }
  }
}
