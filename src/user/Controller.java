package user;

import classes.Socket;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.sql.ResultSet;

public class Controller {

    public TextField searchUsernameTxt;
    public ListView usersLV;

    public Controller() {
    }

    @FXML
    private void initialize() {
        ResultSet users = Socket.getAll("user");
        try {
            int index = 0;
            while (users.next()) {
                ObservableList<String> items =FXCollections.observableArrayList ();
                items.add(users.getString(index));
                usersLV.setItems(items);
                index++;
            }
        } catch (Exception e) {

        } finally {

        }
    }
}
