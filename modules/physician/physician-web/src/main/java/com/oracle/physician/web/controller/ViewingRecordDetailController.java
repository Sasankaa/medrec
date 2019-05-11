package com.oracle.physician.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import com.oracle.medrec.facade.model.RecordDetail;
import com.oracle.physician.service.RecordService;
import com.oracle.physician.web.Constants;

/**
 * ViewingRecordDetailController is a JSF ManagedBean that is responsible for
 * showing details of record.
 *
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@ManagedBean(name = "viewingRecordDetailController")
@SessionScoped
public class ViewingRecordDetailController extends BasePhysicianPageController {

  private static final Logger LOGGER = Logger.getLogger(ViewingRecordDetailController.class.getName());

  private String recordId;

  private RecordDetail recordDetail;

  @Inject
  private RecordService recordService;

  public void setRecordId(String recordId) {
    LOGGER.finest("Set record id: " + recordId);
    this.recordId = recordId;
  }

  public RecordDetail getRecordDetail() {
    return recordDetail;
  }

  public String viewRecordDetail() {
    setRecordId(getPageContext().getRequestParam("recordId"));

    LOGGER.finer("Record ID: " + recordId);

    // find record detail by id
    recordDetail = recordService.getRecordDetail(Long.parseLong(recordId));

    LOGGER.finer("Got record detail");
    LOGGER.finer("Symptoms: " + recordDetail.getSymptoms());
    LOGGER.finer("Temperature: " + recordDetail.getVitalSigns().getTemperature());
    LOGGER.finer("Number of prescriptions: " + recordDetail.getPrescriptions().size());
    if (recordDetail.getPrescriptions().size() > 0) {
      LOGGER.finer("Drug of the 1st prescription: " + recordDetail.getPrescriptions().get(0).getDrug());
    }
    LOGGER.info("viewRecordDetail CURRENT VIEW" + FacesContext.getCurrentInstance().getViewRoot().getViewId());
    return Constants.VIEW_RECORD_DETAIL;
  }
  
  public String getXray() {
  	FacesContext fc = FacesContext.getCurrentInstance();
    ExternalContext ec = fc.getExternalContext();
    
    Object xrayId = getPageContext().getRequestParam("xrayId");
  	LOGGER.info("Download xray id: " + xrayId );
    File file = new File("/Users/pnagy/Desktop/xray.png");
    
    ec.responseReset();
    ec.setResponseContentType("image/jpg");
    ec.setResponseContentLength(new Long(file.length()).intValue());
    ec.setResponseHeader("Content-Disposition", "attachment;filename=xray.png");
    
    FileInputStream input = null;
    try {
			OutputStream out = ec.getResponseOutputStream();
			Files.copy(file.toPath(), out);
      fc.responseComplete();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(input != null) {
				try {
					input.close();
				} catch (IOException e) {
				}
			}
		}
    LOGGER.info("getXray current view" + FacesContext.getCurrentInstance().getViewRoot().getViewId());
    return null;
  }  
  
}
