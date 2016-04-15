package com.yossis.threadray.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Object model of a thread dump.
 *
 * @author Yossi Shaul
 */
public class ThreadDump {
    /**
     * The raw thread dump text.
     */
    private final String text;

    /**
     * List of threads that are part of this thread dump.
     */
    private final List<ThreadElement> threads;

    public ThreadDump(String text, List<ThreadElement> threads) {
        this.text = text;
        this.threads = threads;
    }

    /**
     * @return List of threads that are part of this thread dump.
     */
    public List<ThreadElement> getThreads() {
        return new ArrayList<>(threads);
    }

    /**
     * @return The raw thread dump text
     */
    public String getText() {
        return text;
    }

    public int countMatches(String text) {
        return 0;
    }
}
