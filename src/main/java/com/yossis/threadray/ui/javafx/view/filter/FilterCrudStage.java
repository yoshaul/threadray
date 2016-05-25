package com.yossis.threadray.ui.javafx.view.filter;

import com.yossis.threadray.model.filter.WildcardFilter;
import com.yossis.threadray.ui.javafx.util.UI;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

/**
 * Stage to create or edit a filter.
 *
 * @author Yossi Shaul
 */
public class FilterCrudStage extends Stage {
    public FilterCrudStage(Stage parent, List<OnOffThreadFilter> filtersFx) {
        initModality(Modality.APPLICATION_MODAL);
        initOwner(parent);
        UI.setAppIcon(this);

        ComboBox<String> filterTypes = new ComboBox<>(FXCollections.observableArrayList("Wildcard", "Regexp"));
        filterTypes.getSelectionModel().selectFirst();

        TextField pattern = new TextField();

        Button addBtn = new Button("Add");
        addBtn.setOnAction(e -> {
            filtersFx.add(new OnOffThreadFilter(new WildcardFilter(pattern.getText())));
            FilterCrudStage.this.hide();
        });

        FlowPane root = new FlowPane(filterTypes, pattern, addBtn);

        Scene scene = new Scene(root);
        // close stage on escape
        scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {if (e.getCode() == KeyCode.ESCAPE) this.close();});
        setScene(scene);
        show();
    }
}
