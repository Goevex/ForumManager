package settings;

import classes.Constants;
import classes.SettingsLoader;
import classes.ManagerTask;
import classes.database.ConnectionBuilder;
import classes.message.Confirmation;
import classes.message.Error;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.codehaus.plexus.util.ExceptionUtils;

import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
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
    private Thread testConnectionThread;
    private Thread testWebsiteThread;
    private Thread openConnectionThread;

    public Controller() {
    }

    private void saveSettings() {
        SettingsLoader.setSqlHost(hostTxt.getText());
        SettingsLoader.setSqlPort(portTxt.getText());
        SettingsLoader.setSqlDatabase(databaseTxt.getText());
        SettingsLoader.setSqlUser(userTxt.getText());
        SettingsLoader.setSqlPassword(passwordPtxt.getText());
        RadioButton protRadio = (RadioButton) (protocolTG.getSelectedToggle());
        if (protRadio != null) {
            SettingsLoader.setWebProt(protRadio.getText());
        }
        SettingsLoader.setWebHost(webHostTxt.getText());
        SettingsLoader.saveToFile();
    }

    private void loadSettings() {
        loadDatabaseSettings();
        loadWebsiteSettings();
    }

    private void loadDatabaseSettings() {
        hostTxt.setText(SettingsLoader.getSqlHost());
        portTxt.setText(SettingsLoader.getSqlPort());
        databaseTxt.setText(SettingsLoader.getSqlDatabase());
        userTxt.setText(SettingsLoader.getSqlUser());
        passwordPtxt.setText(SettingsLoader.getSqlPassword());
    }

    private void loadWebsiteSettings() {
        protHttpRadio.setSelected(false);
        protHttpsRadio.setSelected(false);
        if (SettingsLoader.getWebProt() != null) {
            switch (SettingsLoader.getWebProt()) {
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
        webHostTxt.setText(SettingsLoader.getWebHost());
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
            if (node instanceof TextArea) {
                ((RadioButton) node).setText("");
            }
        }
    }

    private void testConnection() {
        ManagerTask testConnectionTask = new ManagerTask() {
            @Override
            protected Object call() throws Exception {
                connectionTab.getContent().getScene().setCursor(Cursor.WAIT);
                Connection testC = null;
                try {
                    testC = ConnectionBuilder.connect(hostTxt.getText(), portTxt.getText(), databaseTxt.getText(), userTxt.getText(), passwordPtxt.getText());
                } catch (SQLException e) {
                    setErrorTrace(ExceptionUtils.getStackTrace(e));
                } finally {
                    connectionTab.getContent().getScene().setCursor(Cursor.DEFAULT);
                    try {
                        testC.close();
                    } catch (SQLException e) {
                    }
                }
                return null;
            }

            @Override
            protected void succeeded() {
                Confirmation.showConf("Connection Confirmation", "Connection has been successful.");
            }

            @Override
            protected void failed() {
                Error.showErr("Connection failed", "Could not connect to the database.", getErrorTrace());
            }
        };
        if (testConnectionThread == null || !testConnectionThread.isAlive()) {
            testConnectionThread = new Thread(testConnectionTask, Constants.TASK_TEST_CONNECTION);
            testConnectionThread.start();
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
