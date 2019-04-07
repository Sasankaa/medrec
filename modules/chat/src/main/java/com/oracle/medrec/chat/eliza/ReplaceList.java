package com.oracle.medrec.chat.eliza;

import java.util.ArrayList;

/**
 * @author : xiaojwu.
 *         Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public class ReplaceList extends ArrayList<ReplaceEntity> {

    public String replace(String str) {
        for (ReplaceEntity entity : this) {
            str = entity.replace(str);
        }
        return str;
    }
}
