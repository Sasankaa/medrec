package com.oracle.medrec.facade;

import com.oracle.medrec.facade.model.FoundPatient;
import com.oracle.medrec.model.Patient;

import javax.ejb.Local;
import java.util.List;

/**
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@Local
public interface PatientFacade {

  Patient getPatient(Long patientId);

  FoundPatient findApprovedPatientBySsn(String ssn);

  List<FoundPatient> findApprovedPatientsByLastName(String lastName);

  List<FoundPatient> fuzzyFindApprovedPatientsByLastNameAndSsn(String lastName, String ssn);
}
