package com.oracle.medrec.service.impl.notification;

import com.oracle.medrec.common.messaging.JmsClient;
import com.oracle.medrec.model.Patient;
import com.oracle.medrec.model.PersonName;
import junit.framework.JUnit4TestAdapter;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.junit.Test;

import javax.jms.JMSException;
import javax.jms.Queue;

/**
 * {@link PatientNotifierDelegate} test case.
 *
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 * @since Jul 17, 2007
 */
public class PatientNotifierDelegateTestCase {

  public static junit.framework.Test suite() {
    return new JUnit4TestAdapter(PatientNotifierDelegateTestCase.class);
  }

  @Test
  public void notifyPatient() {
    JmsClient mg = createMock(JmsClient.class);
    PatientNotifierDelegate delegate = new PatientNotifierDelegate();
    delegate.setMessageGateway(mg);

    Patient patient = new Patient();
    patient.setEmail("foo@foo.com");
    patient.setName(new PersonName());
    mg.send(null, new PatientToNotify(patient));
    expectLastCall().andReturn(null);

    replay(mg);
    delegate.notifyPatient(patient);
    verify(mg);
  }
}
