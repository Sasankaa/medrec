package com.oracle.medrec.facade.impl;

import com.oracle.medrec.facade.model.RecordDetail;
import com.oracle.medrec.facade.model.RecordSummary;
import com.oracle.medrec.facade.model.RecordToCreate;
import com.oracle.medrec.model.Physician;
import com.oracle.medrec.model.Record;
import com.oracle.medrec.service.RecordService;
import junit.framework.JUnit4TestAdapter;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * {@link RecordFacadeImpl} test case.
 *
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 * @since Jul 17, 2007
 */
public class RecordFacadeImplTestCase {

  private RecordService rs;
  private RecordFacadeImpl impl;

  public static junit.framework.Test suite() {
    return new JUnit4TestAdapter(RecordFacadeImplTestCase.class);
  }

  @Before
  public void setUp() {
    rs = createMock(RecordService.class);
    impl = new RecordFacadeImpl();
    impl.setRecordService(rs);
  }

  @Test
  public void testCreateRecord() {
    rs.createRecord(isA(Record.class), eq(2L), eq(1L));
    expectLastCall().once();
    replay(rs);
    RecordToCreate rtc = new RecordToCreate();
    rtc.setPhysicianId(2L);
    rtc.setPatientId(1L);
    impl.createRecord(rtc);
    verify(rs);
  }

  @Test
  public void testGetRecordSummaryByPatientId() {
    Record r1 = new Record();
    r1.setPhysician(new Physician());
    Record r2 = new Record();
    r2.setPhysician(new Physician());
    List<Record> records = Arrays.asList(r1, r2);
    rs.getRecordsByPatientId(1L);
    expectLastCall().andReturn(records);
    replay(rs);
    RecordSummary sum = impl.getRecordSummaryByPatientId(1L);
    assertNotNull(sum);
    assertEquals(sum.getPatientId(), (Long) 1L);
    verify(rs);
  }

  @Test(expected = AssertionError.class)
  public void testGetRecordDetailWithInvalidId() {
    rs.getRecord(1L);
    expectLastCall().andReturn(null);

    replay(rs);
    impl.getRecordDetail(1L);
    verify(rs);
  }

  @Test
  public void testGetRecordDetail() {
    Record record = new Record();
    rs.getRecord(1L);
    expectLastCall().andReturn(record);

    replay(rs);
    RecordDetail rd = impl.getRecordDetail(1L);
    assertNotNull(rd);
    verify(rs);
  }
}
