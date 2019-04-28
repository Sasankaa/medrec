/*
 * Copyright (c) 2018, 2019 Oracle and/or its affiliates. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.helidon.medrec.patient;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oracle.medrec.model.Patient;

/**
 * A Patient service for Medrec application
 */
@Path("/patient")
@RequestScoped
public class PatientResource {

	private static final Logger LOGGER = Logger.getLogger(PatientResource.class.getName());

	private static final JsonBuilderFactory JSON = Json.createBuilderFactory(Collections.emptyMap());

	/**
	 * Using constructor injection to get a configuration property. By default this
	 * gets the value from META-INF/microprofile-config
	 *
	 * @param greetingConfig the configured greeting message
	 */
	@Inject
	public PatientResource() {
	}

	/**
	 * Return a wordly greeting message.
	 *
	 * @return {@link JsonObject}
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public JsonObject getDefaultMessage() {
		String msg = String.format("%s!", PatientResource.class.getName());

		return JSON.createObjectBuilder().add("message", msg).build();
	}

	/**
	 * Set the greeting to use in future messages.
	 *
	 * @param jsonObject JSON containing the new greeting
	 * @return {@link Response}
	 *
	 * 				@SuppressWarnings("checkstyle:designforextension") @Path("/greeting")
	 * @PUT
	 * @Consumes(MediaType.APPLICATION_JSON) @Produces(MediaType.APPLICATION_JSON)
	 *                                       public Response
	 *                                       updateGreeting(JsonObject jsonObject) {
	 * 
	 *                                       if
	 *                                       (!jsonObject.containsKey("greeting")) {
	 *                                       JsonObject entity =
	 *                                       JSON.createObjectBuilder().add("error",
	 *                                       "No greeting provided").build(); return
	 *                                       Response.status(Response.Status.BAD_REQUEST).entity(entity).build();
	 *                                       }
	 * 
	 *                                       String newGreeting =
	 *                                       jsonObject.getString("greeting");
	 * 
	 *                                       greetingProvider.setMessage(newGreeting);
	 *                                       return
	 *                                       Response.status(Response.Status.NO_CONTENT).build();
	 *                                       }
	 */

	@SuppressWarnings("unchecked")
	@Path("/findApprovedPatientsByLastNameAndSsn")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response findApprovedPatientsByLastNameAndSsn(
			@DefaultValue("") @QueryParam(value = "lastName") final String lastName,
			@DefaultValue("") @QueryParam(value = "ssn") final String ssn) {

		LOGGER.info("findApprovedPatientsByLastNameAndSsn invoked. Params: " + lastName + ", " + ssn);
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("PatientServiceEM");
		EntityManager em = entityManagerFactory.createEntityManager();

		Query query = em
				.createQuery(
						"SELECT p FROM Patient p WHERE p.ssn LIKE :ssnparameter AND p.name.lastName LIKE :lastnameparameter")
				.setParameter("ssnparameter", "%" + ssn + "%").setParameter("lastnameparameter", "%" + lastName + "%");
		List<Patient> resultList = query.getResultList();
		
		if (resultList.size() == 0) {
			LOGGER.info("Patient not found");
			return Response.status(Response.Status.NO_CONTENT).build();
		}

		resultList.forEach((patient) -> {
			LOGGER.info("Patient found: " + patient.getUsername() + " / " + patient.getSsn());
		});

		ObjectMapper objMap = new ObjectMapper();
		String result = "";

		try {
			result = objMap.writeValueAsString(resultList);
		} catch (JsonProcessingException e) {
			LOGGER.warning("JSON conversion error");
			e.printStackTrace();
		}
		em.close();

		return Response.ok(result).build();

	}
}
