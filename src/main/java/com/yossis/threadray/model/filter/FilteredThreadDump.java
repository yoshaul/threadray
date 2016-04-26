package com.yossis.threadray.model.filter;

import com.yossis.threadray.model.ThreadDump;
import com.yossis.threadray.model.ThreadElement;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A {@link ThreadDump} with optional filters. <p>
 * A filtered thread dump wraps an un-filtered thread dump and returns data after filtering.
 *
 * @author Yossi Shaul
 */
public class FilteredThreadDump {

    private final ThreadDump unfiltered;
    private List<ThreadFilter> filters;

    public FilteredThreadDump(ThreadDump threadDump) {
        this.unfiltered = threadDump;
    }

    public String getText() {
        return unfiltered.getText();
    }

    public List<ThreadElement> getThreads() {
        return unfiltered.getThreads().stream().filter(this::accepts).collect(Collectors.toList());
    }

    private boolean accepts(ThreadElement t) {
        return filters == null || filters.stream().allMatch(f -> f.test(t));
    }

    public void addFilter(PatternFilter filter) {
        if (filters == null) {
            filters = new ArrayList<>();
        }
        filters.add(filter);
    }
}
