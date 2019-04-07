package com.oracle.medrec.chat;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.websocket.Session;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

/**
 * JSON response message factory.
 *
 * @author Xiaojun Wu. <br>
 *         Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public class ResponseMessageFactory {

    private final static String TYPE_SYSTEM = "system";
    private final static String TYPE_CHAT = "chat";
    private final static String TYPE_ALL_ONLINE_PHYSICIANS = "all_online_physicians";
    private final static String TYPE_ROOM_CLOSE = "room_close";
    private final static String TYPE_ROOM_ALL_MEMBER = "all_room_member";
    private final static String TYPE_ROOM_UPDATE = "room_member_update";

    private final static String ROLE_PATIENT = "patient";
    private final static String ROLE_PHYSICIAN = "physician";

    private final static String EVENT_NEW_MEMBER = "join";
    private final static String EVENT_MEMBER_LEAVE = "leave";

    private final static SimpleDateFormat dateFormat = new SimpleDateFormat(
            "kk:mm:ss");

    public static String createSystemMessage(String message) {
        JsonObject model = Json.createObjectBuilder().add("type", TYPE_SYSTEM)
                .add("time", dateFormat.format(new Date()))
                .add("message", message).build();
        return model.toString();
    }

    public static String createPatientMessage(String name, String message) {
        JsonObject model = Json.createObjectBuilder().add("type", TYPE_CHAT)
                .add("role", ROLE_PATIENT)
                .add("time", dateFormat.format(new Date()))
                .add("speaker", name).add("message", message).build();
        return model.toString();
    }

    public static String createPhysicianMessage(String name, String message) {
        JsonObject model = Json.createObjectBuilder().add("type", TYPE_CHAT)
                .add("role", ROLE_PHYSICIAN)
                .add("time", dateFormat.format(new Date()))
                .add("speaker", name).add("message", message).build();
        return model.toString();
    }

    public static String createAllOnlinePhysicianMessage(
            Collection<Session> physicians) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        String id, name;
        for (Session session : physicians) {
            id = (String) session.getUserProperties().get("id");
            name = (String) session.getUserProperties().get("name");
            arrayBuilder.add(Json.createObjectBuilder().add("id", id)
                    .add("name", name));
        }

        JsonObject model = Json.createObjectBuilder()
                .add("type", TYPE_ALL_ONLINE_PHYSICIANS)
                .add("physicians", arrayBuilder.build()).build();
        return model.toString();
    }

    public static String createAllRoomMemberMessage(Set<Session> sessions) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        String id, name;
        for (Session session : sessions) {
            id = (String) session.getUserProperties().get("id");
            name = (String) session.getUserProperties().get("name");
            arrayBuilder.add(Json.createObjectBuilder().add("id", id)
                    .add("name", name));
        }

        JsonObject model = Json.createObjectBuilder()
                .add("type", TYPE_ROOM_ALL_MEMBER)
                .add("members", arrayBuilder.build()).build();
        return model.toString();
    }

    public static String createNewRoomMemberMessage(String id, String name) {
        JsonObject model = Json.createObjectBuilder()
                .add("type", TYPE_ROOM_UPDATE).add("event", EVENT_NEW_MEMBER)
                .add("id", id).add("name", name).build();
        return model.toString();
    }

    public static String createRoomMemberLeftMessage(String id, String name) {
        JsonObject model = Json.createObjectBuilder()
                .add("type", TYPE_ROOM_UPDATE).add("event", EVENT_MEMBER_LEAVE)
                .add("id", id).add("name", name).build();
        return model.toString();
    }

    public static String createRoomCloseMessage(String name) {
        JsonObject model = Json.createObjectBuilder()
                .add("type", TYPE_ROOM_CLOSE).add("name", name).build();
        return model.toString();
    }

}
