package com.yossis.threadray.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Copies selected text to the clipboard.
 *
 * @author Yossi Shaul
 */
class CopyActionListener implements ActionListener {
    private final JTextArea textArea;

    CopyActionListener(JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String selectedText = textArea.getSelectedText();
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(new StringSelection(selectedText), new StringSelection(selectedText));
    }
}
