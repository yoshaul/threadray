package com.yossis.threadray.model;

import com.yossis.threadray.parser.Parser;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
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
        ThreadDump threadDump = new ThreadDump("dummy", threads);
        List<ThreadElement> copy = threadDump.getThreads();
        assertEquals(1, copy.size());
        assertNotSame(threads, copy.size());
    }

    @Test
    public void textCountMatchesNoMatches() throws IOException, URISyntaxException {
        ThreadDump dump = new Parser(getClass().getResource("/jstack-oracle-jdk-1.8.0_60.tdump").toURI().getPath()).parse();
        assertEquals(0, dump.countMatches("no_such_dump"));
    }

    // @Test
    public void textCountMatches() throws IOException, URISyntaxException {
        ThreadDump dump = new Parser(getClass().getResource("/jstack-oracle-jdk-1.8.0_60.tdump").toURI().getPath()).parse();
        assertEquals(7, dump.countMatches("at java.lang.Thread.run(Thread.java:745)"));
    }
}
