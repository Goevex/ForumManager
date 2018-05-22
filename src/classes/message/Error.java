package classes.message;

import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;

public class Error extends Alert {
    public Error(String title, String header, String dialog) {
        super(AlertType.ERROR);
        this.setTitle(title);
        this.setHeaderText(header);
        if (dialog != "") {
            this.getDialogPane().setExpandableContent(new ScrollPane(new TextArea(dialog)));
        }
    }

    public Error(String title, String header) {
        this(title, header, "");
    }

    public static void showErr(String title, String header) {
        showErr(title, header, "");
    }

    public static void showErr(String title, String header, String dialog) {
        new Error(title, header, dialog).showAndWait();
    }
}
