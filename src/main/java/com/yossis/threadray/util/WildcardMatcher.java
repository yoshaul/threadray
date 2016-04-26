package com.yossis.threadray.util;

/**
 * Matches an input string against a given wildcards pattern. <br>
 * The wildcard '?' matches exactly single character. <br>
 * The wildcard '*' matches zero or more characters. <br><br>
 * <p>
 * Some examples:
 * <pre>
 *     matches("any", "*") = true
 *     matches("hello", "h*l?") = true
 *     matches("hello", "c*l?") = false
 *     matches("hello world", "h* *d") = true
 * </pre>
 *
 * @author Yossi Shaul
 */
public class WildcardMatcher {

    /**
     * Returns whether the input text matches the input pattern.
     *
     * @param text    Text to test for matching. Always return false if text is null
     * @param pattern A pattern with optional wildcards
     * @return True if there's a match.
     */
    public static boolean matches(String text, String pattern) {
        if (text == null) {
            // no null matching
            return false;
        }
        if (pattern == null || pattern.length() == 0) {
            // empty patter -> everything but null matches
            return true;
        }

        if (pattern.indexOf('?') < 0 && pattern.indexOf('*') < 0) {
            return text.equals(pattern);
        }

        return doMatch(text, pattern);
    }

    private static boolean doMatch(String text, String pattern) {
        int tIndex = 0, pIndex = 0;
        int curAsteriskIndex = -1;  // index in the pattern string of the current active asterisk
        int tAsteriskIndex = -1;    // index in the text of the last character matched to the active asterisk
        while (tIndex < text.length()) {
            char c = text.charAt(tIndex);
            if (pIndex < pattern.length()) {
                char p = pattern.charAt(pIndex);
                if (p == '?' || p == c) {
                    pIndex++;
                    tIndex++;
                    continue;
                }
                if (p == '*') {
                    curAsteriskIndex = pIndex;  // store current asterisk location
                    pIndex++;                   // move to the next pattern char
                    tAsteriskIndex = tIndex;    // store the location of current text index
                    continue;
                }
            }
            if (curAsteriskIndex > -1) {
                pIndex = curAsteriskIndex + 1;  // revert pattern index to character after current asterisk
                tIndex = ++tAsteriskIndex;      // move to the next character in the text and store the last matched character location
                continue;
            }
            return false;
        }

        // skip trailing stars
        while (pIndex < pattern.length() && pattern.charAt(pIndex) == '*') {
            pIndex++;
        }
        return pIndex == pattern.length();
    }
}