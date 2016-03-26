package com.yossis.threadray.config;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * App configuration handler.
 *
 * @author Yossi Shaul
 */
public class ThreadRayConfig extends Config {
    private static ThreadRayConfig config;

    private ThreadRayConfig(File configFile) {
        super(configFile);
    }

    public static ThreadRayConfig getConfig() {
        if (config == null) {
            File file = new File(new File(System.getProperty("user.home"), ".threadray"), "threadray.conf");
            config = new ThreadRayConfig(file);
            try {
                config.load();
            } catch (IOException e) {
                // TODO: log
            }
        }
        return config;
    }

    public void setLocation(Point location) {
        setProperty("gui.location.x", (int) location.getX());
        setProperty("gui.location.y", (int) location.getY());
    }

    public Point getLocation() {
        int x = getIntProperty("gui.location.x", 0);
        int y = getIntProperty("gui.location.y", 0);
        return new Point(x, y);
    }

    public void setWindowSize(Dimension size) {
        setProperty("gui.height", (int) size.getHeight());
        setProperty("gui.width", (int) size.getWidth());
    }

    public Dimension getWindowSize() {
        Toolkit tk = Toolkit.getDefaultToolkit();
        int width = getIntProperty("gui.width", ((int) tk.getScreenSize().getWidth()));
        int height = getIntProperty("gui.height", ((int) tk.getScreenSize().getHeight()));
        return new Dimension(width, height);
    }

    public File getLastOpenDirectory() {
        String lastOpen = getProperty("gui.last.open", System.getProperty("user.dir"));
        return new File(lastOpen);
    }

    public void setLastOpenFolder(File parentFile) {
        setProperty("gui.last.open", parentFile.getAbsolutePath());
    }
}
