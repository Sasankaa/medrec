package com.oracle.medrec.chat.eliza;

import java.util.List;

/**
 * @author : xiaojwu.
 *         Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
public class Key implements Comparable {
    /**
     * The key itself
     */
    private String key;

    /**
     * The numerical rank
     */
    private int rank;

    /**
     * The list of decompositions
     */
    private List<Decomp> decompList;

    /**
     * Initialize the key.
     *
     * @param key
     * @param rank
     * @param decompList
     */
    public Key(String key, int rank, List<Decomp> decompList) {
        this.key = key;
        this.rank = rank;
        this.decompList = decompList;
    }

    /**
     * Another initialization for gotoKey.
     */
    public Key() {
        key = null;
        rank = 0;
        decompList = null;
    }

    public String getKey() {
        return key;
    }

    public int getRank() {
        return rank;
    }

    public List<Decomp> getDecompList() {
        return decompList;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("[key] key=").append(key);
        builder.append(", rank=").append(rank);
        if (decompList != null && decompList.size() > 0) {
            for (Decomp decomp : decompList) {
                builder.append(".\n").append(decomp);
            }
        }
        return builder.toString();
    }


    @Override
    public int compareTo(Object o) {
        return rank - ((Key) o).rank;
    }
}
