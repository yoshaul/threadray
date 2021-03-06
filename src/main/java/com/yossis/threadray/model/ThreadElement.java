package com.yossis.threadray.model;

import com.yossis.threadray.parser.StackElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single thread in the thread dump.
 * Example of a thread:
 * <pre>
 * "Keep-Alive-SocketCleaner" daemon prio=8 tid=0x000000000dabb800 nid=0xd44 in Object.wait() [0x000000001051f000]
 *   java.lang.Thread.State: WAITING (on object monitor)
 *   at java.lang.Object.wait(Native Method)
 *   - waiting on <0x00000000f0d8bb48> (a sun.net.www.http.KeepAliveStreamCleaner)
 *   at sun.net.www.http.KeepAliveStreamCleaner.run(KeepAliveStreamCleaner.java:101)
 *   - locked <0x00000000f0d8bb48> (a sun.net.www.http.KeepAliveStreamCleaner)
 *   at java.lang.Thread.run(Thread.java:722)
 * </pre>
 *
 * @author Yossi Shaul
 */
public class ThreadElement {
    private final String name;
    private int number;
    private int priority;
    private long threadId;
    private int nativeId;
    private String description;
    private boolean daemon;
    /**
     * The thread state. See {@link Thread.State}
     */
    private String state;
    private State threadState;
    private List<StackElement> stackElements;
    /**
     * The plain text from the stack dump representing the current thread
     */
    private String threadDump;
    private int osPriority;

    public ThreadElement(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public long getThreadId() {
        return threadId;
    }

    public void setThreadId(long threadId) {
        this.threadId = threadId;
    }

    public int getNativeId() {
        return nativeId;
    }

    public void setNativeId(int nativeId) {
        this.nativeId = nativeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDaemon() {
        return daemon;
    }

    public void setDaemon(boolean daemon) {
        this.daemon = daemon;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void addStackElement(StackElement stackElement) {
        if (stackElements == null) {
            stackElements = new ArrayList<>();
        }
        stackElements.add(stackElement);
    }

    public List<StackElement> getStackElements() {
        return stackElements;
    }

    public void setThreadDump(String threadDump) {
        this.threadDump = threadDump;
    }

    public String getThreadDump() {
        return threadDump;
    }

    public int getOsPriority() {
        return osPriority;
    }

    public void setOsPriority(int osPriority) {
        this.osPriority = osPriority;
    }

    public State getThreadState() {
        return threadState;
    }

    public void setThreadState(State threadState) {
        this.threadState = threadState;
    }

    public static class State {
        private final Thread.State state;
        private final String description;

        public State(Thread.State state, String description) {
            this.state = state;
            this.description = description;
        }

        public Thread.State getState() {
            return state;
        }

        public String getDescription() {
            return description;
        }
    }

}
