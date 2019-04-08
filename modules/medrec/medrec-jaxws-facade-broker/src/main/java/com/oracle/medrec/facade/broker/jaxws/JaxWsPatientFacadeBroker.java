package com.oracle.medrec.facade.broker.jaxws;

import com.oracle.medrec.facade.JaxWsPatientFacade;
import com.oracle.medrec.facade.PatientFacade;
import com.oracle.medrec.facade.model.FoundPatient;
import com.oracle.medrec.model.Patient;

import javax.inject.Inject;
import javax.jws.WebService;
import java.util.List;

/**
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@WebService(name = "PatientFacade", endpointInterface = "com.oracle.medrec.facade.JaxWsPatientFacade",
    portName = "PatientFacadePort", serviceName = "PatientFacadeService",
    targetNamespace = "http://www.oracle.com/medrec/service/jaxws")
public class JaxWsPatientFacadeBroker implements JaxWsPatientFacade {

  @Inject
  private PatientFacade patientFacade;

  public Patient getPatient(Long patientId) {
	  System.out.println("SYSTEMOUT:" +  JaxWsPatientFacadeBroker.class.getName() + " - getPatient");
    return patientFacade.getPatient(patientId);
  }

  public FoundPatient findApprovedPatientBySsn(String ssn) {
	  System.out.println("SYSTEMOUT:" +  JaxWsPatientFacadeBroker.class.getName() + " - findApprovedPatientBySsn");
    return patientFacade.findApprovedPatientBySsn(ssn);
  }

  public List<FoundPatient> findApprovedPatientsByLastName(String lastName) {
	  System.out.println("SYSTEMOUT:" +  JaxWsPatientFacadeBroker.class.getName() + " - findApprovedPatientsByLastName");
    return patientFacade.findApprovedPatientsByLastName(lastName);
  }

  public List<FoundPatient> fuzzyFindApprovedPatientsByLastNameAndSsn(String lastName, String ssn) {
	  System.out.println("SYSTEMOUT:" +  JaxWsPatientFacadeBroker.class.getName() + " - fuzzyFindApprovedPatientsByLastNameAndSsn");
    return patientFacade.fuzzyFindApprovedPatientsByLastNameAndSsn(lastName, ssn);
  }

}
