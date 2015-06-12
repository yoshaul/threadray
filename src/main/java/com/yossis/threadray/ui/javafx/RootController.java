package com.yossis.threadray.ui.javafx;

import com.yossis.threadray.config.ThreadRayConfig;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;

import java.io.File;

/**
 * Controller for the main window.
 *
 * @author Yossi Shaul
 */
public class RootController {

    private ThreadRayApp app;
    private ThreadRayConfig config;

    public void setApp(ThreadRayApp app) {
        this.app = app;
    }

    @FXML
    private void initialize() {
        config = ThreadRayConfig.loadConfig();
    }

    /**
     * Opens a file chooser to load thread dump file.
     */
    @FXML
    private void handleOpen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(config.getLastOpenDirectory());
        File file = fileChooser.showOpenDialog(app.getPrimaryStage());
        if (file != null) {
            config.setLastOpenFolder(file.getParentFile());
            app.loadThreadDump(file.toPath());
        }
    }

    /**
     * Handles the exit menu item.
     */
    @FXML
    private void handleExit() {
        app.closeApp();
    }

    /**
     * Display the about dialog.
     */
    @FXML
    private void handleAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("ThreadRay");
        alert.setHeaderText("About");
        alert.setContentText("Copyright 2014-2015 Yossi Shaul");
        alert.showAndWait();
    }
}
