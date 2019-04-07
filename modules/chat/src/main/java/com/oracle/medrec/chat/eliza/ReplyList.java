package com.oracle.medrec.chat.eliza;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author : xiaojwu.
 *         Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public class ReplyList extends ArrayList<String> {

    public String getRandomReply() {
        Random random = new Random();
        return get(random.nextInt(size()));
    }

}
