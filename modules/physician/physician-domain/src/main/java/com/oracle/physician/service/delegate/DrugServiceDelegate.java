package com.oracle.physician.service.delegate;

import com.oracle.medrec.common.util.ServerPropertiesUtils;
import com.oracle.medrec.facade.model.DrugInfo;
import com.oracle.physician.JaxRsProperties;
import com.oracle.physician.service.DrugService;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.List;
import java.util.logging.Logger;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

/**
 * @author Xiaojun Wu. <br>
 *         Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public class DrugServiceDelegate implements DrugService {

  private static final Logger LOGGER = Logger.getLogger(DrugServiceDelegate.class.getName());

  private WebTarget drugFacade;

  private final static String KEY = "drugs";

  @PostConstruct
  public void init() {
    drugFacade = ClientBuilder.newClient().target(URI.create(JaxRsProperties.DRUG_URI));
  }

  @Override
  public List<DrugInfo> getAllDrugsInfo() {
    // check whether in coherence cluster
    if (ServerPropertiesUtils.isOnCoherence()) {
      // use shared cache across partitions
      NamedCache sharedCache = CacheFactory.getCache("SharedCache");
      // first check data whether has been stored in cache
      List<DrugInfo> result = (List<DrugInfo>) sharedCache.get(KEY);
      if (result == null) {
        LOGGER.info("Drug info list is not stored in shared cache.");
        // if not cache, get data from server end point
        LOGGER.info("Fetch list from server end point.");
        result = drugFacade.request().get(new GenericType<List<DrugInfo>>() {});
        // store data, after that, following invocation will fetch data from cache directly
        sharedCache.put(KEY, result);
        LOGGER.info("Store drug info list into shared cache.");
      } else {
        LOGGER.info("Drug info list has already stored in shared cache");
      }
      return result;
    }
    // single server
    return drugFacade.request().get(new GenericType<List<DrugInfo>>() {});
  }
}
