package classes.database;

import classes.SettingsLoader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionBuilder {
    public static Connection connect() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager
                .getConnection("jdbc:mysql://" + SettingsLoader.getSqlHost() + ":" + SettingsLoader.getSqlPort() + "/" + SettingsLoader.getSqlDatabase(), SettingsLoader.getSqlUser(), SettingsLoader.getSqlPassword());
    }

    public static Connection connect(String host, String port, String database, String user, String password) throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager
                .getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, user, password);
    }
}
