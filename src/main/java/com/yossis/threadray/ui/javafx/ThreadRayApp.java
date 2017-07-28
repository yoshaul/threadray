package com.yossis.threadray.ui.javafx;

import com.yossis.threadray.config.ThreadRayConfig;
import com.yossis.threadray.model.ThreadDump;
import com.yossis.threadray.parser.Parser;
import com.yossis.threadray.ui.javafx.util.UI;
import com.yossis.threadray.ui.javafx.view.RootLayout;
import com.yossis.threadray.ui.javafx.view.ThreadsPane;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Main application window.
 *
 * @author Yossi Shaul
 */
public class ThreadRayApp extends Application {

    private ThreadRayConfig config;
    private Stage stage;
    private BorderPane rootLayout;
    private ThreadsPane.ThreadRayThreadsController threadsController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        config = ThreadRayConfig.getConfig();

        stage.setTitle("ThreadRay");
        UI.setAppIcon(stage);
        stage.setOnCloseRequest(e -> closeApp());

        createRootLayout();
        createThreadsMainLayout();
        loadLastLocation();

        File lastOpenFile = config.getLastOpenFile();
        Path lastOpen = lastOpenFile != null ? lastOpenFile.toPath() : null;
        loadThreadDump(lastOpen);

        Scene scene = new Scene(rootLayout);
        scene.getStylesheets().add(this.getClass().getResource("/ui/themes/DefaultTheme.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    private void createRootLayout() throws IOException {
        ThreadRayRootController controller = new ThreadRayRootController();
        controller.setApp(this);
        controller.initialize();
        rootLayout = new RootLayout(controller);
    }

    public void createThreadsMainLayout() throws IOException {
        ThreadsPane threadsPane = new ThreadsPane(stage);
        rootLayout.setCenter(threadsPane);
        threadsController = threadsPane.getController();
    }

    public void loadThreadDump(Path path) {
        if (path == null || !path.toFile().isFile()) {
            return;
        }
        try {
            ThreadDump dump = new Parser(path).parse();

            stage.setTitle("ThreadRay - " + path.getFileName());

            threadsController.update(dump);
        } catch (Exception e) {
            // TODO: print error to the UI
            e.printStackTrace();
        }
    }

    public Window getPrimaryStage() {
        return stage;
    }

    private void loadLastLocation() {
        Point location = config.getLocation();
        stage.setX(location.getX());
        stage.setY(location.getY());

        Dimension size = config.getWindowSize();
        stage.setHeight(size.getHeight());
        stage.setWidth(size.getWidth());
    }

    private void setLastStageLocation() {
        config.setLocation(new Point((int) stage.getX(), (int) stage.getY()));
        config.setWindowSize(new Dimension((int) stage.getWidth(), (int) stage.getHeight()));
    }

    @Override
    public void stop() throws Exception {
        setLastStageLocation();
        config.save();
    }

    public void closeApp() {
        Platform.exit();
    }
}
