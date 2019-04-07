package com.oracle.medrec.chat.eliza;

/**
 * Replace entity. E.g. src is you, dest is I.
 *
 * @author : xiaojwu.
 *         Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public class ReplaceEntity {

    private String src;

    private String dest;

    private Type type;

    enum Type {PRE, POST}

    public ReplaceEntity(String src, String dest, Type type) {
        this.src = src;
        this.dest = dest;
        this.type = type;
    }

    public String replace(String str) {
        return str.replaceAll(src, dest);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("[").append(type.name()).append("] src=").append(src);
        builder.append(", dest=").append(dest);
        return builder.toString();
    }

}
