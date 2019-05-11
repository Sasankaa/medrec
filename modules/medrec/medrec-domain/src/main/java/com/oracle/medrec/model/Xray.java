package com.oracle.medrec.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
//@Embeddable
//@XmlRootElement
public class Xray implements Serializable {

  public Xray() {
		super();
	}

	private static final long serialVersionUID = 2907071444700296617L;

  private Long id;

  private Long patientId;

  private Date dateCreated;
  
  private String xrayArea;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPatientId() {
		return patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getXrayArea() {
		return xrayArea;
	}

	public void setXrayArea(String xrayArea) {
		this.xrayArea = xrayArea;
	}


}
