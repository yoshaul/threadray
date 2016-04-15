package com.yossis.threadray.parser;

import com.yossis.threadray.model.ThreadElement;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.*;

/**
 * {@link Parser} unit tests.
 *
 * @author Yossi Shaul
 */
public class OracleJdk7ParserTest {
    private Path resourcePath;
    private List<ThreadElement> threads;

    @Before
    public void setup() throws IOException, URISyntaxException {
        URL resource = getClass().getResource("/visualvm-oracle-jdk-1.7.0_21.tdump");
        resourcePath = Paths.get(resource.toURI());
        threads = new Parser(resourcePath).parse().getThreads();
        assertEquals("Unexpected thread count", 39, threads.size());
    }

    @Test
    public void verifyNormalThread() throws URISyntaxException, IOException {
        ThreadElement t = threads.get(1);
        assertEquals("pool-7-thread-1", t.getName());
        assertEquals(6, t.getPriority());
        assertEquals(0x000000000dab8800, t.getThreadId());
        assertEquals(0x25f4, t.getNativeId());
        assertEquals("WAITING (parking)", t.getState());
        assertEquals(Thread.State.WAITING, t.getThreadState().getState());
        assertEquals("parking", t.getThreadState().getDescription());
        //assertEquals("waiting on condition", t.getDescription());
        assertNotNull(t.getStackElements());
        assertTrue(t.getStackElements().size() > 0);
        List<String> threadLines = Files.readAllLines(resourcePath).subList(13, 25);
        String threadText = String.join("\n", threadLines);
        assertEquals("Unexpected thread text", threadText, t.getThreadDump());
        assertThat(t.getThreadDump(), CoreMatchers.is(threadText));
    }

    @Test
    public void verifyCompilerThread() throws URISyntaxException, IOException {
        ThreadElement t = threads.get(23);
        assertEquals("C2 CompilerThread1", t.getName());
        assertTrue(t.isDaemon());
        assertEquals(10, t.getPriority());
        assertEquals(0x0000000008aed800, t.getThreadId());
        assertEquals(0x13a0, t.getNativeId());
        assertEquals("RUNNABLE", t.getState());
        assertEquals(Thread.State.RUNNABLE, t.getThreadState().getState());
        assertEquals("", t.getThreadState().getDescription());
        //assertEquals("waiting on condition", t.getDescription());
        assertNull(t.getStackElements());
        String threadText = "\"C2 CompilerThread1\" daemon prio=10 tid=0x0000000008aed800 nid=0x13a0 waiting on condition [0x0000000000000000]\n" +
                "   java.lang.Thread.State: RUNNABLE\n";
        assertEquals("Unexpected thread text", threadText, t.getThreadDump());
        assertThat(t.getThreadDump(), CoreMatchers.is(threadText));
    }
}
