package classes.database.component;

import classes.Constants;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class ComponentDBHelper {
    Runnable onChangeFunc;
    ArrayList<Control> controls;

    public ComponentDBHelper(Runnable onChange) {
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
        controls.add(control);
    }

    private boolean controlHasFocus(Control control) {
        return control.getScene().focusOwnerProperty().get() == control;
    }

    public void setControls(ArrayList<Control> dbControls) {
        this.controls = dbControls;
    }
}
