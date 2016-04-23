package com.yossis.threadray.ui.javafx.view.filter;

import com.yossis.threadray.model.filter.ThreadFilter;
import com.yossis.threadray.ui.javafx.model.ThreadElementFx;

import java.util.List;
import java.util.function.Predicate;

/**
 * A predicate that wraps list of {@link ThreadFilter} and accepts a thread if all the filters accept it.
 *
 * @author Yossi Shaul
 */
public class ThreadFiltersPredicate implements Predicate<ThreadElementFx> {

    private final List<ThreadFilter> filters;

    public ThreadFiltersPredicate(List<ThreadFilter> filters) {
        this.filters = filters;
    }

    @Override
    public boolean test(ThreadElementFx threadElementFx) {
        if (filters != null) {
            for (ThreadFilter filter : filters) {
                if (filter.filter(threadElementFx.getThreadElement())) {
                    return false;
                }
            }
        }
        return true;
    }

    public List<ThreadFilter> getFilters() {
        return filters;
    }
}
