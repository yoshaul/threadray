package com.yossis.threadray.ui.javafx.view.filter;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Stage to create or edit a filter.
 *
 * @author Yossi Shaul
 */
public class FilterCrudStage extends Stage {
    public FilterCrudStage(Stage parent) {
        initModality(Modality.APPLICATION_MODAL);
        initOwner(parent);

        Scene scene = new Scene(new Label("Filter"));
        // close stage on escape
        scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {if (e.getCode() == KeyCode.ESCAPE) this.close();});
        setScene(scene);
        show();
    }
}
