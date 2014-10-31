package com.yossis.threadray.ui;

import com.yossis.threadray.config.ThreadRayConfig;
import com.yossis.threadray.model.ThreadElement;
import com.yossis.threadray.parser.Parser;

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
import java.util.stream.Collectors;

/**
 * Main application window.
 *
 * @author Yossi Shaul
 */
public class MainFrame extends JFrame {

    private ThreadRayConfig config;
    private AppWindowAdapter windowListener;
    private TextPanel textPanel;
    private JTextArea leftTextArea;

    public MainFrame(String title) throws HeadlessException {
        super(title);
        setup();
    }

    private void setup() {
        config = ThreadRayConfig.loadConfig();

        windowListener = new AppWindowAdapter();
        addWindowListener(windowListener);

        setLookAndFeel();

        // main text area
        textPanel = new TextPanel();

        createMenuBar();

        leftTextArea = new JTextArea();
        JScrollPane leftScrollPane = new JScrollPane(leftTextArea);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftScrollPane, textPanel);
        add(splitPane);

        loadLocation();
        setVisible(true);
    }

    private void createMenuBar() {
        // menu bar
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // file menu
        menuBar.add(createFileMenu());

        // edit menu
        menuBar.add(createEditMenu());

        // about menu
        menuBar.add(createHelpMenu());
    }

    private JMenu createFileMenu() {
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');

        // open
        JMenuItem openMenuItem = new JMenuItem("Open", 'O');
        openMenuItem.setAccelerator(KeyStroke.getKeyStroke('O', InputEvent.CTRL_DOWN_MASK));
        openMenuItem.addActionListener(e -> {
            File lastFolder = config.getLastOpenDirectory();
            JFileChooser fc = new JFileChooser(lastFolder);
            //Handle open button action.
            int returnVal = fc.showOpenDialog(MainFrame.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                config.setLastOpenFolder(file.getParentFile());
                try {
                    java.util.List<String> lines = Files.readAllLines(file.toPath());
                    String content = String.join("\n", lines);
                    textPanel.setText(content);
                    // parse
                    SwingUtilities.invokeLater(() -> {
                        try {
                            Parser parser = new Parser(content).parse();
                            String threadNames = parser.getThreads().stream()
                                    .map(ThreadElement::getName)
                                    .collect(Collectors.joining("\n"));
                            leftTextArea.setText(threadNames);
                        } catch (IOException e1) {
                            StringWriter stringWriter = new StringWriter();
                            e1.printStackTrace(new PrintWriter(stringWriter));
                            textPanel.setText("Failed to parse file " + file.getAbsolutePath() + ":\n" + stringWriter);
                        }
                    });
                } catch (IOException e1) {
                    StringWriter stringWriter = new StringWriter();
                    e1.printStackTrace(new PrintWriter(stringWriter));
                    textPanel.setText("Failed to load content from " + file.getAbsolutePath() + ":\n" + stringWriter);
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

    private Component createEditMenu() {
        JMenu edit = new JMenu("Edit");
        edit.setMnemonic('E');
        edit.add(createCopyMenuItem());
        return edit;
    }

    private JMenuItem createCopyMenuItem() {
        JMenuItem copy = new JMenuItem("Copy");
        copy.setAccelerator(KeyStroke.getKeyStroke('C', InputEvent.CTRL_DOWN_MASK));
        copy.addActionListener(new CopyActionListener(textPanel.getTextArea()));
        return copy;
    }


    private JMenu createHelpMenu() {
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic('H');

        JMenuItem aboutMenu = new JMenuItem("About...");
        aboutMenu.addActionListener(e -> JOptionPane.showMessageDialog(MainFrame.this, "Copyright 2014 Yossi Shaul"));
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
        setLocation(location);

        Dimension size = config.getWindowSize();
        setSize(size);
    }

    private void saveLastLocation() {
        Point locationOnScreen = getLocationOnScreen();
        config.setLocation(locationOnScreen.getLocation());
        config.setWindowSize(getSize());
    }

    private class AppWindowAdapter extends WindowAdapter {
        public void windowClosing(WindowEvent windowEvent) {
            saveLastLocation();
            config.save();
            System.exit(0);
        }
    }
}
