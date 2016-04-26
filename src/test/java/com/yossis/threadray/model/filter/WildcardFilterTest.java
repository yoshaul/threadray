package com.yossis.threadray.model.filter;

import com.yossis.threadray.model.ThreadElement;
import com.yossis.threadray.parser.StackElement;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link WildcardFilter}.
 *
 * @author Yossi Shaul
 */
public class WildcardFilterTest {
    @Test
    public void getType() throws Exception {
        assertEquals(new WildcardFilter("").getType(), "Pattern");
    }

    @Test
    public void filterMatch() throws Exception {
        ThreadElement t = new ThreadElement("test");
        t.addStackElement(new StackElement("org.test"));
        assertTrue(new WildcardFilter("org").test(t));
    }

    @Test
    public void filterNoMatch() throws Exception {
        ThreadElement t = new ThreadElement("test");
        t.addStackElement(new StackElement("org.test"));
        assertTrue(new WildcardFilter("org").test(t));
    }

    @Test
    public void filterEmptyStack() throws Exception {
        ThreadElement t = new ThreadElement("test");
        assertFalse(new WildcardFilter("org").test(t));
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(new WildcardFilter("**/path").toString(), "**/path");
    }
}