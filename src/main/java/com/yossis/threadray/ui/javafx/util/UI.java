package com.yossis.threadray.ui.javafx.util;

import com.yossis.threadray.util.Resources;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Utility class for UI actions.
 *
 * @author Yossi Shaul
 */
public abstract class UI {

    public static void setAppIcon(Stage stage) {
        stage.getIcons().add(new Image(Resources.getStream("/ui/icons/icon_016.png")));
        stage.getIcons().add(new Image(Resources.getStream("/ui/icons/icon_032.png")));
    }
}