package com.yossis.threadray.ui.javafx;

import com.yossis.threadray.config.ThreadRayConfig;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

import java.io.File;

/**
 * Controller for the main window.
 *
 * @author Yossi Shaul
 */
public class ThreadRayRootController {

    private ThreadRayApp app;
    private ThreadRayConfig config;

    @FXML
    private TextArea threadDumpTextArea;

    public void setApp(ThreadRayApp app) {
        this.app = app;
    }

    @FXML
    public void initialize() {
        config = ThreadRayConfig.getConfig();
    }

    /**
     * Opens a file chooser to load thread dump file.
     */
    @FXML
    public void handleOpen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(config.getLastOpenDirectory());
        File file = fileChooser.showOpenDialog(app.getPrimaryStage());
        if (file != null) {
            config.setLastOpenFile(file);
            app.loadThreadDump(file.toPath());
        }
    }

    /**
     * Handles the exit menu item.
     */
    @FXML
    public void handleExit() {
        app.closeApp();
    }

    /**
     * Display the about dialog.
     */
    @FXML
    public void handleAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("ThreadRay");
        alert.setHeaderText("About");
        alert.setContentText("Copyright 2014-2016 Yossi Shaul");
        alert.showAndWait();
    }

    /**
     * Handles the copy menu item.
     */
    @FXML
    public void handleCopy() {
        threadDumpTextArea.copy();
    }

}
