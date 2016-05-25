package com.yossis.threadray.model.filter;

import com.yossis.threadray.model.ThreadElement;
import com.yossis.threadray.parser.StackElement;
import com.yossis.threadray.util.WildcardMatcher;

import java.util.List;

/**
 * A {@link ThreadFilter} that filters threads by a given wildcards pattern. <br>
 * See {@link WildcardMatcher} for more info.
 *
 * @author Yossi Shaul
 */
public class WildcardFilter implements ThreadFilter {

    private final String pattern;

    public WildcardFilter(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public String getType() {
        return "Pattern";
    }

    @Override
    public boolean test(ThreadElement t) {
        List<StackElement> elements = t.getStackElements();
        if (elements != null) {
            for (StackElement se : elements) {
                if (WildcardMatcher.matches(se.getElement(), pattern)) {
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
