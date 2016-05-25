package com.yossis.threadray.ui.javafx.view.filter;

import com.yossis.threadray.model.ThreadElement;
import com.yossis.threadray.model.filter.ThreadFilter;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * A decorator {@link ThreadFilter} to enable or disabled the wrapped filter.
 *
 * @author Yossi Shaul
 */
public class OnOffThreadFilter implements ThreadFilter {

    private final ThreadFilter filter;
    private BooleanProperty enabled = new SimpleBooleanProperty(true);

    public OnOffThreadFilter(ThreadFilter filter) {
        this.filter = filter;
    }

    public BooleanProperty enabledProperty() {
        return enabled;
    }

    public boolean isEnabled() {
        return enabled.get();
    }

    public void setEnabled(boolean enabled) {
        this.enabled.setValue(enabled);
    }

    @Override
    public String getType() {
        return filter.getType();
    }

    @Override
    public boolean test(ThreadElement t) {
        return enabled.get() && filter.test(t);
    }

    @Override
    public String toString() {
        return filter.toString();
    }
}
