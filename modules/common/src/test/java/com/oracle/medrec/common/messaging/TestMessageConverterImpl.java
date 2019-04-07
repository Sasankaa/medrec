package com.oracle.medrec.common.messaging;

/**
 * TODO heavily refactored
 * <p/>
 * {@link MessageComposerImpl} test case.
 *
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 * @since Jul 18, 2007
 */
public class TestMessageConverterImpl {
  // public TestMessageConverterImpl() {
  // }
  //
  // private MessageComposerImpl impl;
  //
  // @Before
  // public void setUp() {
  // impl = new MessageComposerImpl();
  // }
  //
  // @Test
  // public void testConvertToDesiredType() throws Exception {
  // InnerClass obj = new InnerClass();
  // ObjectMessage omsg = createMock(ObjectMessage.class);
  // TextMessage tmsg = createMock(TextMessage.class);
  //
  // omsg.getObject();
  // expectLastCall().andReturn(obj);
  // tmsg.getText();
  // expectLastCall().andReturn("foo");
  //
  // replay(omsg);
  // replay(tmsg);
  // assertSame(obj, impl.convert(InnerClass.class, omsg));
  // assertEquals("foo", impl.convert(String.class, tmsg));
  // verify(omsg);
  // verify(tmsg);
  // }
  //
  // @Test(expected = MessageException.class)
  // public void testConvertToDesiredTypeUnknownMessageType() throws Exception
  // {
  // InnerMessage imsg = createMock(InnerMessage.class);
  //
  // impl.convert(String.class, imsg);
  // }
  //
  // @Test
  // public void testConvertToMessage() throws Exception {
  // InnerClass obj = new InnerClass();
  // Session session = createMock(Session.class);
  // ObjectMessage msg = createMock(ObjectMessage.class);
  //
  // session.createObjectMessage(obj);
  // expectLastCall().andReturn(msg);
  //
  // replay(session);
  // assertSame(msg, impl.composeMessage(session, obj));
  // verify(session);
  // }
  //
  // public static class InnerClass implements Serializable {
  // }
  //
  // public static interface InnerMessage extends Message {
  // }
}
