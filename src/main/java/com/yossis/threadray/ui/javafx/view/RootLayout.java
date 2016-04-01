package com.yossis.threadray.ui.javafx.view;

import com.yossis.threadray.ui.javafx.ThreadRayRootController;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;

/**
 * Root pane of the application.
 *
 * @author Yossi Shaul
 */
public class RootLayout extends BorderPane {

    public RootLayout(ThreadRayRootController controller) {
        Menu fileMenu = createFileMenu(controller);
        Menu editMenu = createEditMenu(controller);
        Menu helpMenu = createHelpMenu(controller);
        MenuBar menuBar = new MenuBar(fileMenu, editMenu, helpMenu);
        setTop(menuBar);
    }

    private Menu createFileMenu(ThreadRayRootController controller) {
        MenuItem open = new MenuItem("_Open");
        open.setAccelerator(KeyCombination.keyCombination("shortcut+O"));
        open.setOnAction(e -> controller.handleOpen());

        MenuItem exit = new MenuItem("E_xit");
        exit.setAccelerator(KeyCombination.keyCombination("shortcut+Q"));
        exit.setOnAction(e -> controller.handleExit());

        return new Menu("_File", null, open, new SeparatorMenuItem(), exit);
    }

    private Menu createEditMenu(ThreadRayRootController controller) {
        MenuItem copy = new MenuItem("_Copy");
        copy.setAccelerator(KeyCombination.keyCombination("shortcut+C"));
        copy.setOnAction(e -> controller.handleCopy());

        return new Menu("_Edit", null, copy);
    }

    private Menu createHelpMenu(ThreadRayRootController controller) {
        MenuItem about = new MenuItem("_About");
        about.setOnAction(e -> controller.handleAbout());

        return new Menu("_Help", null, about);
    }
}
