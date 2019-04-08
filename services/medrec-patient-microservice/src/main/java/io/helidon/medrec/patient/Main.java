/*
 * Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.
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

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.LogManager;

import io.helidon.microprofile.server.Server;

/**
 * Main method simulating trigger of main method of the server.
 */
public final class Main {

	/**
	 * Cannot be instantiated.
	 */
	private Main() {
	}

	/**
	 * Application main entry point.
	 * 
	 * @param args command line arguments
	 * @throws IOException if there are problems reading logging properties
	 */
	public static void main(final String[] args) throws IOException {
		setupLogging();

		Server server = startServer();

		System.out.println("http://localhost:" + server.port() + "/greet");

		String driver = "org.apache.derby.jdbc.EmbeddedDriver";
		String protocol = "jdbc:derby:";

		try {
			Class.forName(driver).newInstance();
			Connection conn = DriverManager.getConnection(protocol + "patientDB;create=true", new Properties());
			// Creating a database table
			Statement sta = conn.createStatement();
			int count = sta
					.executeUpdate("CREATE TABLE patient (ID INT, firstname VARCHAR(20)," + " lastname VARCHAR(20))");
			System.out.println("Table created.");
			sta.close();

			conn.close();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Start the server.
	 * 
	 * @return the created {@link Server} instance
	 */
	static Server startServer() {
		// Server will automatically pick up configuration from
		// microprofile-config.properties
		// and Application classes annotated as @ApplicationScoped
		return Server.create().start();
	}

	/**
	 * Configure logging from logging.properties file.
	 */
	private static void setupLogging() throws IOException {
		// load logging configuration
		LogManager.getLogManager().readConfiguration(Main.class.getResourceAsStream("/logging.properties"));
	}
}
