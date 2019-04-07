package com.oracle.medrec.service.batch;

import com.oracle.medrec.model.Physician;
import com.oracle.medrec.model.Record;

import javax.enterprise.context.ApplicationScoped;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * An application scoped cache to conserve the cost at physician dimension for physician report.
 *
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.<br>
 *         Created by xiaojwu on 14-2-17.
 */
@ApplicationScoped
public class PhysicianSumCache {

  // key - job seq
  private final Map<String, PhysicianSum> cacheMap = new HashMap();

  /**
   * Starts cache physician statistic for a job.
   *
   * @param seq String - job sequence id.
   * @return
   */
  public PhysicianSum cache(String seq) {
    PhysicianSum sum = new PhysicianSum();
    cacheMap.put(seq, sum);
    return sum;
  }

  /**
   * Gets cache physician statistic of a job.
   *
   * @param seq String - job sequence id.
   * @return
   */
  public PhysicianSum getCache(String seq) {
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
   * Inner class to cache {@link com.oracle.medrec.service.batch.PhysicianSumCache.PhysicianUnit}s of one job.
   */
  class PhysicianSum {
    private final Map<Long, PhysicianUnit> jobMap = new HashMap();

    /**
     * Adds up amount of money of Record.
     *
     * @param record
     */
    public void addRecord(Record record) {
      PhysicianUnit unit = jobMap.get(record.getPhysician().getId());
      if (unit == null) {
        unit = new PhysicianUnit(record.getPhysician());
        unit.record_num = 1;
        unit.cost_sum = record.getCost();
        jobMap.put(record.getPhysician().getId(), unit);
      } else {
        unit.record_num++;
        unit.cost_sum = unit.cost_sum.add(record.getCost());
      }
    }

    /**
     * Gets all {@link com.oracle.medrec.service.batch.PhysicianSumCache.PhysicianUnit}s
     *
     * @return
     */
    public Collection<PhysicianUnit> getUnits() {
      return jobMap.values();
    }
  }

  /**
   * Inner class to hold one physician's sum of record and cost.
   */
  class PhysicianUnit {
    Physician physician;
    int record_num;
    BigDecimal cost_sum;

    PhysicianUnit(Physician physician) {
      this.physician = physician;
    }
  }


}
