package com.oracle.physician;

import com.oracle.medrec.common.util.ServerPropertiesUtils;

/**
 * @author Xiaojun Wu. <br>
 *         Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public abstract class JaxRsProperties {

  public final static String RSURL = "http://" + ServerPropertiesUtils.getRegion() + "medrec-jaxrs-services/resources/";

  public final static String RECORD_URI = RSURL + "records";

  public final static String PHYSICIAN_URI = RSURL + "physician";

  public final static String DRUG_URI = RSURL + "drug";
}
