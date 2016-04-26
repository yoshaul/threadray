package com.yossis.threadray.ui.javafx.view.filter;

import com.yossis.threadray.model.filter.ThreadFilter;
import com.yossis.threadray.ui.javafx.model.ThreadElementFx;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.function.Predicate;

/**
 * A predicate that wraps list of {@link ThreadFilter} and accepts a thread if all the filters accept it.
 *
 * @author Yossi Shaul
 */
public class ThreadFiltersPredicate implements Predicate<ThreadElementFx> {

    private final ObservableList<ThreadFilter> filters = FXCollections.observableArrayList();

    @Override
    public boolean test(ThreadElementFx threadElementFx) {
        for (ThreadFilter filter : filters) {
            if (filter.test(threadElementFx.getThreadElement())) {
                return false;
            }
        }
        return true;
    }

    public ObservableList<ThreadFilter> getFilters() {
        return filters;
    }
}
