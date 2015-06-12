package com.yossis.threadray.ui.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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

        // popup menu for the main text area
        JPopupMenu popupMenu = createPopupMenu();
        textArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        scrollPane = new JScrollPane(textArea);
        add(scrollPane);
    }

    private JPopupMenu createPopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.add(createCopyMenuItem());
        return popupMenu;
    }

    private JMenuItem createCopyMenuItem() {
        JMenuItem copy = new JMenuItem("Copy");
        copy.setAccelerator(KeyStroke.getKeyStroke('C', InputEvent.CTRL_DOWN_MASK));
        copy.addActionListener(new CopyActionListener(textArea));
        return copy;
    }

    public void setText(String text) {
        textArea.setText(text);
        // scroll back to the top
        SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(0));
    }

    public JTextArea getTextArea() {
        return textArea;
    }
}
