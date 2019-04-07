package com.oracle.medrec.chat.eliza;

import javax.inject.Inject;

/**
 * Eliza Service the portal of chat.
 *
 * @author : xiaojwu.
 *         Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public class ElizaService {

    @Inject
    private InputProcessor inputProcessor;

    @Inject
    private ElizaResource resource;

    /**
     * start a new chat.
     *
     * @return Hello
     */
    public String newChat() {
        return resource.initial;
    }

    /**
     * chat with Eliza.
     *
     * @param input user's input
     * @return Eliza's reply
     */
    public String chat(String input) {
        // mock the thinking time of Eliza
        try {
            Thread.currentThread().sleep(2000);
        } catch (InterruptedException e) {
            // do nothing
        }
        return inputProcessor.processInput(input);
    }


}
