package com.oracle.medrec.chat.eliza;

import com.oracle.medrec.chat.eliza.ReplaceEntity.Type;

import javax.enterprise.context.ApplicationScoped;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handle language resource.
 *
 * @author : xiaojwu.
 *         Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@ApplicationScoped
public class ElizaResource {

    private static Logger LOGGER = Logger.getLogger(ElizaResource.class.getName());

    Map<String, Key> keyMap = new HashMap<String, Key>();

    Map<String, String[]> synonMap = new HashMap<String, String[]>();

    ReplaceList preList = new ReplaceList();

    ReplaceList postList = new ReplaceList();

    List<String> quitList = new ArrayList<String>();

    String initial = "Hello";

    String bye = "Bye";

    private List<Decomp> lastDecomp = null;

    private ReplyList lastReply = null;

    public ElizaResource() {
        analyzeResource();
    }

    /**
     * Read and parse language resource while initialize all relevant components.
     */
//    todo @PostConstruct
    public void analyzeResource() {
        try (InputStream in =
                     ElizaResource.class.getResourceAsStream("/language.resource");
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line = reader.readLine();
            while (line != null) {
                analyzeALine(line.trim());
                line = reader.readLine();
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "There was a problem reading the script file.", e);
        }
        lastDecomp = null;
        lastReply = null;
    }

    private void analyzeALine(String line) {
        LOGGER.finest("Analyze a line: " + line);
        String temp;
        if (line.startsWith("#") || line.length() == 0) {
            // comment line
            return;
        } else if (line.startsWith("reply:")) {
            //  reply sentence by regular expression of decomp
            if (lastReply == null) {
                LOGGER.severe("Error: no decomp line before.");
                return;
            }
            temp = line.substring(6).trim();
            LOGGER.finest("[relay] " + temp);
            lastReply.add(temp);
        } else if (line.startsWith("decomp:")) {
            // regular expression conforming to its key.
            if (lastDecomp == null) {
                LOGGER.severe("Error: no key line before.");
                return;
            }
            lastReply = new ReplyList();
            Decomp decomp = new Decomp(line.substring(7).trim(), false, lastReply);
            LOGGER.finest(decomp.toString());
            lastDecomp.add(decomp);
        } else if (line.startsWith("key:")) {
            // key word
            lastDecomp = new ArrayList();
            lastReply = null;
            String[] strs = line.split(" ");
            Key key = new Key(strs[1], Integer.parseInt(strs[2]), lastDecomp);
            LOGGER.finest(key.toString());
            keyMap.put(key.getKey(), key);
        } else if (line.startsWith("synon:")) {
            // word array of identical meaning
            String[] synons = line.substring(6).trim().split(" ");
            if (LOGGER.isLoggable(Level.FINEST)) {
                for (String synon : synons) {
                    LOGGER.finest("[synon] " + synon);
                }
            }
            synonMap.put(synons[0], synons);
        } else if (line.matches("pre: (.*) (.*)")) {
            // need unified before decomp. E.g. you are = you're
            String[] strs = line.substring(4).trim().split(" ", 2);
            ReplaceEntity entity = new ReplaceEntity(strs[0], strs[1], Type.PRE);
            preList.add(entity);
        } else if (line.matches("post: (.*) (.*)")) {
            // need replaced when quoting user's words
            String[] strs = line.substring(5).trim().split(" ", 2);
            ReplaceEntity entity = new ReplaceEntity(strs[0], strs[1], Type.POST);
            postList.add(entity);
        } else if (line.startsWith("initial:")) {
            // hello at the very beginning
            initial = line.substring(8).trim();
            LOGGER.finest("[initial] " + initial);
        } else if (line.startsWith("bye:")) {
            // goodbye finally
            bye = line.substring(4).trim();
            LOGGER.finest("[bye] " + bye);
        } else if (line.startsWith("quit:")) {
            // say goodbye if user said so
            temp = line.substring(5).trim();
            LOGGER.finest("[quit] " + temp);
            quitList.add(temp);
        } else {
            LOGGER.warning("Unrecognized input: " + line);
        }
    }

}
