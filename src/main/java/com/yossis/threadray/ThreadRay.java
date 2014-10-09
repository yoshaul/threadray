package com.yossis.threadray;

import com.yossis.threadray.config.ThreadRayConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
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
        JMenuBar menuBar = new JMenuBar();
        main.setJMenuBar(menuBar);

        // file menu
        JMenu fileMenu = createFileMenu();
        menuBar.add(fileMenu);

        // main text area
        JTextArea textArea = new JTextArea("יוסי ואריאל טליה ועלמה ליטל המקסימים");
        textArea.setEditable(false);
        main.add(textArea);

        loadLocation();
        main.setVisible(true);
    }

    private JMenu createFileMenu() {
        setLookAndFeel();
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
        // exit
        JMenuItem exitMenuItem = new JMenuItem("Exit", 'X');
        exitMenuItem.addActionListener(e -> windowListener.windowClosing(null));
        fileMenu.add(exitMenuItem);

        // open
        JMenuItem openMenuItem = new JMenuItem("Open", 'O');
        openMenuItem.setAccelerator(KeyStroke.getKeyStroke('O', InputEvent.CTRL_DOWN_MASK));
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
