package com.yossis.threadray.parser;

import com.yossis.threadray.model.ThreadElement;
import org.hamcrest.CoreMatchers;
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
public class ParserTest {

    @Test
    public void validFileParsing() throws URISyntaxException, IOException {
        URL resource = getClass().getResource("/visualvm-oracle-jdk-1.7.0_21.tdump");
        Path resourcePath = Paths.get(resource.toURI());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Files.copy(resourcePath, out);
        List<ThreadElement> threads = new Parser(out.toString()).parse().getThreads();

        assertEquals("Unexpected thread count", 39, threads.size());
        ThreadElement t = threads.get(1);
        assertEquals("pool-7-thread-1", t.getName());
        assertEquals(6, t.getPriority());
        assertEquals(0x000000000dab8800, t.getThreadId());
        assertEquals(0x25f4, t.getNativeId());
        //assertEquals("waiting on condition", t.getDescription());
        assertNotNull(t.getStackElements());
        assertTrue(t.getStackElements().size() > 0);
        List<String> threadLines = Files.readAllLines(resourcePath).subList(13, 25);
        String threadText = String.join("\n", threadLines);
        assertEquals("Unexpected thread text", threadText, t.getThreadDump());
        assertThat(t.getThreadDump(), CoreMatchers.is(threadText));
    }

    private final static String threadText =
            "    \"pool-7-thread-1\" prio=6 tid=0x000000000dab8800 nid=0x25f4 waiting on condition [0x000000000c7bf000]\n" +
                    "       java.lang.Thread.State: WAITING (parking)\n" +
                    "    \tat sun.misc.Unsafe.park(Native Method)\n" +
                    "    \t- parking to wait for  <0x00000000fdb403b0> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)\n" +
                    "    \tat java.util.concurrent.locks.LockSupport.park(LockSupport.java:186)\n" +
                    "    \tat java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2043)\n" +
                    "    \tat java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)\n" +
                    "    \tat java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1068)\n" +
                    "    \tat java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1130)\n" +
                    "    \tat java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:615)\n" +
                    "    \tat java.lang.Thread.run(Thread.java:722)\n" +
                    "\n" +
                    "       Locked ownable synchronizers:\n" +
                    "    \t- None\n";
}
