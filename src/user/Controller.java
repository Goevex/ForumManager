package user;

import classes.database.Socket;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Controller {
    private final ObservableList<String> statusItems = FXCollections.observableArrayList(
            "Active", "Inactive");
    private final ObservableList<String> roleItems = FXCollections.observableArrayList();
    private final ObservableList<String> genderItems = FXCollections.observableArrayList(
            "Prefer not to say", "Male", "Female", "Other");
    public Result<Record> user;
    public TextField searchUsernameTxt;
    public ListView usersLV;
    public TextField idDBTxt;
    public TextField usernameDbTxt;
    public PasswordField newPassTxt;
    public TextField firstnameDBTxt;
    public TextField lastnameDBtxt;
    public TextField locationDBTxt;
    public DatePicker birthdayDBDPicker;
    public TextField skypeDBTxt;
    public TextField emailDBtxt;
    public ChoiceBox statusDBCoiceB;
    public ChoiceBox roleDBChoiceB;
    public TextField websiteDBTxt;
    public TextField facebookDBTxt;
    public ChoiceBox genderDBChoiceB;
    public TextArea aboutDBTxtarea;
    public Button savePassBtn;


    public Controller() {
    }

    @FXML
    private void initialize() {
        statusDBCoiceB.setItems(statusItems);
        roleDBChoiceB.setItems(roleItems);
        genderDBChoiceB.setItems(genderItems);

        refreshUsersLV();
    }

    private void refreshUsersLV() {
        try {
            ResultSet usersRS = fetch("SELECT username FROM user");
            usersRS.first();
            while (usersRS.next()) {
                usersLV.getItems().add(usersRS.getString("Username"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void refreshUser(String username) {
//        try {
//            usersRS.first();
//            while (usersRS.next()) {
//                if (usersRS.getString("username").equals(username)) {
//                    idDBTxt.setText(usersRS.getString("id"));
//                    usernameDbTxt.setText(usersRS.getString("Username"));
//                    emailDBtxt.setText(usersRS.getString(String.valueOf(emailDBtxt)));
//                    statusDBCoiceB.getSelectionModel().select(usersRS.getInt("status"));
//                    roleDBChoiceB.getSelectionModel().select(0);
//                    genderDBChoiceB.getSelectionModel().select(usersRS.getInt("gender"));
//                    firstnameDBTxt.setText(usersRS.getString("Username"));
//                    lastnameDBtxt.setText(usersRS.getString("Username"));
//                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//                    LocalDate birtdayDate = LocalDate.parse(usersRS.getString("birthday"), formatter);
//                    birthdayDBDPicker.setValue(birtdayDate);
//                    locationDBTxt.setText(usersRS.getString("location"));
//                    websiteDBTxt.setText(usersRS.getString("website"));
//                    facebookDBTxt.setText(usersRS.getString("facebook"));
//                    skypeDBTxt.setText((usersRS.getString("skype")));
//                    aboutDBTxtarea.setText(usersRS.getString("about"));
//                } else if (usersRS.isLast()) {
//                    Confirmation.showConf("Not Found", "User " + username + " doesn't exist");
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }

    private ResultSet fetchJOOQ(String sql) {
        DSLContext con = DSL.using(Socket.getConnection(), SQLDialect.MYSQL);

        return null;
    }

    private ResultSet fetch(String sql) {
        Connection conn = Socket.getConnection();
        Statement st = null;
        try {
            st = conn.createStatement();

            ResultSet rs = st.executeQuery(sql);
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
//            try {
//                st.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
        }
        return null;
    }

    private ResultSet fetchUsers() {
        Connection conn = Socket.getConnection();
        Statement st = null;
        try {
            st = conn.createStatement();

            String query = "SELECT * FROM user";
            return st.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private ResultSet fetchUser(int id) {
        Connection conn = Socket.getConnection();
        Statement st = null;
        try {
            st = conn.createStatement();

            String query = "SELECT * FROM user WHERE id = " + id;
            return st.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void handleSaveDBBtn(ActionEvent actionEvent) {
        fetchUsers();
    }
}
