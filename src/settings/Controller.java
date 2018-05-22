package settings;

import classes.Settings;
import classes.database.Socket;
import classes.message.Confirmation;
import classes.message.Error;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.codehaus.plexus.util.ExceptionUtils;

import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;

public class Controller {

    public Tab connectionTab;
    public TextField hostTxt;
    public TextField portTxt;
    public TextField databaseTxt;
    public TextField userTxt;
    public PasswordField passwordPtxt;
    public AnchorPane connectionAPane;
    public AnchorPane websiteAPane;
    public RadioButton protHttpRadio;
    public RadioButton protHttpsRadio;
    public TextField webHostTxt;
    public ToggleGroup protocolTG;

    public Controller() {
    }

    private void saveSettings() {
        Settings.setSqlHost(hostTxt.getText());
        Settings.setSqlPort(portTxt.getText());
        Settings.setSqlDatabase(databaseTxt.getText());
        Settings.setSqlUser(userTxt.getText());
        Settings.setSqlPassword(passwordPtxt.getText());
        RadioButton protRadio = (RadioButton) (protocolTG.getSelectedToggle());
        if (protRadio != null) {
            Settings.setWebProt(protRadio.getText());
        }
        Settings.setWebHost(webHostTxt.getText());
        Settings.saveToFile();
    }

    private void loadSettings() {
        loadDatabaseSettings();
        loadWebsiteSettings();
    }

    private void loadDatabaseSettings() {
        hostTxt.setText(Settings.getSqlHost());
        portTxt.setText(Settings.getSqlPort());
        databaseTxt.setText(Settings.getSqlDatabase());
        userTxt.setText(Settings.getSqlUser());
        passwordPtxt.setText(Settings.getSqlPassword());
    }

    private void loadWebsiteSettings() {
        protHttpRadio.setSelected(false);
        protHttpsRadio.setSelected(false);
        if (Settings.getWebProt() != null) {
            switch (Settings.getWebProt()) {
                case "http":
                    protHttpRadio.setSelected(true);
                    break;
                case "https":
                    protHttpsRadio.setSelected(true);
                    break;
                default:
                    break;
            }
        }
        webHostTxt.setText(Settings.getWebHost());
    }

    private void clearInputInPane(Pane pane) {
        for (Node node : pane.getChildren()) {
            if (node instanceof TextField) {
                ((TextField) node).setText("");
            }
            if (node instanceof PasswordField) {
                ((PasswordField) node).setText("");
            }
            if (node instanceof RadioButton) {
                ((RadioButton) node).setSelected(false);
            }
        }
    }

    private void testConnection() {
        try {
            Socket.connect(hostTxt.getText(), portTxt.getText(), databaseTxt.getText(), userTxt.getText(), passwordPtxt.getText());
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Connection Confirmation");
            alert.setHeaderText("Connection has been successful.");
            alert.showAndWait();
        } catch (SQLException e) {
            Error.showErr("Connection failed", "Could not connect to the database.", ExceptionUtils.getStackTrace(e));
        } finally {
            Socket.close();
        }
    }

    private void openConnection() {
        try {
            Socket.connect();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Connection Confirmation");
            alert.setHeaderText("Connection has been successful.");
            alert.showAndWait();
        } catch (SQLException e) {
            Error.showErr("Connection failed", "Could not connect to the database.", ExceptionUtils.getStackTrace(e));
        } finally {
            Socket.close();
        }
    }

    private void testWebsite() {
        URL url = null;
        String protocol = "";
        RadioButton protRadio = (RadioButton) (protocolTG.getSelectedToggle());
        if (protRadio != null) {
            protocol = protRadio.getText();
        }
        try {
            url = new URL(protocol + "://" + webHostTxt.getText());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new Exception("Invalid HTTP Status");
            } else {
                Confirmation.showConf("Request successful", "Requested website successfully");
            }
        } catch (Exception e) {
            Error.showErr("Connection failed", "Could not connect to the website.", ExceptionUtils.getStackTrace(e));
        }
    }

    @FXML
    private void initialize() {
        loadSettings();
    }

    public void handleSaveBtnAction(ActionEvent actionEvent) {
        saveSettings();
    }

    public void handleTestConnBtn(ActionEvent actionEvent) {
        testConnection();
    }

    public void handleConnResetBtn(ActionEvent actionEvent) {
        loadSettings();
    }


    public void handleWebResetBtn(ActionEvent actionEvent) {
        loadSettings();
    }

    public void handleWebClearBtn(ActionEvent actionEvent) {
        clearInputInPane(websiteAPane);
    }

    public void handleConnClearBtn(ActionEvent actionEvent) {
        clearInputInPane(connectionAPane);
    }

    public void handleTestWebBtn(ActionEvent actionEvent) {
        testWebsite();
    }
}
