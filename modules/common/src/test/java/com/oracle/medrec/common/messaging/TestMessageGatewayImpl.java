package com.oracle.medrec.common.messaging;

/**
 * TODO heavily refactored
 *
 * @author Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 * @since Jul 18, 2007
 */
public class TestMessageGatewayImpl {
  public TestMessageGatewayImpl() {
  }

  // private ResourceManager rm;
  // private MessageComposer mc;
  // private NamingClient ng;
  // private MessageClientImpl impl;
  //
  // @Before
  // public void setUp() {
  // rm = createMock(ResourceManager.class);
  // mc = createMock(MessageComposer.class);
  // ng = createMock(NamingClient.class);
  // impl = new MessageClientImpl();
  // impl.setResourceManager(rm);
  // impl.setMessageConverter(mc);
  // impl.setNamingGateway(ng);
  // }
  //
  // @Test
  // public void testSendMessage() throws Exception {
  // FooService obj = new FooService();
  // Connection conn = createMock(Connection.class);
  // Session session = createMock(Session.class);
  // Message message = createMock(Message.class);
  // MessageProducer mp = createMock(MessageProducer.class);
  // Destination dest = createMock(Destination.class);
  //
  // rm.getConnection();
  // expectLastCall().andReturn(conn);
  // conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
  // expectLastCall().andReturn(session);
  // mc.composeMessage(session, obj);
  // expectLastCall().andReturn(message);
  // rm.getDestination("dest");
  // expectLastCall().andReturn(dest);
  // session.createProducer(dest);
  // expectLastCall().andReturn(mp);
  // mp.send(message);
  // expectLastCall().once();
  // message.getJMSMessageID();
  // expectLastCall().andReturn("foo");
  // conn.close();
  // expectLastCall().once();
  //
  // replay(rm);
  // replay(mc);
  // replay(ng);
  // replay(conn);
  // replay(session);
  // replay(message);
  // replay(mp);
  // replay(dest);
  // assertEquals("foo", impl.send("dest", obj));
  // verify(rm);
  // verify(mc);
  // verify(ng);
  // verify(conn);
  // verify(session);
  // verify(message);
  // verify(mp);
  // verify(dest);
  // }
  //
  // public static class FooService implements Serializable {
  // }
}
