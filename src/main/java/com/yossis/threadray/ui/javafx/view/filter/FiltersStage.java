package com.yossis.threadray.ui.javafx.view.filter;

import com.yossis.threadray.model.filter.ThreadFilter;
import com.yossis.threadray.ui.javafx.model.ObservableThreadDump;
import com.yossis.threadray.ui.javafx.util.UI;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * A panel to display and edit thread filters.
 *
 * @author Yossi Shaul
 */
public class FiltersStage extends Stage {

    private final TableView<ThreadFilter> table;

    public FiltersStage(Stage mainStage, ObservableThreadDump threadDump) {
        initModality(Modality.APPLICATION_MODAL);
        initOwner(mainStage);
        UI.setAppIcon(this);

        Button addBtn = new Button("Add...");
        addBtn.setOnAction(e -> new FilterCrudStage(this, threadDump.getFilters()));
        HBox toolbar = new HBox(10, addBtn);

        TableColumn nameCol = new TableColumn<>("Type");
        nameCol.setPrefWidth(25);
        TableColumn valueCol = new TableColumn<>("Value");
        valueCol.setPrefWidth(75);

        table = new TableView<>();
        table.getColumns().addAll(nameCol, valueCol);
        nameCol.prefWidthProperty().bind(table.widthProperty().multiply(0.25));
        valueCol.prefWidthProperty().bind(table.widthProperty().multiply(0.75));

        VBox root = new VBox(toolbar, table);
        Scene scene = new Scene(root, 400, 400);
        // close stage on escape
        scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {if (e.getCode() == KeyCode.ESCAPE) this.close();});
        setScene(scene);

        new Controller(threadDump, nameCol, valueCol);

        show();
    }

    private class Controller {

        Controller(ObservableThreadDump threadDump,
                TableColumn<ThreadFilter, String> typeCol,
                TableColumn<ThreadFilter, String> valueCol) {

            table.setItems(threadDump.getFilters());
            typeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));
            valueCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().toString()));
        }
    }
}
