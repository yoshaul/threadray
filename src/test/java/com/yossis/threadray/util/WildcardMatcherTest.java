package com.yossis.threadray.util;

import org.junit.Test;

import static com.yossis.threadray.util.WildcardMatcher.matches;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Units tests for {@link WildcardMatcher}.
 *
 * @author Yossi Shaul
 */
public class WildcardMatcherTest {

    @Test
    public void asteriskMatchingAnyString() {
        assertTrue(matches("", "*"));
        assertTrue(matches("a", "*"));
        assertTrue(matches("a\n", "*"));
    }

    @Test
    public void asteriskAtEnd() {
        assertTrue(matches("a", "a*"));
        assertTrue(matches("a", "a**"));
    }

    @Test
    public void multipleAsteriskWithSpace() {
        assertTrue(matches("hello world", "h*"));
        assertTrue(matches("hello world", "h* *d"));
    }

    @Test
    public void asteriskMatching() {
        assertFalse(matches("", "a*"));
        assertTrue(matches("a", "a*")); // zero or more
        assertTrue(matches("ab", "a*"));
        assertTrue(matches("ba", "*a*"));
        assertTrue(matches("bab", "*a*"));
        assertTrue(matches("babb", "*a*"));
        assertFalse(matches("bbbb", "*a*"));
    }

    @Test
    public void questionMarkMatching() {
        assertTrue(matches("a", "?"));
        assertFalse(matches("", "?"));
        assertTrue(matches("aa", "??"));
        assertFalse(matches("aa", "?"));
        assertTrue(matches("aa", "?a"));
        assertTrue(matches("aa", "a?"));
        assertTrue(matches("aba", "a?a"));
    }

    @Test
    public void combinedMatching() {
        assertTrue(matches("a", "*?"));
        assertFalse(matches("", "?*"));
        assertTrue(matches("aa", "*?"));
        assertTrue(matches("aba", "*?"));
        assertFalse(matches("aba", "*?d"));
        assertTrue(matches("caab", "*a?b"));
        assertTrue(matches("ccaab", "*a?b"));
        assertTrue(matches("hello", "h*l?"));
        assertFalse(matches("hello", "c*l?"));
    }

    @Test
    public void matchPatternWithNoWildcards() {
        assertTrue(matches("aa", "aa"));
        assertFalse(matches("a", "aa"));
        assertFalse(matches("aa", "aaa"));
    }

    @Test
    public void matchEmptyTextAndPattern() {
        assertTrue(matches("", ""));
    }

    @Test
    public void matchNullText() {
        assertFalse(matches(null, "*"));
    }
}