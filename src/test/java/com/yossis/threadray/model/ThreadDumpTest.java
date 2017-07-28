package com.yossis.threadray.model;

import com.yossis.threadray.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    private ThreadDump td;

    @Before
    public void setup() throws IOException, URISyntaxException {
        URL resource = getClass().getResource("/visualvm-oracle-jdk-1.7.0_21.tdump");
        Path resourcePath = Paths.get(resource.toURI());
        td = new Parser(resourcePath).parse();
    }

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
    public void countStringOccurrences() {
        assertEquals(6, td.countMatches("LinkedBlockingQueue"));
    }

    @Test
    public void countNotFound() {
        assertEquals(0, td.countMatches("nope"));
    }
}
