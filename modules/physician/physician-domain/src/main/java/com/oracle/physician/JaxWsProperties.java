package com.oracle.physician;

import com.oracle.medrec.common.util.ServerPropertiesUtils;

/**
 * @author Xiaojun Wu. <br>
 *         Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public abstract class JaxWsProperties {

  public final static String NAMESPACEURL = "http://www.oracle.com/medrec/service/jaxws";

  public final static String WSURL = "http://" + ServerPropertiesUtils.getRegion() + "medrec-jaxws-services/";
}
