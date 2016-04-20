package com.yossis.threadray.ui.javafx.view;

import com.yossis.threadray.ui.javafx.util.UI;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * A panel to display and edit thread filters.
 *
 * @author Yossi Shaul
 */
public class FiltersStage extends Stage {

    private final TableView<StringProperty> table;

    public FiltersStage(Stage mainStage) {
        initModality(Modality.APPLICATION_MODAL);
        initOwner(mainStage);
        UI.setAppIcon(this);

        TableColumn nameCol = new TableColumn<>("Name");
        nameCol.setPrefWidth(25);
        TableColumn valueCol = new TableColumn<>("Value");
        valueCol.setPrefWidth(75);

        table = new TableView<>();
        table.getColumns().addAll(nameCol, valueCol);
        nameCol.prefWidthProperty().bind(table.widthProperty().multiply(0.25));
        valueCol.prefWidthProperty().bind(table.widthProperty().multiply(0.75));

        Scene scene = new Scene(table, 400, 400);
        // close stage on escape
        scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {if (e.getCode() == KeyCode.ESCAPE) this.close();});
        setScene(scene);

        ObservableList<StringProperty> filtersFx = FXCollections.observableArrayList();
        filtersFx.addAll(new SimpleStringProperty("Test"));
        Controller controller = new Controller(filtersFx, nameCol, valueCol);

        show();
    }

    private class Controller {
        private ObservableList<StringProperty> filtersFx;

        public Controller(ObservableList<StringProperty> filtersFx, TableColumn<StringProperty, String> nameCol,
                TableColumn<StringProperty, String> valueCol) {
            this.filtersFx = filtersFx;
            table.setItems(filtersFx);
            nameCol.setCellValueFactory(cellData -> cellData.getValue());
            valueCol.setCellValueFactory(cellData -> cellData.getValue());
        }
    }
}
