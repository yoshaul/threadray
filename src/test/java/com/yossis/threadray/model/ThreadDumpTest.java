package com.yossis.threadray.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 * Unit tests for {@link ThreadDump}.
 *
 * @author Yossi Shaul
 */
public class ThreadDumpTest {

    @Test
    public void testCopyOfThreadsList() {
        List<ThreadElement> threads = new ArrayList<>();
        threads.add(new ThreadElement("tt"));
        ThreadDump threadDump = new ThreadDump(threads);
        List<ThreadElement> copy = threadDump.getThreads();
        assertEquals(1, copy.size());
        assertNotSame(threads, copy.size());
    }
}
