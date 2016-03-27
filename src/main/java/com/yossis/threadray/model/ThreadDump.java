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
     * List of threads that are part of this thread dump.
     */
    private final List<ThreadElement> threads;

    public ThreadDump(List<ThreadElement> threads) {
        this.threads = threads;
    }

    /**
     * @return List of threads that are part of this thread dump.
     */
    public List<ThreadElement> getThreads() {
        return new ArrayList<>(threads);
    }
}
