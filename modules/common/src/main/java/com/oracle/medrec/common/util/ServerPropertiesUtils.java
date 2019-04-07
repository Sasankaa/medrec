package com.oracle.medrec.common.util;

import com.oracle.medrec.common.core.SystemException;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.logging.Logger;

/**
 *
 * @author : xiaojwu.
 *         Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */

public abstract class ServerPropertiesUtils {

  private static final Logger LOGGER = Logger.getLogger(ServerPropertiesUtils.class.getName());

  private static String REGION;

  private final static String SERVER = "server";
  private final static String CLUSTER = "cluster";
  private final static String COHERENCE_CLUSTER = "coherence_cluster";

  private static boolean IS_ON_SERVER = false;
  private static boolean IS_ON_COHERENCE = false;


  public static void checkTarget(String target) {
    if (target.equals(SERVER)) {
      IS_ON_SERVER = true;
      LOGGER.info("MedRec is running in single server.");
    } else if (target.equals(CLUSTER)) {
      LOGGER.info("MedRec is running in non-coherence cluster.");
    } else if (target.equals(COHERENCE_CLUSTER)) {
      IS_ON_COHERENCE = true;
      LOGGER.info("MedRec is running in coherence cluster.");
    }
  }

  public static boolean isOnServer() {
    return IS_ON_SERVER;
  }

  public static boolean isOnCoherence() {
    return IS_ON_COHERENCE;
  }

  /**
   * Gets application region uri host:port/ or host:port/partition/ (if MT).
   *
   * @return
   */
  public static String getRegion() {
    return REGION;
  }

  /**
   * Sets application region uri host:port/ or host:port/partition/ (if MT).
   *
   * @param region
   */
  public static void setRegion(String region) {
    REGION = region;
  }

}
