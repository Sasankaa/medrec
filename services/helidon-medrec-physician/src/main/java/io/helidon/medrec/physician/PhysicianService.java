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

package io.helidon.medrec.physician;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oracle.medrec.model.Physician;

import io.helidon.common.http.Http;
import io.helidon.config.Config;
import io.helidon.webserver.Routing;
import io.helidon.webserver.ServerRequest;
import io.helidon.webserver.ServerResponse;
import io.helidon.webserver.Service;

/**
 * A Physician service for Medrec application.
 */

public class PhysicianService implements Service {

	Logger LOGGER = Logger.getLogger(PhysicianService.class.getName());

	private static final JsonBuilderFactory JSON = Json.createBuilderFactory(Collections.emptyMap());

	PhysicianService(Config config) {
	}

	/**
	 * A service registers itself by updating the routine rules.
	 * 
	 * @param rules the routing rules.
	 */
	@Override
	public void update(Routing.Rules rules) {
		rules.get("/", this::getDefaultHandler).get("/authenticateAndReturnPhysician",
				this::authAndReturnPhysicianHandler);
	}

	/**
	 * Return a wordly greeting message.
	 * 
	 * @param request  the server request
	 * @param response the server response
	 */
	private void getDefaultHandler(ServerRequest request, ServerResponse response) {
		String msg = String.format("%s!", this.getClass().getName());

		JsonObject returnObject = JSON.createObjectBuilder().add("message", msg).build();
		response.send(returnObject);
	}

	/**
	 * Return a greeting message using the name that was provided.
	 * 
	 * @param request  the server request
	 * @param response the server response
	 */
	// TODO using password in query param maybe not the best
	private void authAndReturnPhysicianHandler(ServerRequest request, ServerResponse response) {
		String userName = request.queryParams().first("userName").orElse("");
		String password = request.queryParams().first("password").orElse("");
		LOGGER.info("Physician handler invoked username: " + userName + "/ password: " + password);
		String result = getPhysician(userName, password);
		if (result.length() == 0) {
			response.status(Http.Status.NO_CONTENT_204);
		}
		response.send(result);
	}

	@SuppressWarnings("unchecked")
	private String getPhysician(String userName, String password) {

		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("PhysicianService");
		EntityManager em = entityManagerFactory.createEntityManager();

		Query query = em
				.createQuery("SELECT p FROM Physician p WHERE p.username = :userName AND p.password = :password")
				.setParameter("userName", userName).setParameter("password", password);
		List<Physician> resultList = query.getResultList();

		ObjectMapper objMap = new ObjectMapper();
		String result = "";

		for (Physician physician : resultList) {
			LOGGER.info("Physican query result:" + physician.getUsername() + " / " + physician.getPassword());
			try {
				result = objMap.writeValueAsString(physician);
				LOGGER.info("Physician JSON object:" + result);
			} catch (JsonProcessingException e) {
				LOGGER.warning("Physican Object to JSON error.");
				e.printStackTrace();
			}
		}
		em.close();
		return result;
	}

}
