package com.yossis.threadray.parser;

import com.yossis.threadray.model.ThreadElement;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
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
        URL resource = getClass().getResource("/visualvm-td.txt");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Files.copy(Paths.get(resource.toURI()), out);
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
    }

    /*
    *
    "pool-7-thread-1" prio=6 tid=0x000000000dab8800 nid=0x25f4 waiting on condition [0x000000000c7bf000]
       java.lang.Thread.State: WAITING (parking)
    	at sun.misc.Unsafe.park(Native Method)
    	- parking to wait for  <0x00000000fdb403b0> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
    	at java.util.concurrent.locks.LockSupport.park(LockSupport.java:186)
    	at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2043)
    	at java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)
    	at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1068)
    	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1130)
    	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:615)
    	at java.lang.Thread.run(Thread.java:722)

       Locked ownable synchronizers:
    	- None
    * */

}
