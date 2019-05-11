package com.oracle.medrec.facade.broker.jaxws;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.jws.WebService;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.oracle.medrec.common.config.ExternalConfiguration;
import com.oracle.medrec.common.config.Services;
import com.oracle.medrec.facade.JaxWsPatientFacade;
import com.oracle.medrec.facade.PatientFacade;
import com.oracle.medrec.facade.model.FoundPatient;
import com.oracle.medrec.model.Patient;

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

	@SuppressWarnings("rawtypes")
	public List<FoundPatient> fuzzyFindApprovedPatientsByLastNameAndSsn(String lastName, String ssn) {
		LOGGER.info("Invoked: fuzzyFindApprovedPatientsByLastNameAndSsn");

		List<Patient> patientList;
		List<FoundPatient> foundPatient = new ArrayList<FoundPatient>();

		String patientServiceUrl = System.getenv(ExternalConfiguration.PATIENT_SERVICE_URL);
				//ExternalConfiguration.getExternalConfigValue(ExternalConfiguration.PATIENT_SERVICE_URL);

		if (patientServiceUrl != null) {
			LOGGER.info("Patient service URL found in external configuration: " + patientServiceUrl);

			try {
				Client client = ClientBuilder.newClient();
				WebTarget patientTarget = client
						.target(patientServiceUrl + "/" + Services.FIND_APPROVED_PATIENTS_BY_LASTNAME_AND_SSN)
						.queryParam("ssn", ssn).queryParam("lastName", lastName);

				Response response = patientTarget.request(MediaType.APPLICATION_JSON).get();
				LOGGER.info("Patient service invocation was successful: " + response.getStatus());

				if (response.getStatus() == Response.Status.NO_CONTENT.getStatusCode()) {
					foundPatient = null;
					LOGGER.info("Patient not found.");
				} else if (response.getStatus() == Response.Status.OK.getStatusCode()) {
					patientList = response.readEntity(new GenericType<List<Patient>>() {
					});
					LOGGER.info("Patients found.");
					foundPatient = new ArrayList<FoundPatient>();
					for (Iterator iterator = patientList.iterator(); iterator.hasNext();) {
						Patient patient = (Patient) iterator.next();
						LOGGER
								.info("=> " + patient.getUsername() + " / " + patient.getSsn());
						foundPatient.add(new FoundPatient(patient));
					}

				} else {
					LOGGER.warning("Patient service response error.");
				}
			} catch (Exception e) {
				LOGGER.warning("Patient service invocation error.");
				e.printStackTrace();
			}
		} else {
			LOGGER.info("Patient service URL NOT found in external configuration.");
			foundPatient = patientFacade.fuzzyFindApprovedPatientsByLastNameAndSsn(lastName, ssn);
		}

		if (foundPatient != null) {
			foundPatient.forEach((patient) -> {
				LOGGER.info("Patient => " + patient.getName() + " / " + patient.getSsn());
			});
		}
		return foundPatient;
	}

}
