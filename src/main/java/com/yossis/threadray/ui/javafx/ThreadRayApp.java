package com.yossis.threadray.ui.javafx;

import com.yossis.threadray.config.ThreadRayConfig;
import com.yossis.threadray.model.ThreadElement;
import com.yossis.threadray.parser.Parser;
import com.yossis.threadray.util.Resources;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Main application window.
 *
 * @author Yossi Shaul
 */
public class ThreadRayApp extends Application {

    private ThreadRayConfig config;
    private Stage stage;
    private BorderPane rootLayout;
    private ThreadRayThreadsController threadsController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        config = ThreadRayConfig.getConfig();

        stage.setTitle("ThreadRay");
        stage.getIcons().add(new Image(Resources.getStream("/ui/icons/icon_016.png")));
        stage.getIcons().add(new Image(Resources.getStream("/ui/icons/icon_032.png")));
        stage.setOnCloseRequest(e -> closeApp());

        loadRootLayout();
        loadThreadsMainLayout();
        loadLastLocation();

        // loadThreadDump(null);

        Scene scene = new Scene(rootLayout);
        stage.setScene(scene);
        stage.show();
    }

    private void loadRootLayout() throws IOException {
        FXMLLoader loader = getFxmlLoader("/com/yossis/threadray/ui/javafx/view/ThreadRayRootLayout.fxml");
        rootLayout = loader.load();
        ThreadRayRootController controller = loader.getController();
        controller.setApp(this);
    }

    public void loadThreadsMainLayout() throws IOException {
        FXMLLoader loader = getFxmlLoader("/com/yossis/threadray/ui/javafx/view/ThreadRayThreadsLayout2.fxml");
        AnchorPane personOverview = loader.load();
        rootLayout.setCenter(personOverview);
        threadsController = loader.getController();
        threadsController.setApp(this);
    }

    private FXMLLoader getFxmlLoader(String fxmlResource) {
        FXMLLoader loader = new FXMLLoader();
        URL resource = Resources.getUrl(fxmlResource);
        loader.setLocation(resource);
        return loader;
    }

    public void loadThreadDump(Path path) {
        try {
            if (path == null) {
                path = Paths.get("C:\\Dev\\Work\\Josh\\threadray\\src\\test\\resources\\visualvm-td.txt");
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Files.copy(path, out);
            List<ThreadElement> threads = new Parser(out.toString()).parse().getThreads();

            stage.setTitle("ThreadRay - " + path.getFileName());

            threadsController.update(threads);
        } catch (IOException e) {
            throw new RuntimeException(e);
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

    public void closeApp() {
        setLastStageLocation();
        config.save();
        System.exit(0);
    }
}
