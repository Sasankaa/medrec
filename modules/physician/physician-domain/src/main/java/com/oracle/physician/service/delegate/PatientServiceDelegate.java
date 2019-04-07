package com.oracle.physician.service.delegate;

import com.oracle.medrec.common.core.MethodInvocationCached;
import com.oracle.medrec.facade.JaxWsPatientFacade;
import com.oracle.medrec.facade.model.FoundPatient;
import com.oracle.medrec.model.Patient;
import com.oracle.physician.JaxWsProperties;
import com.oracle.physician.service.PatientService;

import javax.annotation.PostConstruct;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public class PatientServiceDelegate implements PatientService {

  private JaxWsPatientFacade patientFacade;

  private String patientFacadeServiceName = "PatientFacadeService";

  private String patientFacadeServiceNamespaceUri = JaxWsProperties.NAMESPACEURL;

  private String patientFacadeServiceWsdlLocation = JaxWsProperties.WSURL + "PatientFacadeService?WSDL";

  @PostConstruct
  public void init() {
    if (patientFacade == null) {
      URL url = null;
      try {
        url = new URL(patientFacadeServiceWsdlLocation);
      } catch (MalformedURLException e) {
        e.printStackTrace();
      }
      QName qname = new QName(patientFacadeServiceNamespaceUri, patientFacadeServiceName);
      Service service = Service.create(url, qname);
      patientFacade = service.getPort(JaxWsPatientFacade.class);
    }
  }

  public Patient getPatient(Long patientId) {
    return patientFacade.getPatient(patientId);
  }

  public FoundPatient findApprovedPatientBySsn(String ssn) {
    FoundPatient patient = patientFacade.findApprovedPatientBySsn(ssn);
    return patient;
  }

  public List<FoundPatient> findApprovedPatientsByLastName(String lastName) {
    return patientFacade.findApprovedPatientsByLastName(lastName);
  }

  public List<FoundPatient> fuzzyFindApprovedPatientsByLastNameAndSsn(String lastName, String ssn) {
    return patientFacade.fuzzyFindApprovedPatientsByLastNameAndSsn(lastName, ssn);
  }

}
