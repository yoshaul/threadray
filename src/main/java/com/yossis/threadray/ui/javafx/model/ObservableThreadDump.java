package com.yossis.threadray.ui.javafx.model;

import com.yossis.threadray.model.ThreadDump;
import com.yossis.threadray.model.ThreadElement;
import com.yossis.threadray.model.filter.PatternFilter;
import com.yossis.threadray.model.filter.ThreadFilter;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/**
 * An observable {@link ThreadDump} that supports ui binding and filtering.
 *
 * @author Yossi Shaul
 */
public class ObservableThreadDump {

    private final ThreadDump td;
    private final ObservableList<ThreadElementFx> threads = FXCollections.observableArrayList();
    private final ListProperty<ThreadElementFx> threadsProperty;
    private final IntegerProperty threadsCount;
    private final ObservableList<ThreadFilter> filters = FXCollections.observableArrayList();
    // private final ThreadFiltersPredicate filteredThreads;

    public ObservableThreadDump(ThreadDump td) {
        this.td = td;
        td.getThreads().stream().forEach(t -> threads.add(new ThreadElementFx(t)));
        threadsProperty = new SimpleListProperty<>(threads);
        filters.addListener((ListChangeListener<ThreadFilter>) c -> doFilter());
        threadsCount = new SimpleIntegerProperty(threads.size());
    }

    public String getText() {
        return td.getText();
    }

    public ListProperty<ThreadElementFx> threadsProperty() {
        return threadsProperty;
    }

    public IntegerProperty threadsCountProperty() {
        return threadsCount;
    }

    public ObservableList<ThreadFilter> getFilters() {
        return filters;
    }

    public void addFilter(PatternFilter filter) {
        filters.add(filter);
        doFilter();
    }

    private void doFilter() {
        threads.clear();
        td.getThreads().stream().filter(this::accepts).forEach(t -> threads.add(new ThreadElementFx(t)));
        threadsCount.set(threads.size());
    }

    private boolean accepts(ThreadElement t) {
        return filters == null || filters.stream().allMatch(f -> f.test(t));
    }

    // public ThreadFiltersPredicate getFilteredThreads() {
    //     return filteredThreads;
    // }
}
