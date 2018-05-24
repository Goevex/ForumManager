package user;

import classes.database.model.UserModel;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static classes.database.fetch.UserFetch.fetchUserByUsername;
import static classes.database.fetch.UserFetch.getUsernames;

public class Controller {
    private final ObservableList<String> statusItems = FXCollections.observableArrayList(
            "Active", "Inactive");
    private final ObservableList<String> roleItems = FXCollections.observableArrayList();
    private final ObservableList<String> genderItems = FXCollections.observableArrayList(
            "Prefer not to say", "Male", "Female", "Other");
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
    public Label lastUpdateDBLabel;
    public Label createdOnLabel;

    ObservableList<String> userEntries = FXCollections.observableArrayList();
    UserModel currentUser;

    public Controller() {
    }

    @FXML
    private void initialize() {
        usersLV.getSelectionModel().selectedItemProperty().addListener((ChangeListener<String>) (observable, oldValue, newValue) -> {
            if (newValue != null) {
                refreshUser(newValue);
            }
        });
        searchUsernameTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            searchUserLV(oldValue, newValue);
        });

        statusDBCoiceB.setItems(statusItems);
        roleDBChoiceB.setItems(roleItems);
        genderDBChoiceB.setItems(genderItems);

        refreshUsersLV();
    }

    private void refreshUsersLV() {
        usersLV.getItems().clear();
        ArrayList<String> usernames = getUsernames();
        for (String username : usernames) {
            userEntries.add(username);
            usersLV.getItems().add(username);
        }
    }

    private void refreshUser(String username) {
        UserModel user = fetchUserByUsername(username);
        idDBTxt.setText(String.valueOf(user.getId()));
        usernameDbTxt.setText(user.getUsername());
        emailDBtxt.setText(user.getEmail());
        statusDBCoiceB.getSelectionModel().select(user.getStatusId());
        roleDBChoiceB.getSelectionModel().select(0);
        createdOnLabel.setText(user.getCreatedOn());
        lastUpdateDBLabel.setText(user.getLastUpdate());
        genderDBChoiceB.getSelectionModel().select(user.getGender());
        firstnameDBTxt.setText(user.getFirstname());
        lastnameDBtxt.setText(user.getLastname());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birtdayDate = LocalDate.parse(user.getBirthday() != null ? user.getBirthday() : "1900-01-01", formatter);
        birthdayDBDPicker.setValue(birtdayDate);
        locationDBTxt.setText(user.getLocation());
        websiteDBTxt.setText(user.getWebsite());
        facebookDBTxt.setText(user.getFacebook());
        skypeDBTxt.setText(user.getSkype());
        aboutDBTxtarea.setText(user.getAbout());
        currentUser = user;
    }

    private void searchUserLV(String oldValue, String newValue) {
        if (oldValue != null && (newValue.length() < oldValue.length())) {
            usersLV.setItems(userEntries);
        }
        String value = newValue.toUpperCase();
        ObservableList<String> subentries = FXCollections.observableArrayList();
        for (Object entry : usersLV.getItems()) {
            boolean match = true;
            String entryText = (String) entry;
            if (!entryText.toUpperCase().contains(value)) {
                match = false;
                continue;
            }
            if (match) {
                subentries.add(entryText);
            }
        }
        usersLV.setItems(subentries);
    }

    public void handleSaveDBBtn(ActionEvent actionEvent) {

    }
}
