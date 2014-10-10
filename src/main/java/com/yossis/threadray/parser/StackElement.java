package com.yossis.threadray.parser;

/**
 * Represents a stack trace element.
 *
 * @author Yossi Shaul
 */
public class StackElement {

    private final String element;
    /**
     * Optional lock state
     */
    private String state;

    public StackElement(String element) {
        this.element = element;
    }

    public String getElement() {
        return element;
    }

    public void setState(String state) {
        this.state = state;
    }
}
