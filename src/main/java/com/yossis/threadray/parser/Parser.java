package com.yossis.threadray.parser;

import com.yossis.threadray.model.ThreadDump;
import com.yossis.threadray.model.ThreadElement;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
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

    public Parser(Path filePath) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Files.copy(filePath, out);
            content = out.toString("UTF-8");
        }
    }

    public Parser(String content) {
        this.content = content;
    }

    public List<ThreadElement> getThreads() {
        return threads;
    }

    public ThreadDump parse() throws IOException {
        BufferedReader reader = new BufferedReader(new StringReader(content));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("\"")) {
                // beginning of new thread
                parseThread(line, reader);
            }
        }
        return new ThreadDump(content, threads);
    }

    private void parseThread(String threadTitle, BufferedReader reader) throws IOException {
        StringBuilder sb = new StringBuilder(threadTitle).append("\n");
        ThreadElement thread = parseThreadTitle(threadTitle);
        threads.add(thread);

        // parse optional state
        String line = reader.readLine();
        if (!line.isEmpty()) {  // can be empty is no state and no stack elements (e.g., GC worker threads)
            sb.append(line).append("\n");
            parseState(thread, line);

            // parse optional stack elements
            parseStackElements(thread, reader, sb);
        }

        thread.setThreadDump(sb.toString());
    }

    /**
     * Parses thread title. Structure of thread title:
     * Java 7: "thread name" [daemon] prio={} tid={} nid={} description
     * Java 7: "thread name" [#thread_number] [daemon] [prio={}] os_prio={} tid={} nid={} description
     *
     * @param threadTitle The thread title
     * @return Thread element with the parsed data
     */
    private ThreadElement parseThreadTitle(String threadTitle) {
        try {
            int threadNameEnd = threadTitle.indexOf("\"", 1);
            String threadName = threadTitle.substring(1, threadNameEnd);
            ThreadElement thread = new ThreadElement(threadName);
            String[] tokens = threadTitle.substring(threadNameEnd + 2).split(" ");
            int i = 0;

            // java 8 thread number
            if (tokens[i].startsWith("#")) {
                thread.setNumber(Integer.parseInt(tokens[i++].substring(1)));
            }

            // optional daemon
            if ("daemon".equals(tokens[i])) {
                thread.setDaemon(true);
                // daemon is optional so we increase the current token index only if it exists
                i++;
            }

            // prio is optional in java 8
            if ("prio".equals(getKey(tokens[i]))) {
                thread.setPriority(Integer.parseInt(getValue(tokens[i++])));
            }

            // check for java 8 os_prio
            if ("os_prio".equals(getKey(tokens[i]))) {
                thread.setOsPriority(Integer.parseInt(getValue(tokens[i++])));
            }

            thread.setThreadId(Long.decode(getValue(tokens[i++])));
            thread.setNativeId(Integer.decode(getValue(tokens[i++])));
            thread.setDescription(tokens[i]); // TODO: not the whole tokens
            return thread;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse: " + threadTitle, e);
        }
    }

    private String getKey(String token) {
        // get the key from a token in the format of key=value
        return token.substring(0, token.indexOf('='));
    }

    private String getValue(String token) {
        // get the value from a token in the format of key=value
        return token.substring(token.indexOf('=') + 1);
    }

    private void parseState(ThreadElement thread, String stateLine) {
        String statePrefix = "java.lang.Thread.State: ";
        if (stateLine.trim().length() > statePrefix.length()) {
            String stateAndDescription = stateLine.trim().substring(statePrefix.length()).trim();
            Thread.State state;
            String description = "";
            // extract Thread.STATE and optional extra details inside brackets. e.g., WAITING (parking)
            int descriptionIdx = stateAndDescription.indexOf('(');
            if (descriptionIdx > -1) {
                state = Thread.State.valueOf(stateAndDescription.substring(0, descriptionIdx - 1));
                // extract description from within brackets
                description = stateAndDescription.substring(descriptionIdx + 1, stateAndDescription.length() - 1);
            } else {
                state = Thread.State.valueOf(stateAndDescription);
            }
            thread.setState(stateAndDescription);
            thread.setThreadState(new ThreadElement.State(state, description));
        } else {
            // unknown state line
            thread.setState(stateLine);
        }
    }

    private void parseStackElements(ThreadElement thread, BufferedReader reader, StringBuilder sb) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.isEmpty()) {
                return; // done parsing current thread
            }
            sb.append(line).append("\n");
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
