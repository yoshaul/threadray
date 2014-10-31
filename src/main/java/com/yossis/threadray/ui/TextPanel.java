package com.yossis.threadray.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Contains the text component with the thread dump text.
 *
 * @author Yossi Shaul
 */
public class TextPanel extends JPanel {

    private JTextArea textArea;
    private JScrollPane scrollPane;

    public TextPanel() {
        setLayout(new GridLayout(1, 1));
        textArea = new JTextArea("יוסי ואריאל טליה ועלמה ליטל המקסימים");
        textArea.setEditable(false);
        scrollPane = new JScrollPane(textArea);
        add(scrollPane);
    }

    public void setText(String text) {
        textArea.setText(text);
        // scroll back to the top
        SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(0));
    }

    public String getSelectedText() {
        return textArea.getSelectedText();
    }
}
