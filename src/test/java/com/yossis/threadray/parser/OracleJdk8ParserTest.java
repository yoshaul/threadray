package com.yossis.threadray.parser;

import com.yossis.threadray.model.ThreadElement;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
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
public class OracleJdk8ParserTest {

    private List<ThreadElement> threads;
    private Path resourcePath;

    @Before
    public void setup() throws IOException, URISyntaxException {
        URL resource = getClass().getResource("/jstack-oracle-jdk-1.8.0_60.tdump");
        resourcePath = Paths.get(resource.toURI());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Files.copy(resourcePath, out);
        threads = new Parser(out.toString()).parse().getThreads();
        assertEquals("Unexpected thread count", 29, threads.size());
    }

    @Test
    public void verifyNormalThread() throws URISyntaxException, IOException {
        ThreadElement t = threads.get(0);
        assertEquals("Prism Font Disposer", t.getName());
        assertEquals(22, t.getNumber());
        assertTrue(t.isDaemon());
        assertEquals(10, t.getPriority());
        assertEquals(2, t.getOsPriority());
        assertEquals(0x000000001f252000, t.getThreadId());
        assertEquals(0x163c, t.getNativeId());
        assertEquals("WAITING (on object monitor)", t.getState());
        //assertEquals(" in Object.wait() [0x0000000026a4f000]", t.getDescription());
        assertNotNull(t.getStackElements());
        assertEquals(t.getStackElements().size(), 5);
        List<String> threadLines = Files.readAllLines(resourcePath).subList(3, 13);
        String threadText = String.join("\n", threadLines);
        assertEquals("Unexpected thread text", threadText, t.getThreadDump());
        assertThat(t.getThreadDump(), CoreMatchers.is(threadText));
    }

    @Test
    public void verifyGCThread() throws URISyntaxException, IOException {
        ThreadElement t = threads.get(20);
        assertEquals("GC task thread#0 (ParallelGC)", t.getName());
        assertEquals(0, t.getNumber());
        assertFalse(t.isDaemon());
        assertEquals(0, t.getPriority());
        assertEquals(0, t.getOsPriority());
        assertEquals(0x00000000022e7800, t.getThreadId());
        assertEquals(0x1580, t.getNativeId());
        assertNull(t.getState());
        assertEquals("runnable", t.getDescription());
        assertNull(t.getStackElements());
        // assertEquals(t.getStackElements().size(), 0);
        String threadText = "\"GC task thread#0 (ParallelGC)\" os_prio=0 tid=0x00000000022e7800 nid=0x1580 runnable \n";
        assertEquals("Unexpected thread text", threadText, t.getThreadDump());
        assertThat(t.getThreadDump(), CoreMatchers.is(threadText));
    }
}
