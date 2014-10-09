package com.yossis.threadray;

import com.yossis.threadray.config.ThreadRayConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.util.List;

/**
 * Main class.
 *
 * @author Yossi Shaul
 */
public class ThreadRay {

    private JFrame main;
    private ThreadRayConfig config;
    private AppWindowAdapter windowListener;
    private JTextArea textArea;

    public static void main(String[] args) {
        new ThreadRay().start();
    }

    private void start() {

        config = ThreadRayConfig.loadConfig();

        main = new JFrame("ThreadRay");
        main.setLayout(new GridLayout(1, 1));
        windowListener = new AppWindowAdapter();
        main.addWindowListener(windowListener);

        // menu bar
        JMenuBar menuBar = new JMenuBar();
        main.setJMenuBar(menuBar);

        // file menu
        JMenu fileMenu = createFileMenu();
        menuBar.add(fileMenu);

        // about menu
        JMenu helpMenu = createHelpMenu();
        menuBar.add(helpMenu);

        // main text area
        textArea = new JTextArea("יוסי ואריאל טליה ועלמה ליטל המקסימים");
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        main.add(scrollPane);

        loadLocation();
        main.setVisible(true);
    }

    private JMenu createFileMenu() {
        setLookAndFeel();
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');

        // open
        JMenuItem openMenuItem = new JMenuItem("Open", 'O');
        openMenuItem.setAccelerator(KeyStroke.getKeyStroke('O', InputEvent.CTRL_DOWN_MASK));
        openMenuItem.addActionListener(e -> {
            File lastFolder = config.getLastOpenDirectory();
            JFileChooser fc = new JFileChooser(lastFolder);
            //Handle open button action.
            int returnVal = fc.showOpenDialog(main);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                config.setLastOpenFolder(file.getParentFile());
                try {
                    List<String> lines = Files.readAllLines(file.toPath());
                    String content = String.join("\n", lines);
                    textArea.setText(content);
                } catch (IOException e1) {
                    StringWriter stringWriter = new StringWriter();
                    e1.printStackTrace(new PrintWriter(stringWriter));
                    textArea.setText("Failed to load content from " + file.getAbsolutePath() + ":\n" + stringWriter);
                }
            }
        });
        fileMenu.add(openMenuItem);

        // exit
        JMenuItem exitMenuItem = new JMenuItem("Exit", 'X');
        exitMenuItem.addActionListener(e -> windowListener.windowClosing(null));
        fileMenu.add(exitMenuItem);

        return fileMenu;
    }

    private JMenu createHelpMenu() {
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic('H');

        JMenuItem aboutMenu = new JMenuItem("About...");
        aboutMenu.addActionListener(e -> JOptionPane.showMessageDialog(main, "Copyright 2014 Yossi Shaul"));
        helpMenu.add(aboutMenu);

        return helpMenu;

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
