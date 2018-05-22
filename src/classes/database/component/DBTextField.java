package classes.database.component;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TextField;


public class DBTextField extends TextField {
    private final StringProperty field = new SimpleStringProperty();
    private final StringProperty result = new SimpleStringProperty();

    public DBTextField() {
        super();
    }

    public final StringProperty fieldProperty() {
        return field;
    }

    public final String getField() {
        return field.get();
    }

    public final void setField(String field) {
        this.field.set(field);
    }

    public final StringProperty resultProperty() {
        return result;
    }

    public final String getResult() {
        return result.get();
    }

    public final void setResult(String field) {
        this.result.set(field);
    }
}
