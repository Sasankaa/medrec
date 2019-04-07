package com.oracle.medrec.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@Embeddable
@XmlRootElement
public class VitalSigns implements Serializable {

  private static final long serialVersionUID = 2907071444700296617L;

  @Column(name = "systolic_blood_pressure")
  private Double systolicBloodPressure;

  @Column(name = "diastolic_blood_pressure")
  private Double diastolicBloodPressure;

  private Double height;

  private Double pulse;

  private Double temperature;

  private Double weight;

  public Double getSystolicBloodPressure() {
    return systolicBloodPressure;
  }

  public void setSystolicBloodPressure(Double systolicBloodPressure) {
    this.systolicBloodPressure = systolicBloodPressure;
  }

  public Double getDiastolicBloodPressure() {
    return diastolicBloodPressure;
  }

  public void setDiastolicBloodPressure(Double diastolicBloodPressure) {
    this.diastolicBloodPressure = diastolicBloodPressure;
  }

  public Double getHeight() {
    return height;
  }

  public void setHeight(Double height) {
    this.height = height;
  }

  public Double getPulse() {
    return pulse;
  }

  public void setPulse(Double pulse) {
    this.pulse = pulse;
  }

  public Double getTemperature() {
    return temperature;
  }

  public void setTemperature(Double temperature) {
    this.temperature = temperature;
  }

  public Double getWeight() {
    return weight;
  }

  public void setWeight(Double weight) {
    this.weight = weight;
  }

}
