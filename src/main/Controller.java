package main;

import classes.Constants;
import classes.GlyphBuilder;
import classes.SettingsLoader;
import classes.database.ConnectionBuilder;
import javafx.application.Application;
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
        if (!SettingsLoader.loadFromFile()) {
            SettingsLoader.saveToFile();
        }

        try {
            ConnectionBuilder.connect();
        } catch (SQLException e) {
            openSettings();
        }

        userBtn.setGraphic(GlyphBuilder.get("USER", 48));
        settingsBtn.setGraphic(GlyphBuilder.get("GEAR", 48));
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

    public void handlecloseMenuItemActionC(ActionEvent actionEvent) {
        System.exit(0);
    }
}
