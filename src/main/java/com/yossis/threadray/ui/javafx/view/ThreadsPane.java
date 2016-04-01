package com.yossis.threadray.ui.javafx.view;

import com.yossis.threadray.model.ThreadElement;
import com.yossis.threadray.ui.javafx.ThreadRayApp;
import com.yossis.threadray.ui.javafx.model.ThreadElementFx;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Pane containing thread dump info.
 *
 * @author Yossi Shaul
 */
public class ThreadsPane extends AnchorPane {
    private final ThreadRayThreadsController controller;
    private TableView<ThreadElementFx> threadsTable;
    private TableColumn<ThreadElementFx, String> threadNameColumn;
    private TableColumn<ThreadElementFx, String> threadStateColumn;
    private Label threadsCountLabel;
    private TextArea threadDumpTextArea;

    public ThreadsPane() {

        double leftRightInsets = 14.0;

        setPrefSize(800, 800);

        AnchorPane threadsList = new AnchorPane();
        threadsTable = new TableView<>();

        threadNameColumn = new TableColumn<>("Name");
        threadStateColumn = new TableColumn<>("State");

        threadsTable.getColumns().addAll(threadStateColumn, threadNameColumn);

        threadsList.getChildren().addAll(threadsTable);
        AnchorPane.setTopAnchor(threadsTable, 0.0);
        AnchorPane.setBottomAnchor(threadsTable, 0.0);
        AnchorPane.setLeftAnchor(threadsTable, 0.0);
        AnchorPane.setRightAnchor(threadsTable, 0.0);

        AnchorPane summaryPane = new AnchorPane();
        summaryPane.getChildren().add(new Label("Summary"));

        GridPane gridPane = new GridPane();
        gridPane.setLayoutX(14);
        gridPane.setLayoutY(39);
        gridPane.getColumnConstraints().addAll(new ColumnConstraints(100), new ColumnConstraints(100));
        AnchorPane.setLeftAnchor(gridPane, leftRightInsets);
        AnchorPane.setRightAnchor(gridPane, leftRightInsets);
        gridPane.add(new Label("Threads Count"), 0, 0);
        gridPane.add(threadsCountLabel = new Label(), 1, 0);
        // gridPane.setStyle("-fx-border-color: blue;");

        summaryPane.getChildren().add(gridPane);

        threadDumpTextArea = new TextArea();
        summaryPane.getChildren().add(threadDumpTextArea);

        threadDumpTextArea.setLayoutX(14);
        threadDumpTextArea.setLayoutY(117);
        AnchorPane.setTopAnchor(threadDumpTextArea, 117.0);
        AnchorPane.setBottomAnchor(threadDumpTextArea, 10.0);
        AnchorPane.setLeftAnchor(threadDumpTextArea, leftRightInsets);
        AnchorPane.setRightAnchor(threadDumpTextArea, leftRightInsets);

        SplitPane splitPane = new SplitPane(threadsList, summaryPane);
        splitPane.setPrefSize(600, 400);

        getChildren().add(splitPane);
        setBottomAnchor(splitPane, 0.0);
        setTopAnchor(splitPane, 0.0);
        setLeftAnchor(splitPane, 0.0);
        setRightAnchor(splitPane, 0.0);

        controller = new ThreadRayThreadsController();
        controller.initialize();
    }

    public ThreadRayThreadsController getController() {
        return controller;
    }


    public class ThreadRayThreadsController {
        private ObservableList<ThreadElementFx> threadsFx = FXCollections.observableArrayList();

        @FXML
        private void initialize() {
            threadNameColumn.setCellValueFactory(cellData -> cellData.getValue().getThreadName());
            threadStateColumn.setCellValueFactory(cellData -> cellData.getValue().getThreadState());

            threadsTable.getSelectionModel().selectedItemProperty().addListener(
                    (observable, oldValue, newValue) -> showThreadDetails(newValue));
        }

        public void setApp(ThreadRayApp app) {
            threadsTable.setItems(threadsFx);
        }

        public void update(List<ThreadElement> threads) {
            threadsCountLabel.setText(threads.size() + "");
            threadsFx.remove(0, threadsFx.size());
            threads.stream().forEach(t -> threadsFx.add(new ThreadElementFx(t)));
            String threadDump = threads.stream().map(ThreadElement::getThreadDump).collect(Collectors.joining("\n"));
            threadDumpTextArea.setText(threadDump);
        }

        private void showThreadDetails(ThreadElementFx thread) {
            //threadNameLabel.setText(thread != null ? thread.getThreadName().get() : "");
            //threadIdLabel.setText(thread != null ? thread.getThreadId() + "" : "");
            threadDumpTextArea.setText(thread != null ? thread.getThreadDump() : "");
        /*
        Text t = (Text) threadDumpTextArea.lookup(".text");
        System.out.println("LayoutX " + t.getLayoutX());
        System.out.println("LayoutY " + t.getLayoutY());
        System.out.println("Width: " + t.getBoundsInLocal().getWidth());
        System.out.println("Height: " + t.getBoundsInLocal().getHeight());
        */
        }

    }
}
