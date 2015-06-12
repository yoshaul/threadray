package com.yossis.threadray.ui.swing;

import javax.swing.*;

/**
 * Main class.
 *
 * @author Yossi Shaul
 */
public class ThreadRay {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame("ThreadRay"));
    }
}
