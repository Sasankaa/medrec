package com.oracle.medrec.service.batch;

import com.oracle.medrec.model.Drug;
import com.oracle.medrec.model.Prescription;

import javax.enterprise.context.ApplicationScoped;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * An application scoped cache to conserve the cost at Drug dimension for Drug report.
 *
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.<br>
 *         Created by xiaojwu on 14-2-17.
 */
@ApplicationScoped
public class DrugsSumCache {

  // key - job seq
  private final Map<String, DrugSum> cacheMap = new HashMap();

  /**
   * Starts cache drug statistic for a job.
   *
   * @param seq String - job sequence id.
   * @return
   */
  public DrugSum cache(String seq) {
    DrugSum sum = new DrugSum();
    cacheMap.put(seq, sum);
    return sum;
  }

  /**
   * Gets drug statistic of a job.
   *
   * @param seq String - job sequence id.
   * @return
   */
  public DrugSum getCache(String seq) {
    return cacheMap.get(seq);
  }

  /**
   * Releases cache for a job.
   *
   * @param seq String - job sequence id.
   */
  public void release(String seq) {
    if (seq != null) {
      cacheMap.remove(seq);
    }
  }

  /**
   * Inner class to cache {@link com.oracle.medrec.service.batch.DrugsSumCache.DrugUnit}s of one job.
   */
  class DrugSum {
    // key - drug id
    private final Map<Long, DrugUnit> jobMap = new HashMap();

    /**
     * Adds up amount of money of Prescription respectively classified by Drug id.
     *
     * @param prescription
     */
    public void addPrescription(Prescription prescription) {
      DrugUnit unit = jobMap.get(prescription.getDrug().getId());
      if (unit == null) {
        unit = new DrugUnit(prescription.getDrug());
        unit.dosage_sum = BigDecimal.valueOf(prescription.getDosage());
        unit.cost_sum = prescription.getCost();
        jobMap.put(prescription.getDrug().getId(), unit);
      } else {
        unit.dosage_sum = unit.dosage_sum.add(BigDecimal.valueOf(prescription.getDosage()));
        unit.cost_sum = unit.cost_sum.add(prescription.getCost());
      }
    }

    /**
     * Adds up amount of money of Prescriptions respectively classified by Drug id.
     *
     * @param prescriptions
     */
    public void addPrescription(Collection<Prescription> prescriptions) {
      if (prescriptions == null) {
        return;
      }
      for (Prescription p : prescriptions) {
        addPrescription(p);
      }
      // prescriptions.forEach(this::addPrescription);
    }

    /**
     * Gets all {@link com.oracle.medrec.service.batch.DrugsSumCache.DrugUnit}s
     *
     * @return
     */
    public Collection<DrugUnit> getUnits() {
      return jobMap.values();
    }
  }

  /**
   * Inner class to hold one drug's sum of dosage and cost.
   */
  class DrugUnit {
    Drug drug;
    BigDecimal dosage_sum;
    BigDecimal cost_sum;

    DrugUnit(Drug drug) {
      this.drug = drug;
    }
  }

}
