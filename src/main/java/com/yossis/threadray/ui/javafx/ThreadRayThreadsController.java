package com.yossis.threadray.ui.javafx;

import com.yossis.threadray.model.ThreadElement;
import com.yossis.threadray.ui.javafx.model.ThreadElementFx;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

import java.util.List;
import java.util.stream.Collectors;

/**
 * App main controller.
 *
 * @author Yossi Shaul
 */
@Deprecated
public class ThreadRayThreadsController {
    private ObservableList<ThreadElementFx> threadsFx = FXCollections.observableArrayList();

    @FXML
    private TableView<ThreadElementFx> threadsTable;
    @FXML
    private TableColumn<ThreadElementFx, String> threadNameColumn;
    @FXML
    private TableColumn<ThreadElementFx, String> threadStateColumn;
    @FXML
    private Label threadNameLabel;
    @FXML
    private Label threadIdLabel;
    @FXML
    private Label threadsCountLabel;
    @FXML
    private TextArea threadDumpTextArea;

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
