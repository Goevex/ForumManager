package classes.message;

import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;

public class Confirmation extends Alert {
    public Confirmation(String title, String header, String dialog) {
        super(AlertType.CONFIRMATION);
        this.setTitle(title);
        this.setHeaderText(header);
        if (dialog != "") {
            this.getDialogPane().setExpandableContent(new ScrollPane(new TextArea(dialog)));
        }
    }

    public Confirmation(String title, String header) {
        this(title, header, "");
    }

    public static void showConf(String title, String header) {
        showConf(title, header, "");
    }

    public static void showConf(String title, String header, String dialog) {
        new Confirmation(title, header, dialog).showAndWait();
    }
}
