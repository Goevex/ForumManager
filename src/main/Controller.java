package main;

import classes.Constants;
import classes.FontAwesome;
import classes.Settings;
import classes.database.Socket;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.SQLException;

public class Controller {
    public Button userBtn;
    public Button settingsBtn;

    public Controller() {
    }

    @FXML
    private void initialize() {
        if (!Settings.loadFromFile()) {
            Settings.saveToFile();
        }

        try {
            Socket.connect();
        } catch (SQLException e) {
            openSettings();
        }

        userBtn.setGraphic(FontAwesome.get("USER", 48));
        settingsBtn.setGraphic(FontAwesome.get("GEAR", 48));
    }

    private void openUser() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/user/user.fxml"));
            Stage userStage = new Stage();
            userStage.getIcons().add(new Image(Constants.ICON_PATH));
            userStage.setTitle("User");
            userStage.setScene(new Scene(root));
            userStage.initModality(Modality.APPLICATION_MODAL);
            userStage.showAndWait();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private void openSettings() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/settings/settings.fxml"));
            Stage settingsStage = new Stage();
            settingsStage.getIcons().add(new Image(Constants.ICON_PATH));
            settingsStage.setTitle("Settings");
            settingsStage.setScene(new Scene(root));
            settingsStage.initModality(Modality.APPLICATION_MODAL);
            settingsStage.showAndWait();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void handleUserBtnAction() {
        openUser();
    }

    public void handleSaveBtnAction(ActionEvent actionEvent) {
        openSettings();
    }
}
