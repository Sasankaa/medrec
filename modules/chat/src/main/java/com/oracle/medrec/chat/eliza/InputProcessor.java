package com.oracle.medrec.chat.eliza;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * In charge of interpreting user's input.
 *
 * @author : xiaojwu.
 *         Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@ApplicationScoped
public class InputProcessor {

    private static Logger LOGGER = Logger.getLogger(InputProcessor.class.getName());

    @Inject
    private ElizaResource resource;

    public InputProcessor() {

    }

    public InputProcessor(ElizaResource resource) {
        this.resource = resource;
    }

    /**
     * Interpret input
     *
     * @param inputString
     * @return Eliza's reply
     */
    public String processInput(String inputString) {
        String reply;
        //  to lower case.
        inputString = inputString.toLowerCase();

        // if user say quit so, bye
        for (String quit : resource.quitList) {
            if (inputString.startsWith(quit)) {
                return resource.bye;
            }
        }
        // filter symbols
        inputString = translate(inputString, "@#$%^&*()_-+=~`{[}]|:<>\\\"",
                                            "                         ");
        // divide sentences
        inputString = translate(inputString, ",;?!",
                                             "....");

        //  Break apart sentences, and do each separately.
        for (String sentence : inputString.split("\\.")) {
            // find reply sentence according to user's sentence.
            reply = sentence(sentence);
            if (reply != null) {
                return reply;
            }
        }
        //  No appropriate reply, reply with noapptrep.
        Key key = resource.keyMap.get("noapptrep");
        if (key != null) {
            reply = decompose(key, inputString);
            if (reply != null) {
                return reply;
            }
        }
        //  No noapptrep, just say so.
        return "I've got aphasia.";
    }

    /**
     * Translate special characters.
     *
     * @param str
     * @param src
     * @param dest
     * @return
     */
    private String translate(String str, String src, String dest) {
        if (src.length() != dest.length()) {
            LOGGER.warning("ignore translate because of not equal params' length.");
        } else {
            for (int i = 0; i < src.length(); i++) {
                str = str.replace(src.charAt(i), dest.charAt(i));
            }
        }
        return str;
    }

    /**
     * Interpret one sentence.
     *
     * @param inputString
     * @return
     */
    private String sentence(String inputString) {
        // need unified before decomp. E.g. you are = you're
        inputString = resource.preList.replace(inputString);

        // check bye again in split sentence
        if (resource.quitList.contains(inputString)) {
            return resource.bye;
        }

        // scan and rank key words of sentence
        List<Key> keyList = buildRankKeyList(inputString);

        // decompose the sentence with key ordered by rank.
        for (Key key : keyList) {
            String reply = decompose(key, inputString);
            // if match the regex, the reply sentence is available
            if (reply != null) {
                return reply;
            }
        }
        return null;
    }

    /**
     * Decompose the sentence with relevant Decomp Entities.
     *
     * @param key
     * @param inputString
     * @return
     */
    private String decompose(Key key, String inputString) {
        for (Decomp decomp : key.getDecompList()) {
            // handle synons,
            // if the regex of decomp has synons, it needs to update its regex.
            CurrentPattern pattern = new CurrentPattern(decomp.getPattern());
            pattern.parse(inputString);

            // match regex
            if (inputString.matches(pattern.pattern)) {
                // if match, assemble the reply sentence.
                String reply = assemble(pattern, decomp.getReplyList().getRandomReply(), inputString);
                if (reply != null) {
                    return reply;
                }
            }
        }
        return null;
    }

    /**
     * Pattern. If the regex of decomp has synons, it needs to update its regex.
     */
    class CurrentPattern {
        String pattern;
        // preserve the original synon
        private List<String> synonList;

        CurrentPattern(String pattern) {
            this.pattern = pattern;
            synonList = new ArrayList(0);
        }

        /**
         * Parse input, insofar as synons are concerned.
         *
         * @param inputString
         */
        void parse(String inputString) {
            int a = pattern.indexOf("@");
            while (a != -1) {
                int b = pattern.indexOf(" ", a);
                if (b == -1) {
                    b = pattern.length();
                }
                String synonKey = pattern.substring(a + 1, b);
                String[] synons = resource.synonMap.get(synonKey);
                boolean tag = true;
                if (synons != null) {
                    for (String synon : synons) {
                        if (inputString.indexOf(synon) != -1) {
                            pattern = pattern.replaceAll("@" + synonKey, synon);
                            synonList.add(synon);
                            tag = false;
                            break;
                        }
                    }
                }
                // if no synons matching, remove the '@'
                if (tag) {
                    pattern = pattern.replaceAll("@" + synonKey, synonKey);
                }
                a = pattern.indexOf("@");
            }
        }

    }

    /**
     * Assemble Eliza's reply according with reply rule.
     *
     * @param pattern
     * @param replyRule
     * @param inputString
     * @return
     */
    private String assemble(CurrentPattern pattern, String replyRule, String inputString) {
        // if 'goto' another another key
        int a = replyRule.indexOf("goto ");
        if (a != -1) {
            String gotoKeyName = replyRule.substring(a + 5);
            Key gotoKey = resource.keyMap.get(gotoKeyName);
            if (gotoKey != null) {
                // redecomp goto key
                return decompose(gotoKey, inputString);
            }
        }
        // preserve original rule
        String reply = replyRule;
        a = replyRule.indexOf("(");
        // if reply rule has '(num}' means needing using user's input as a part of reply
        if (a != -1) {
            // compart the input sentence
            List<String> parts = splitInputByRegex(inputString, pattern.pattern);
            // use the phrase from input as a part of reply
            do {
                int b = replyRule.indexOf(")", a);
                if (b == -1) {
                    LOGGER.warning("')' is expected. Illegal reply rule: " + replyRule);
                    return null;
                }
                String snum = replyRule.substring(a + 1, b);
                int num = 0;
                try {
                    num = Integer.valueOf(snum);
                } catch (NumberFormatException e) {
                    LOGGER.warning("Number is expected in (). Illegal reply rule: " + replyRule);
                    return null;
                }
                // replace special word because of quoting. E.g you -> I
                String str = resource.postList.replace(parts.get(num).trim());
                reply = reply.replaceAll("\\(" + snum + "\\)", str);
                a = reply.indexOf("(");
            } while (a != -1);
        }
        // if regex of decomp has synons, it need to return to the original words of input.
        a = replyRule.indexOf("@");
        while (a != -1) {
            String snum = replyRule.substring(a + 1, a + 2);
            int num = 0;
            try {
                num = Integer.valueOf(snum);
            } catch (NumberFormatException e) {
                LOGGER.warning("Number is expected after @. Illegal reply rule: " + replyRule);
                return null;
            }
            if (pattern.synonList.size() - 1 < num) {
                LOGGER.warning("Number after @ is out of the input synons size. Illegal reply rule: " + replyRule);
                return null;
            }
            reply = reply.replaceAll("@" + snum, pattern.synonList.get(num));
            a = reply.indexOf("@");
        }
        return reply;
    }

    /**
     * Create a Key list order by Key's rank. Any Key if sentence contains its pattern will be added into the list.
     * Rank '0' is on the top.
     *
     * @param inputString
     * @return
     */
    private List<Key> buildRankKeyList(String inputString) {
        List<Key> keyList = new ArrayList();
        for (String k : resource.keyMap.keySet()) {
            if (inputString.contains(k)) {
                keyList.add(resource.keyMap.get(k));
            }
        }
//        resource.keyMap.keySet().stream()
//                .filter(k -> inputString.contains(k))
//                .forEach(k -> keyList.add(resource.keyMap.get(k)));
        Collections.sort(keyList);
        return keyList;
    }

    /**
     * Split input by pattern.
     *
     * @param input
     * @param regex
     * @return
     */
    private List<String> splitInputByRegex(String input, String regex) {
        String[] keys = regex.split("\\(\\.\\*\\)");
        String[] temp = null;
        List<String> list = new ArrayList();
        if (keys.length == 0) {
            list.add(input);
            return list;
        }
        for (String k : keys) {
            if (!k.equals("")) {
                LOGGER.finer("k[" + k + "]");
                if (temp == null) {
                    temp = input.split(k, 2);
                } else {
                    temp = temp[1].split(k, 2);
                }
                list.add(temp[0]);
            }
        }
        if (temp != null) {
            list.add(temp[1]);
        }
        if (LOGGER.isLoggable(Level.FINE)) {
            for (String p : list) {
                LOGGER.finer("part: " + p);
            }
//            list.forEach(p -> LOGGER.finer("part: " + p));
        }
        return list;
    }

}
