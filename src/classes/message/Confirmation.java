package classes.message;

import classes.Constants;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Optional;

public class Confirmation extends Alert {
    public final int NONE = 1;
    public final int YES_NO = 2;

    public Confirmation(String title, String header, int dialog) {
        super(AlertType.CONFIRMATION);
        Stage stage = (Stage) this.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Constants.ICON_PATH));
        this.setTitle(title);
        this.setHeaderText(header);
        switch (dialog) {
            case NONE:
                break;
            case YES_NO:
                this.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
                break;
        }
    }

    public Confirmation(String title, String header) {
        this(title, header, 0);
    }

    public static Optional showConf(String title, String header) {
        return showConf(title, header, 0);
    }

    public static Optional showConf(String title, String header, int dialog) {
        return new Confirmation(title, header, dialog).showAndWait();
    }
}
