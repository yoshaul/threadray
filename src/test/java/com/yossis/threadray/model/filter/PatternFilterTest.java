package com.yossis.threadray.model.filter;

import com.yossis.threadray.model.ThreadElement;
import com.yossis.threadray.parser.StackElement;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link PatternFilter}.
 *
 * @author Yossi Shaul
 */
public class PatternFilterTest {
    @Test
    public void getType() throws Exception {
        assertEquals(new PatternFilter("").getType(), "Pattern");
    }

    @Test
    public void filterMatch() throws Exception {
        ThreadElement t = new ThreadElement("test");
        t.addStackElement(new StackElement("org.test"));
        assertTrue(new PatternFilter("org").filter(t));
    }

    @Test
    public void filterNoMatch() throws Exception {
        ThreadElement t = new ThreadElement("test");
        t.addStackElement(new StackElement("org.test"));
        assertTrue(new PatternFilter("org").filter(t));
    }

    @Test
    public void filterEmptyStack() throws Exception {
        ThreadElement t = new ThreadElement("test");
        assertFalse(new PatternFilter("org").filter(t));
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(new PatternFilter("**/path").toString(), "**/path");
    }
}