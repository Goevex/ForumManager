package settings;

import classes.Settings;
import classes.Socket;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;

public class Controller {

    public Tab connectionTab;
    public TextField hostTxt;
    public TextField portTxt;
    public TextField databaseTxt;
    public TextField userTxt;
    public PasswordField passwordPtxt;
    public AnchorPane connectionAPane;

    public Controller() {
    }

    private void saveSettings() {
        Settings.setSqlHost(hostTxt.getText());
        Settings.setSqlPort(portTxt.getText());
        Settings.setSqlDatabase(databaseTxt.getText());
        Settings.setSqlUser(userTxt.getText());
        Settings.setSqlPassword(passwordPtxt.getText());
        Settings.saveToFile();
    }

    private void loadSettings() {
        hostTxt.setText(Settings.getSqlHost());
        portTxt.setText(Settings.getSqlPort());
        databaseTxt.setText(Settings.getSqlDatabase());
        userTxt.setText(Settings.getSqlUser());
        passwordPtxt.setText(Settings.getSqlPassword());
    }

    private void testConnection() {
        try {
            Socket.connect(hostTxt.getText(), portTxt.getText(), databaseTxt.getText(), userTxt.getText(), passwordPtxt.getText());
        } catch (SQLException e) {
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Connection failed");
            alert.setHeaderText("Could not connect to the database.");
            alert.getDialogPane().setExpandableContent(new ScrollPane(new TextArea(sw.toString())));
            alert.showAndWait();
        }
    }

    private void openConnection() {
        try {
            Socket.connect();
        } catch (SQLException e) {
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Connection failed");
            alert.setHeaderText("Could not connect to the database.");
            alert.getDialogPane().setExpandableContent(new ScrollPane(new TextArea(sw.toString())));
            alert.showAndWait();
        }
    }

    @FXML
    private void initialize() {
        loadSettings();
    }

    public void handleConnSaveBtnAction(ActionEvent actionEvent) {
        saveSettings();
        openConnection();
    }

    public void handleTestConBtn(ActionEvent actionEvent) {
        testConnection();
    }

    public void handleConnResetBtn(ActionEvent actionEvent) {
        for( Node node: connectionAPane.getChildren()) {
            if( node instanceof TextField) {
                ((TextField) node).setText("");
            }
        }
    }
}
