package com.oracle.medrec.common.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class ExternalConfiguration {

	private static Logger LOGGER = Logger.getLogger(ExternalConfiguration.class.getName());

	private static ExternalConfiguration externalConfig;

	private static Properties properties = new Properties();

	public static final String EXTERNAL_CONFIG_KEY = "configFile";
	
	public static final String PHYSICIAN_SERVICE_URL = "PHYSICIAN_SERVICE";
	public static final String PATIENT_SERVICE_URL = "PATIENT_SERVICE";

	public static final String MEDREC_URL = "MEDREC_URL";

	/**
	 * Singleton
	 */
	private ExternalConfiguration() {
	}

	/**
	 * Load External Configuration properties
	 * @return Singleton of ExternalConfiguration
	 */
	public static ExternalConfiguration getExternalConfig() {
		LOGGER.info("Get External Config Properties");
		if (externalConfig == null) {
			String configFile = System.getProperty(EXTERNAL_CONFIG_KEY);
			if (configFile == null) {
				LOGGER.warning(
						"External Config File path not available from System.properties. "
						+ "If you want to use add -DconfigFile=\"PATH_TO_EXTERNAL_CONFIG_FILE\" to JAVA_OPTIONS variable!");
			} else {
				LOGGER.info("Reading system property from: " + configFile);
				try {
					InputStream iostream = new FileInputStream(new File(configFile));
					LOGGER.info("configfile available: " + iostream.available());
					properties.load(iostream);
				} catch (Throwable th) {
					LOGGER.warning("Can't load external config file from: " + configFile);
					th.printStackTrace();
				}
				LOGGER.info("External Config Properties loaded.");
			}
		}
		return externalConfig;
	}

	/**
	 * Return external configuration value.
	 * @param configKey
	 * @return config value. Null if not exist.
	 */
	public static String getExternalConfigValue(String configKey) {
		getExternalConfig();
		String value = properties.getProperty(configKey);
		LOGGER.info("Get External Config: " + configKey + "/ Value: " + (value == null ? "Not found!!!" : value));
		return value;
	}

}
