package com.oracle.medrec.chat.eliza;

/**
 * Decomp rule entity.
 *
 * @author : xiaojwu.
 *         Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public class Decomp {

    private String pattern;

    private boolean mem;

    private ReplyList replyList;

    /**
     * Initialize the decompList rule
     */
    Decomp(String pattern, boolean mem, ReplyList replyList) {
        this.pattern = pattern;
        this.mem = mem;
        this.replyList = replyList;
    }

    public String getPattern() {
        return pattern;
    }

    public boolean isMem() {
        return mem;
    }

    public ReplyList getReplyList() {
        return replyList;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("[Decomp] pattern=").append(pattern);
        builder.append(", mem=").append(mem ? "true" : "false");
        if (replyList != null && replyList.size() > 0) {
            for (String reply : replyList) {
                builder.append(".\n[reply] ").append(reply);
            }
        }
        return builder.toString();
    }


}

