package com.yossis.threadray.parser;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

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
        new Parser(out.toString()).parse();
    }

}
