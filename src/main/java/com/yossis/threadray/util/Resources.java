package com.yossis.threadray.util;

import java.io.InputStream;
import java.net.URL;

/**
 * Helper class to retrieve resources.
 *
 * @author Yossi Shaul
 */
public abstract class Resources {

    /**
     * Returns the resource at the given path. The resource path assumed to be absolute.
     *
     * @param name The resource path
     * @return Resource input stream. Throws {@link ResourceNotFoundException} if not found.
     */
    public static InputStream getStream(String name) {
        name = toAbsolutePath(name);
        InputStream resource = Resources.class.getResourceAsStream(name);
        if (resource == null) {
            throw new ResourceNotFoundException(name);
        }
        return resource;
    }

    /**
     * Returns the resource URL at the given path. The resource path assumed to be absolute.
     *
     * @param name The resource path
     * @return Resource URL. Throws {@link ResourceNotFoundException} if not found.
     */
    public static URL getUrl(String name) {
        name = toAbsolutePath(name);
        URL url = Resources.class.getResource(name);
        if (url == null) {
            throw new ResourceNotFoundException(name);
        }
        return url;
    }

    private static String toAbsolutePath(String name) {
        if (!name.startsWith("/")) {
            name = "/" + name;
        }
        return name;
    }

    static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String name) {
            super("Couldn't find resource at: " + name);
        }
    }
}
