package com.yossis.threadray.ui.javafx;

import com.yossis.threadray.ui.javafx.model.ThreadElementFx;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

/**
 * App main controller.
 *
 * @author Yossi Shaul
 */
public class ThreadsController {
    @FXML
    private TableView<ThreadElementFx> threadsTable;
    @FXML
    private TableColumn<ThreadElementFx, String> threadNameColumn;
    @FXML
    private Label threadNameLabel;
    @FXML
    private Label threadIdLabel;
    @FXML
    private TextArea threadDumpTextArea;

    @FXML
    private void initialize() {
        threadNameColumn.setCellValueFactory(cellData -> cellData.getValue().getThreadName());

        threadsTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showThreadDetails(newValue));
    }

    public void setApp(ThreadRayApp app) {
        threadsTable.setItems(app.getThreadsFx());
    }

    private void showThreadDetails(ThreadElementFx thread) {
        threadNameLabel.setText(thread != null ? thread.getThreadName().get() : "");
        threadNameLabel.setText(thread != null ? thread.getThreadName().get() : "");
        threadIdLabel.setText(thread != null ? thread.getThreadId() + "" : "");
        threadDumpTextArea.setText(thread != null ? thread.getThreadDump() : "");
    }
}
