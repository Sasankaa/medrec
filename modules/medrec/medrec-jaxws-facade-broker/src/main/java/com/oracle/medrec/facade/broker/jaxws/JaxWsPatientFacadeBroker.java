package com.oracle.medrec.facade.broker.jaxws;

import com.oracle.medrec.facade.JaxWsPatientFacade;
import com.oracle.medrec.facade.PatientFacade;
import com.oracle.medrec.facade.model.FoundPatient;
import com.oracle.medrec.model.Patient;

import javax.inject.Inject;
import javax.jws.WebService;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Copyright (c) 2007, 2017, Oracle and/or its affiliates. All rights
 *         reserved.
 */
@WebService(name = "PatientFacade", endpointInterface = "com.oracle.medrec.facade.JaxWsPatientFacade", portName = "PatientFacadePort", serviceName = "PatientFacadeService", targetNamespace = "http://www.oracle.com/medrec/service/jaxws")
public class JaxWsPatientFacadeBroker implements JaxWsPatientFacade {

	Logger LOGGER = Logger.getLogger(JaxWsPatientFacadeBroker.class.getName());

	@Inject
	private PatientFacade patientFacade;

	public Patient getPatient(Long patientId) {
		LOGGER.info("Invoked: getPatient");
		return patientFacade.getPatient(patientId);
	}

	public FoundPatient findApprovedPatientBySsn(String ssn) {
		LOGGER.info("Invoked: findApprovedPatientBySsn");
		return patientFacade.findApprovedPatientBySsn(ssn);
	}

	public List<FoundPatient> findApprovedPatientsByLastName(String lastName) {
		LOGGER.info("Invoked: findApprovedPatientsByLastName");
		return patientFacade.findApprovedPatientsByLastName(lastName);
	}

	public List<FoundPatient> fuzzyFindApprovedPatientsByLastNameAndSsn(String lastName, String ssn) {
		LOGGER.info("Invoked: fuzzyFindApprovedPatientsByLastNameAndSsn");
		List<FoundPatient> foundPatient = patientFacade.fuzzyFindApprovedPatientsByLastNameAndSsn(lastName, ssn);
		for (FoundPatient foundPatient2 : foundPatient) {
			LOGGER.info("Found Patient: " + foundPatient2.getSsn() + foundPatient2.getName().toString());
		}
		return foundPatient;
	}

}
