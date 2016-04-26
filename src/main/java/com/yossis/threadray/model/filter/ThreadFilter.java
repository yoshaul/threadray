package com.yossis.threadray.model.filter;

import com.yossis.threadray.model.ThreadElement;

/**
 * A thread filter filters a thread from a thread dump based on criteria.
 *
 * @author Yossi Shaul
 */
public interface ThreadFilter {

    /**
     * @return The type of the filter
     */
    String getType();

    /**
     * @param t The thread to inspect
     * @return True if the given thread should be filtered by this filter
     */
    boolean test(ThreadElement t);
}
