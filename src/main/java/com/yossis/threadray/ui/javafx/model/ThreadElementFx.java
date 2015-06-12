package com.yossis.threadray.ui.javafx.model;

import com.yossis.threadray.model.ThreadElement;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * An observable {@link com.yossis.threadray.model.ThreadElement}.
 *
 * @author Yossi Shaul
 */
public class ThreadElementFx {
    private final ThreadElement threadElement;
    private final StringProperty threadName;
    private final StringProperty threadDump;
    private final SimpleLongProperty threadId;

    public ThreadElementFx(ThreadElement t) {
        threadElement = t;
        this.threadName = new SimpleStringProperty(t.getName());
        this.threadDump = new SimpleStringProperty(t.getThreadDump());
        this.threadId = new SimpleLongProperty(t.getThreadId());
    }

    public ThreadElement getThreadElement() {
        return threadElement;
    }

    public StringProperty getThreadName() {
        return threadName;
    }

    public String getThreadDump() {
        return threadDump.get();
    }

    public StringProperty threadDumpProperty() {
        return threadDump;
    }

    public long getThreadId() {
        return threadId.get();
    }
}
