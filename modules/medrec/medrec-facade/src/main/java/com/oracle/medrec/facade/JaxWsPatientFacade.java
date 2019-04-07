package com.oracle.medrec.facade;

import com.oracle.medrec.facade.model.FoundPatient;
import com.oracle.medrec.model.Patient;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

/**
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@WebService(portName = "PatientFacadePort", serviceName = "PatientFacadeService",
    targetNamespace = "http://www.oracle.com/medrec/service/jaxws")
public interface JaxWsPatientFacade {

  @WebMethod
  public Patient getPatient(Long patientId);

  @WebMethod
  public FoundPatient findApprovedPatientBySsn(String ssn);

  @WebMethod
  public List<FoundPatient> findApprovedPatientsByLastName(String lastName);

  @WebMethod
  public List<FoundPatient> fuzzyFindApprovedPatientsByLastNameAndSsn(String lastName, String ssn);

}
