package com.yossis.threadray.parser;

import com.yossis.threadray.model.ThreadElement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Parses a thread dump.
 *
 * @author Yossi Shaul
 */
public class Parser {

    private final String content;
    private List<ThreadElement> threads = new ArrayList<>();

    public Parser(String content) {
        this.content = content;
    }

    public List<ThreadElement> getThreads() {
        return threads;
    }

    public Parser parse() throws IOException {
        BufferedReader reader = new BufferedReader(new StringReader(content));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("\"")) {
                // beginning of new thread
                parseThread(line, reader);
            }
        }

        System.out.println("Found " + threads.size() + " threads:");
        for (ThreadElement thread : threads) {
            System.out.println(thread.getName());
        }
        return this;
    }

    private void parseThread(String threadTitle, BufferedReader reader) throws IOException {
        ThreadElement thread = parseThreadTitle(threadTitle);
        threads.add(thread);

        // parse optional state
        String line = reader.readLine();
        if (line.isEmpty()) {
            return; // no state and no stack elements (e.g., GC worker threads)
        }
        parseState(thread, line);

        // parse optional stack elements
        parseStackElements(thread, reader);

    }

    private ThreadElement parseThreadTitle(String threadTitle) {
        int threadNameEnd = threadTitle.indexOf("\"", 1);
        String threadName = threadTitle.substring(1, threadNameEnd);
        ThreadElement thread = new ThreadElement(threadName);
        String[] tokens = threadTitle.substring(threadNameEnd + 2).split(" ");
        int i = 0;
        if ("daemon".equals(tokens[i])) {
            thread.setDaemon(true);
            // daemon is optional so we increase the current token index only if it exists
            i++;
        }

        thread.setPriority(Integer.parseInt(getValue(tokens[i++])));
        thread.setThreadId(Long.decode(getValue(tokens[i++])));
        thread.setNativeId(Integer.decode(getValue(tokens[i++])));
        thread.setDescription(tokens[i]); // TODO: not the whole tokens
        return thread;
    }

    private String getValue(String token) {
        // get the value from a token in the format of key=value
        return token.substring(token.indexOf('=') + 1);
    }

    private void parseState(ThreadElement thread, String stateLine) {
        thread.setState(stateLine);
    }

    private void parseStackElements(ThreadElement thread, BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.isEmpty()) {
                return; // no state and no stack elements (e.g., GC worker threads)
            }

            if (line.trim().startsWith("at")) {
                StackElement stackElement = new StackElement(line);
                thread.addStackElement(stackElement);
            } else if (line.trim().startsWith("-")) {
                thread.getStackElements().get(thread.getStackElements().size() - 1).setState(line);
            } else {
                throw new IllegalArgumentException("Unexpected stack trace line: " + line);
            }
        }
    }

    private int skipWhiteSpace(char[] chars) {
        for (int i = 0; i < chars.length; i++) {
            if (!Character.isWhitespace(chars[i])) {
                return i;
            }
        }
        return chars.length;    // whitespace till the end
    }

}
