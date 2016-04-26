package com.yossis.threadray.model.filter;

import com.yossis.threadray.model.ThreadDump;
import com.yossis.threadray.parser.Parser;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link FilteredThreadDump}.
 *
 * @author Yossi Shaul
 */
public class FilteredThreadDumpTest {

    @Test
    public void filteredThreadDumpWithNoFilters() throws IOException, URISyntaxException {
        ThreadDump dump = new Parser(Paths.get(getClass().getResource("/jstack-oracle-jdk-1.8.0_60.tdump").toURI())).parse();
        FilteredThreadDump filtered = new FilteredThreadDump(dump);
        assertEquals(dump.getText(), filtered.getText());
    }

    @Test
    public void filteredThreadDumpWithMatchingFilters() throws IOException, URISyntaxException {
        ThreadDump dump = new Parser(Paths.get(getClass().getResource("/jstack-oracle-jdk-1.8.0_60.tdump").toURI())).parse();
        FilteredThreadDump filtered = new FilteredThreadDump(dump);
        filtered.addFilter(new WildcardFilter("concurrent"));
        assertThat(filtered.getThreads()).isNotEqualTo(dump.getThreads()).hasSize(4);
        // assertNotEquals(dump.getText(), filtered.getText());
    }

    @Test
    public void filteredThreadDumpWithNoMatchingFilters() throws IOException, URISyntaxException {
        ThreadDump dump = new Parser(Paths.get(getClass().getResource("/jstack-oracle-jdk-1.8.0_60.tdump").toURI())).parse();
        FilteredThreadDump filtered = new FilteredThreadDump(dump);
        filtered.addFilter(new WildcardFilter("no such stack element"));
        assertThat(filtered.getThreads()).hasSize(0);
        // assertEquals("", filtered.getText());
    }

}
