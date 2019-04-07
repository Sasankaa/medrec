package com.oracle.medrec.chat;

/**
 * The values of attribute in request message from client end point.
 *
 * @author Xiaojun Wu. <br>
 *         Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public interface RequestMessageType {

    final static String TYPE_LIST = "physicians";
    final static String TYPE_CHAT = "chat";
    final static String TYPE_JOIN = "join";
    final static String TYPE_LEAVE = "leave";
}
