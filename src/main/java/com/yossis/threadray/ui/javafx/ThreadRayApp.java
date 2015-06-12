package com.yossis.threadray.ui.javafx;

import com.yossis.threadray.config.ThreadRayConfig;
import com.yossis.threadray.model.ThreadElement;
import com.yossis.threadray.parser.Parser;
import com.yossis.threadray.ui.javafx.model.ThreadElementFx;
import com.yossis.threadray.util.Resources;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    private ObservableList<ThreadElementFx> threadsFx = FXCollections.observableArrayList();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        config = ThreadRayConfig.loadConfig();

        stage.setTitle("ThreadRay");
        stage.getIcons().add(new Image(Resources.getStream("/ui/icons/icon_016.png")));
        stage.getIcons().add(new Image(Resources.getStream("/ui/icons/icon_032.png")));
        stage.setOnCloseRequest(e -> closeApp());

        initLayout();
        loadThreadDump(null);
        showThreadsMain();
        loadLastLocation();
        stage.show();
    }

    private void initLayout() throws IOException {
        // loaf the root layout and initialize
        FXMLLoader loader = getFxmlLoader("/com/yossis/threadray/ui/javafx/view/ThreadRayLayout.fxml");
        rootLayout = loader.load();
        RootController controller = loader.getController();
        controller.setApp(this);

        // display the root layout scene
        Scene scene = new Scene(rootLayout);
        stage.setScene(scene);
    }

    public void showThreadsMain() throws IOException {
        FXMLLoader loader = getFxmlLoader("/com/yossis/threadray/ui/javafx/view/ThreadRayApp.fxml");

        AnchorPane personOverview = loader.load();
        rootLayout.setCenter(personOverview);

        ThreadsController controller = loader.getController();
        controller.setApp(this);
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

            threadsFx.remove(0, threadsFx.size());
            threads.stream().forEach(t -> threadsFx.add(new ThreadElementFx(t)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Window getPrimaryStage() {
        return stage;
    }

    public ObservableList<ThreadElementFx> getThreadsFx() {
        return threadsFx;
    }

    private void loadLastLocation() {
        Point location = config.getLocation();
        stage.setX(location.getX());
        stage.setY(location.getY());

        Dimension size = config.getWindowSize();
        stage.setHeight(size.getHeight());
        stage.setWidth(size.getWidth());
    }

    private void setLastLocation() {
        config.setLocation(new Point((int) stage.getX(), (int) stage.getY()));
        config.setWindowSize(new Dimension((int) stage.getWidth(), (int) stage.getHeight()));
    }

    public void closeApp() {
        setLastLocation();
        config.save();
        System.exit(0);
    }
}
