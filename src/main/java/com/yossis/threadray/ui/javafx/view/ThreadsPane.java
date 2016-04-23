package com.yossis.threadray.ui.javafx.view;

import com.yossis.threadray.model.ThreadDump;
import com.yossis.threadray.model.ThreadElement;
import com.yossis.threadray.ui.javafx.ThreadRayApp;
import com.yossis.threadray.ui.javafx.model.ThreadElementFx;
import com.yossis.threadray.ui.javafx.view.filter.FiltersStage;
import com.yossis.threadray.ui.javafx.view.filter.ThreadFiltersPredicate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.List;

/**
 * Pane containing thread dump info.
 *
 * @author Yossi Shaul
 */
public class ThreadsPane extends AnchorPane {
    private final ThreadRayThreadsController controller;
    private TableView<ThreadElementFx> threadsTable;
    private TableColumn<ThreadElementFx, ThreadElement.State> threadStateColumn;
    private TableColumn<ThreadElementFx, String> threadNameColumn;
    private Label threadsCountLabel;
    private TextArea threadDumpTextArea;

    public ThreadsPane(Stage mainStage) {
        controller = new ThreadRayThreadsController();

        double leftRightInsets = 14.0;

        setPrefSize(800, 800);

        Button showAllBtn = new Button("Show all");
        showAllBtn.setOnAction(e -> threadsTable.getSelectionModel().clearSelection());

        Button filterBtn = new Button("Filter...");
        filterBtn.setOnAction(e -> new FiltersStage(mainStage, controller.threadFiltersPredicate));

        HBox tableToolbar = new HBox(5, showAllBtn, filterBtn);

        threadsTable = new TableView<>();

        threadStateColumn = new TableColumn<>("State");
        threadStateColumn.setPrefWidth(25);
        threadNameColumn = new TableColumn<>("Name");
        threadNameColumn.setPrefWidth(75);

        threadsTable.getColumns().addAll(threadStateColumn, threadNameColumn);

        // threadsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        threadStateColumn.prefWidthProperty().bind(threadsTable.widthProperty().multiply(0.25));
        threadNameColumn.prefWidthProperty().bind(threadsTable.widthProperty().multiply(0.75));

        AnchorPane.setTopAnchor(threadsTable, 25.0);
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
        threadDumpTextArea.setEditable(false);
        summaryPane.getChildren().add(threadDumpTextArea);

        threadDumpTextArea.setLayoutX(14);
        threadDumpTextArea.setLayoutY(117);
        AnchorPane.setTopAnchor(threadDumpTextArea, 117.0);
        AnchorPane.setBottomAnchor(threadDumpTextArea, 10.0);
        AnchorPane.setLeftAnchor(threadDumpTextArea, leftRightInsets);
        AnchorPane.setRightAnchor(threadDumpTextArea, leftRightInsets);

        threadDumpTextArea.setContextMenu(createTextAreaContextMenu());

        AnchorPane threadsList = new AnchorPane();
        threadsList.getChildren().addAll(tableToolbar, threadsTable);

        SplitPane splitPane = new SplitPane(threadsList, summaryPane);
        splitPane.setPrefSize(600, 400);

        getChildren().add(splitPane);
        setBottomAnchor(splitPane, 0.0);
        setTopAnchor(splitPane, 0.0);
        setLeftAnchor(splitPane, 0.0);
        setRightAnchor(splitPane, 0.0);

        controller.initialize();
    }

    private ContextMenu createTextAreaContextMenu() {
        MenuItem copy = new MenuItem("_Copy");
        copy.setAccelerator(KeyCombination.keyCombination("shortcut+C"));
        copy.setOnAction(e -> threadDumpTextArea.copy());

        MenuItem count = new MenuItem("Count");
        copy.setOnAction(e -> {
            String selectedText = threadDumpTextArea.getSelectedText();
            getController().countMatches(selectedText);
        });

        return new ContextMenu(copy, new SeparatorMenuItem(), count);
    }

    public ThreadRayThreadsController getController() {
        return controller;
    }

    public class ThreadRayThreadsController {
        // current active thread dump
        private ThreadDump dump;
        private ObservableList<ThreadElementFx> threadsFx = FXCollections.observableArrayList();
        private ThreadFiltersPredicate threadFiltersPredicate;

        @FXML
        private void initialize() {
            threadStateColumn.setCellValueFactory(cellData -> cellData.getValue().getThreadState());
            threadStateColumn.setCellFactory(param -> new ThreadStateCell());
            threadNameColumn.setCellValueFactory(cellData -> cellData.getValue().getThreadName());

            threadFiltersPredicate = new ThreadFiltersPredicate(null);
            FilteredList<ThreadElementFx> filteredThreads = new FilteredList<>(threadsFx, threadFiltersPredicate);
            threadsTable.setItems(filteredThreads);
            threadsTable.getSelectionModel().selectedItemProperty().addListener(
                    (observable, oldValue, newValue) -> showThreadDetails(newValue));
        }

        public void setApp(ThreadRayApp app) {
            // threadsTable.setItems(threadsFx);
        }

        public void update(ThreadDump dump) {
            this.dump = dump;
            List<ThreadElement> threads = dump.getThreads();
            threadsCountLabel.setText(threads.size() + "");
            threadsFx.clear();
            threads.stream().forEach(t -> threadsFx.add(new ThreadElementFx(t)));
            threadDumpTextArea.setText(dump.getText());
        }

        private void showThreadDetails(ThreadElementFx thread) {
            threadDumpTextArea.setText(thread != null ? thread.getThreadDump() : dump.getText());
        }

        /**
         * Counts the occurrences of the input text in the thread dump
         *
         * @param text The text to search for
         * @return Count of this text in the thread dump
         */
        public int countMatches(String text) {
            return dump.countMatches(text);
        }
    }

    private static class ThreadStateCell extends TableCell<ThreadElementFx, ThreadElement.State> {
        Label stateLabel = new Label();

        public ThreadStateCell() {
            setGraphic(stateLabel);
        }

        @Override
        protected void updateItem(ThreadElement.State item, boolean empty) {
            if (item != null) {
                stateLabel.setText(item.getState().toString());
                stateLabel.getStyleClass().setAll(getCssStyle(item.getState().name()));
            } else {
                stateLabel.setText("");
                stateLabel.getStyleClass().setAll("state-unknown");
            }
        }

        private String getCssStyle(String state) {
            switch (state) {
                case "NEW":
                    return "state-new";
                case "RUNNABLE":
                    return "state-running";
                case "BLOCKED":
                    return "state-blocked";
                case "WAITING":
                    return "state-waiting";
                case "TIMED_WAITING":
                    return "state-timed-waiting";
                case "TERMINATED":
                    return "state-terminated";
                default:
                    return "state-unknown";
            }
        }
    }
}
