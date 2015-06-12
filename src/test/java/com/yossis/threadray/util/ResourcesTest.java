package com.yossis.threadray.util;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for {@link Resources} class.
 *
 * @author Yossi Shaul
 */
public class ResourcesTest {

    @Test(expected = Resources.ResourceNotFoundException.class)
    public void noSuchResource() {
        Resources.getStream("no_such_resource");
    }

    @Test
    public void existingResourceAbsolutePath() {
        assertNotNull(Resources.getStream("/ui/icons/icon_016.png"));
    }

    @Test
    public void existingResourceRelativePath() {
        assertNotNull(Resources.getStream("ui/icons/icon_016.png"));
    }

    @Test(expected = Resources.ResourceNotFoundException.class)
    public void noSuchResourceUrl() {
        Resources.getUrl("no_such_resource");
    }

    @Test
    public void existingResourceAbsolutePathUrl() {
        assertNotNull(Resources.getUrl("/ui/icons/icon_016.png"));
    }

    @Test
    public void existingResourceRelativePathUrl() {
        assertNotNull(Resources.getUrl("ui/icons/icon_016.png"));
    }
}