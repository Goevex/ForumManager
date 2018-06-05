package classes.database;

import classes.Constants;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.*;

import java.util.ArrayList;

public class DBComponentHelper {
    Runnable onChangeFunc;
    ArrayList<Control> controls;

    public DBComponentHelper(Runnable onChange) {
        this.onChangeFunc = onChange;
        controls = new ArrayList<>();
    }

    public void addControl(Control control) {
        ChangeListener onChangeWhileFocus = (observable, oldValue, newValue) -> {
            if (controlHasFocus(control)) {
                onChangeFunc.run();
            }
        };
        if (control instanceof TextField) {
            ((TextField) control).textProperty().addListener(onChangeWhileFocus);
        }
        if (control instanceof DatePicker) {
            ((DatePicker) control).valueProperty().addListener(onChangeWhileFocus);
        }
        if (control instanceof ChoiceBox) {
            ((ChoiceBox) control).getSelectionModel().selectedItemProperty().addListener(onChangeWhileFocus);
        }
        if (control instanceof TextArea) {
            ((TextArea) control).textProperty().addListener(onChangeWhileFocus);
        }
        if (control instanceof PasswordField) {
            ((PasswordField) control).textProperty().addListener(onChangeWhileFocus);
        }
        controls.add(control);
    }

    private boolean controlHasFocus(Control control) {
        return control.getScene().focusOwnerProperty().get() == control;
    }

    public void setControls(ArrayList<Control> dbControls) {
        this.controls = dbControls;
    }
}
