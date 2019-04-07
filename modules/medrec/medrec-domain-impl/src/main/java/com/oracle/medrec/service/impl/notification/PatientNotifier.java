package com.oracle.medrec.service.impl.notification;

import com.oracle.medrec.model.Patient;

/**
 * @author Copyright (c) 2007,2013, Oracle and/or its affiliates. All rights
 *         reserved.
 */
public interface PatientNotifier {

  void notifyPatient(Patient patient);
}
