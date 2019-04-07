package com.oracle.medrec.chat;

import com.oracle.medrec.chat.eliza.ElizaService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.Session;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Chat service is a Singleton offering methods that refer to all services
 * related chat to web socket end points. One online physician, one opened chat
 * room.
 *
 * @author Xiaojun Wu. <br>
 *         Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@ApplicationScoped
public class ChatService {

    /**
     * Participants respectively of every chat room. Keys are physicians' id.
     */
    private Map<String, Set<Session>> room_map;

    /**
     * All patients online
     */
    private Map<String, Session> patient_map;

    /**
     * All physicians online
     */
    private Map<String, Session> physician_map;

    @Inject
    private ElizaService elizaService;

    public ChatService() {
        room_map = new ConcurrentHashMap<String, Set<Session>>();
        patient_map = new ConcurrentHashMap<String, Session>();
        physician_map = new ConcurrentHashMap<String, Session>();
    }

    /**
     * Send message to web socket client end point.
     *
     * @param session
     * @param message
     */
    public void sendMessage(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            try {
                session.close();
            } catch (IOException ioe) {
                System.err.println("Fail to close connection with client");
                ioe.printStackTrace();
            }
        }
    }

    /**
     * Get all online physicians at present.
     *
     * @param session
     */
    public void getAllOnlinePhysicans(Session session) {

        String json = ResponseMessageFactory
                .createAllOnlinePhysicianMessage(physician_map.values());
        sendMessage(session, json);
    }

    /**
     * Physician log into the chat server. It should be invoked first when a
     * physician connection was set up.
     *
     * @param session
     * @param id
     * @param name
     */
    public void physicianLogin(Session session, String id, String name) {
        physician_map.put(id, session);

        // create chat room
        Set<Session> set = new HashSet<Session>();
        set.add(session);
        room_map.put(id, set);
        sendMessage(session,
                ResponseMessageFactory
                        .createSystemMessage("Your chat room is open."));

    }

    /**
     * Broadcast physician's message over the chat room.
     *
     * @param id
     * @param name
     * @param message
     */
    public void physicianChat(String id, String name, String message) {
        String json = ResponseMessageFactory.createPhysicianMessage(name,
                message);
        for (Session s : room_map.get(id)) {
            if (!s.getUserProperties().get("id").equals(id)) {
                sendMessage(s, json);
            }
        }
    }

    /**
     * This method should be invoked when a physician disconnected.
     *
     * @param session
     */
    public void physicianLogout(Session session) {
        String id = (String) session.getUserProperties().get("id");
        if (physician_map.get(id) != null) {
            String name = (String) session.getUserProperties().get("name");
            Set<Session> set = room_map.get(id);
            set.remove(session);
            // notice the chat room is close.
            String json = ResponseMessageFactory.createRoomCloseMessage(name);
            for (Session s : set) {
                sendMessage(s, json);
            }

            // close the chat room
            room_map.remove(id);

            // physician is off line
            physician_map.remove(id);
        }
    }

    /**
     * Patient log into the chat server. It should be invoked first when a
     * patient connection was set up.
     *
     * @param session
     * @param name
     */
    public void patientLogin(Session session, String id, String name) {
        patient_map.put(id, session);
    }

    /**
     * After connection a patient enter a certain physician's chat room.
     *
     * @param session
     * @param room_id
     */
    public void patientJoin(Session session, String room_id) {

        // physician does not exist
        if (room_id == null) {
            sendMessage(
                    session,
                    ResponseMessageFactory
                            .createSystemMessage("This physician is offline. Please choose another one."));
        }

        if (room_id.equals("eliza")) {
            String reply = elizaService.newChat();
            String json = ResponseMessageFactory
                    .createPhysicianMessage("Eliza", reply);
            session.getUserProperties().put("room_id", room_id);
            sendMessage(session, json);
            return;
        }

        // chat room is close.
        Set<Session> set = room_map.get(room_id);
        if (set == null) {
            sendMessage(
                    session,
                    ResponseMessageFactory
                            .createSystemMessage("This physician is offline. Please choose another one."));
            return;
        }

        String id = (String) session.getUserProperties().get("id");
        String name = (String) session.getUserProperties().get("name");

        // if this patient has join a chat, leaving at first
        patientLeft(session);

        // put patient into the new chat room, and notice everyone in this room.
        String json = ResponseMessageFactory.createNewRoomMemberMessage(id,
                name);
        for (Session s : set) {
            sendMessage(s, json);
        }

        set.add(session);
        session.getUserProperties().put("room_id", room_id);

        // send all online members
        json = ResponseMessageFactory.createAllRoomMemberMessage(set);
        sendMessage(session, json);
    }

    /**
     * After connection a patient leave a certain physician's chat room.
     *
     * @param session
     */
    public void patientLeft(Session session) {
        String room_id = (String) session.getUserProperties().get("room_id");

        // physician does not exist
        if (room_id == null) {
            return;
        }

        Set<Session> set = room_map.get(room_id);
        // chat room is close.
        if (set == null) {
            return;
        }
        set.remove(session);
        session.getUserProperties().remove("room_id");

        String id = (String) session.getUserProperties().get("id");
        String name = (String) session.getUserProperties().get("name");
        String json = ResponseMessageFactory.createRoomMemberLeftMessage(id,
                name);
        for (Session s : set) {
            sendMessage(s, json);
        }
    }

    /**
     * Broadcast patient's message over the chat room.
     *
     * @param session
     * @param message
     */
    public void patientChat(Session session, String message) {
        String room_id = (String) session.getUserProperties().get("room_id");
        if (room_id == null) {
            sendMessage(
                    session,
                    ResponseMessageFactory
                            .createSystemMessage("Your should join a physician's chat first."));
        }
        // if eliza
        if (room_id.equals("eliza")) {
            String reply = elizaService.chat(message);
            String json = ResponseMessageFactory
                    .createPhysicianMessage("Eliza", reply);
            sendMessage(session, json);
            return;
        }
        String id = (String) session.getUserProperties().get("id");
        String name = (String) session.getUserProperties().get("name");

        String json = ResponseMessageFactory
                .createPatientMessage(name, message);
        for (Session s : room_map.get(room_id)) {
            if (!s.getUserProperties().get("id").equals(id)) {
                sendMessage(s, json);
            }
        }
    }

    /**
     * This method should be invoked when a patient disconnected.
     *
     * @param session
     */
    public void patientLogout(Session session) {
        String id = (String) session.getUserProperties().get("id");
        // remove patient
        patient_map.remove(id);

        // if this patient has join a chat, leaving at first
        patientLeft(session);
    }

}
