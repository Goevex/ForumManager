package user;

import classes.Constants;
import classes.Settings;
import classes.database.component.ComponentDBHelper;
import classes.database.model.UserModel;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.converter.IntegerStringConverter;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.function.UnaryOperator;

import static classes.database.fetch.UserFetch.*;

public class Controller {
    private final ObservableList<String> statusItems = FXCollections.observableArrayList(
            "Active", "Inactive");
    private final ObservableList<String> roleItems = FXCollections.observableArrayList();
    private final ObservableList<String> genderItems = FXCollections.observableArrayList(
            "Prefer not to say", "Male", "Female", "Other");
    public TextField searchUsernameTxt;
    public ListView usersLV;
    public TextField idDBTxt;
    public TextField usernameDBTxt;
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
    public Label lastUpdateLabel;
    public Label createdOnLabel;
    public Button saveDBBtn;
    public AnchorPane userPanel;
    public Button resetDBBtn;
    public TextField steamIDDBTxt;
    public Hyperlink websiteHyperlink;

    private ComponentDBHelper componentHelper;
    private ObservableList<String> userEntries = FXCollections.observableArrayList();
    private UserModel currentUser;
    private int dataStatus;

    public Controller() {
    }

    @FXML
    private void initialize() {
        currentUser = new UserModel();
        dataStatus = Constants.STATUS_BROWSE;
        usersLV.getSelectionModel().selectedItemProperty().addListener((ChangeListener<String>) (observable, oldValue, newValue) -> {
            if (newValue != null) {
//                final boolean[] moveOn = {true};
//                if (dataStatus == Constants.STATUS_EDIT) {
//                    Confirmation.showConf("Data not saved", "Input data are not saved yet and will be lost on moving on. Do you want to continue?").ifPresent(new Consumer() {
//                        @Override
//                        public void accept(Object o) {
//                            moveOn[0] = false;
//                        }
//                    });
//                }
//                if (!moveOn[0]) {
//                    return;
//                }
                userPanel.setDisable(false);
                refreshUser(newValue);
                changeStatus(Constants.STATUS_BROWSE);
            }
        });
        searchUsernameTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            searchUserLV(oldValue, newValue);
        });

        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("-?([1-9][0-9]*)?")) {
                return change;
            }
            return null;
        };

        idDBTxt.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, integerFilter));

        componentHelper = new ComponentDBHelper(new Runnable() {
            @Override
            public void run() {
                changeStatus(Constants.STATUS_EDIT);
            }
        });
        componentHelper.addControl(idDBTxt);
        componentHelper.addControl(usernameDBTxt);
        componentHelper.addControl(emailDBtxt);
        componentHelper.addControl(steamIDDBTxt);
        componentHelper.addControl(firstnameDBTxt);
        componentHelper.addControl(lastnameDBtxt);
        componentHelper.addControl(websiteDBTxt);
        componentHelper.addControl(skypeDBTxt);
        componentHelper.addControl(aboutDBTxtarea);
        componentHelper.addControl(birthdayDBDPicker);
        componentHelper.addControl(locationDBTxt);
        componentHelper.addControl(facebookDBTxt);
        componentHelper.addControl(statusDBCoiceB);
        componentHelper.addControl(roleDBChoiceB);
        componentHelper.addControl(genderDBChoiceB);


        userPanel.setDisable(true);
        saveDBBtn.setDisable(true);
        resetDBBtn.setDisable(true);
        createdOnLabel.setText("");
        lastUpdateLabel.setText("");
        websiteHyperlink.setText("");
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
        UserModel user = getUserByUsername(username);
        idDBTxt.setText(String.valueOf(user.getId()));
        usernameDBTxt.setText(user.getUsername());
        emailDBtxt.setText(user.getEmail());
        statusDBCoiceB.getSelectionModel().select(user.getStatusId()-1);
        roleDBChoiceB.getSelectionModel().select(0);
        createdOnLabel.setText(user.getCreatedOn());
        lastUpdateLabel.setText(user.getLastUpdate());
        steamIDDBTxt.setText(user.getSteamID());
        genderDBChoiceB.getSelectionModel().select(user.getGender()-1);
        firstnameDBTxt.setText(user.getFirstname());
        lastnameDBtxt.setText(user.getLastname());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (user.getBirthday() != "") {
            LocalDate birtdayDate = LocalDate.parse(user.getBirthday(), formatter);
            birthdayDBDPicker.setValue(birtdayDate);
        } else {
            birthdayDBDPicker.setValue(null);
        }
        locationDBTxt.setText(user.getLocation());
        websiteDBTxt.setText(user.getWebsite());
        facebookDBTxt.setText(user.getFacebook());
        skypeDBTxt.setText(user.getSkype());
        aboutDBTxtarea.setText(user.getAbout());
        websiteHyperlink.setText(Settings.getWebProt() + "://" + Settings.getWebHost() + "/profile/" + user.getUsername());

        currentUser = user;
        resetDBBtn.setDisable(true);
        saveDBBtn.setDisable(true);
    }

    private UserModel inputToUserModel() {
        UserModel inputUser = new UserModel();
        inputUser.setId(Integer.parseInt(idDBTxt.getText()));
        inputUser.setUsername(usernameDBTxt.getText());
        inputUser.setEmail(emailDBtxt.getText());
        inputUser.setStatusId(statusDBCoiceB.getSelectionModel().getSelectedIndex()+1);
//        roleDBChoiceB.getSelectionModel().getSelectedIndex(0);
        inputUser.setCreatedOn(createdOnLabel.getText());
        inputUser.setGender(genderDBChoiceB.getSelectionModel().getSelectedIndex()+1);
        inputUser.setFirstname(firstnameDBTxt.getText());
        inputUser.setLastname(lastnameDBtxt.getText());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        inputUser.setBirthday(birthdayDBDPicker.getValue() != null ? birthdayDBDPicker.getValue().format(formatter) : "");
        inputUser.setLocation(locationDBTxt.getText());
        inputUser.setWebsite(websiteDBTxt.getText());
        inputUser.setFacebook(facebookDBTxt.getText());
        inputUser.setSkype(skypeDBTxt.getText());
        inputUser.setAbout(aboutDBTxtarea.getText());
        inputUser.setSteamID(steamIDDBTxt.getText());

        return inputUser;
    }

    private boolean inputEqualsCurrentUser() {
        return (currentUser.eq(inputToUserModel()));
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

    private ArrayList<Control> getDBControls() {
        ArrayList<Control> controls = new ArrayList<>();
        Field[] cumFields = this.getClass().getDeclaredFields();
        for (Field field : cumFields) {
            if (field.getName().contains("DB")) {
                try {
                    controls.add((Control) field.get(this));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return controls;
    }

    private void changeStatus(int newStatus) {
        switch (newStatus) {
            case Constants.STATUS_EDIT:
                if (inputEqualsCurrentUser()) {
                    saveDBBtn.setDisable(true);
                    resetDBBtn.setDisable(true);
                    dataStatus = Constants.STATUS_BROWSE;
                } else {
                    saveDBBtn.setDisable(false);
                    resetDBBtn.setDisable(false);
                    dataStatus = newStatus;
                }
                break;
                case Constants.STATUS_BROWSE:{
                    saveDBBtn.setDisable(true);
                    resetDBBtn.setDisable(true);
                    break;
                }
            default:
                dataStatus = newStatus;
                break;
        }
    }

    public void handleSaveDBBtn(ActionEvent actionEvent) {
        if (updateUser(inputToUserModel())) {
            changeStatus(Constants.STATUS_BROWSE);
            currentUser = inputToUserModel();
        } else {
            changeStatus(Constants.STATUS_EDIT);
        }
    }

    public void handleResetDBBtnAction(ActionEvent actionEvent) {
        refreshUser(currentUser.getUsername());
    }

    public void handleWebsiteHyperlinkAction(ActionEvent actionEvent) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI(websiteHyperlink.getText()));
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (URISyntaxException e1) {
                e1.printStackTrace();
            }
        }
    }
}
