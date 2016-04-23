package com.yossis.threadray.model.filter;

import com.yossis.threadray.model.ThreadElement;
import com.yossis.threadray.parser.StackElement;

import java.util.List;

/**
 * A {@link ThreadFilter} that filters threads by a given pattern.
 *
 * @author Yossi Shaul
 */
public class PatternFilter implements ThreadFilter {

    private final String pattern;

    public PatternFilter(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public String getType() {
        return "Pattern";
    }

    @Override
    public boolean filter(ThreadElement t) {
        List<StackElement> elements = t.getStackElements();
        if (elements != null) {
            for (StackElement se : elements) {
                if (se.getElement().contains(pattern)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return pattern;
    }
}
