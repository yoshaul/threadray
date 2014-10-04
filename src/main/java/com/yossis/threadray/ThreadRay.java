package com.yossis.threadray;

import com.yossis.threadray.config.ThreadRayConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

/**
 * Main class.
 *
 * @author Yossi Shaul
 */
public class ThreadRay {

    private JFrame main;
    private ThreadRayConfig config;
    private AppWindowAdapter windowListener;

    public static void main(String[] args) {
        new ThreadRay().start();
    }

    private void start() {

        config = ThreadRayConfig.loadConfig();

        main = new JFrame("ThreadRay");
        main.setLayout(new GridLayout(3, 1));
        windowListener = new AppWindowAdapter();
        main.addWindowListener(windowListener);

        // menu bar
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = createFileMenu();
        menuBar.add(fileMenu);
        main.setMenuBar(menuBar);

        // main text area
        JTextArea textArea = new JTextArea("יוסי ואריאל טליה ועלמה ליטל המקסימים");
        textArea.setEditable(false);
        main.add(textArea);

        loadLocation();
        main.setVisible(true);
    }

    private Menu createFileMenu() {
        setLookAndFeel();
        Menu fileMenu = new Menu("File");
        // exit
        MenuItem exitMenuItem = new MenuItem("Exit", new MenuShortcut(KeyEvent.VK_X));
        exitMenuItem.addActionListener(e -> windowListener.windowClosing(null));
        fileMenu.add(exitMenuItem);

        // open
        MenuItem openMenuItem = new MenuItem("Open", new MenuShortcut(KeyEvent.VK_O));
        openMenuItem.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            //Handle open button action.
            int returnVal = fc.showOpenDialog(main);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                System.out.println("file = " + file);
            }
        });
        fileMenu.add(openMenuItem);

        return fileMenu;
    }

    private void setLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Cannot set look and feel", e);
        }
    }

    private void loadLocation() {
        Point location = config.getLocation();
        main.setLocation(location);

        Dimension size = config.getWindowSize();
        main.setSize(size);
    }

    private void saveLastLocation() {
        Point locationOnScreen = main.getLocationOnScreen();
        config.setLocation(locationOnScreen.getLocation());
        config.setWindowSize(main.getSize());
    }

    private class AppWindowAdapter extends WindowAdapter {
        public void windowClosing(WindowEvent windowEvent) {
            saveLastLocation();
            config.save();
            System.exit(0);
        }
    }
}
