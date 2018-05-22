package classes;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;

public final class Settings {
    private static String sqlHost;
    private static String sqlPort;
    private static String sqlDatabase;
    private static String sqlUser;
    private static String sqlPassword;
    private static String webProt;
    private static String webHost;

    public static void saveToFile() {
        try {
            JSONObject root = new JSONObject();

            Field[] fields = Settings.class.getDeclaredFields();
            for (Field field : fields) {
                root.put(field.getName(), field.get(Settings.class));
            }

            String jsonString = root.toString();

            FileWriter fileWriter = new FileWriter(Constants.CONFIG_PATH);
            fileWriter.write(jsonString);
            fileWriter.flush();
            fileWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean loadFromFile() {

        File file = new File(Constants.CONFIG_PATH);
        String content;
        if (file.exists() && !file.isDirectory()) {
            try {
                content = FileUtils.readFileToString(file, "utf-8");

                JSONObject root = new JSONObject(content);

                Field[] fields = Settings.class.getDeclaredFields();
                for (Field field : fields) {
                    try {
                        field.set(Settings.class, root.getString(field.getName()));
                    } catch (Exception e) {
                        System.out.printf("Settings load failed: %s not found\n", field.getName());
                    }
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static String getSqlHost() {
        return sqlHost;
    }

    public static void setSqlHost(String sqlHost) {
        Settings.sqlHost = sqlHost;
    }

    public static String getSqlPort() {
        return sqlPort;
    }

    public static void setSqlPort(String sqlPort) {
        Settings.sqlPort = sqlPort;
    }

    public static String getSqlDatabase() {
        return sqlDatabase;
    }

    public static void setSqlDatabase(String sqlDatabase) {
        Settings.sqlDatabase = sqlDatabase;
    }

    public static String getSqlUser() {
        return sqlUser;
    }

    public static void setSqlUser(String sqlUser) {
        Settings.sqlUser = sqlUser;
    }

    public static String getSqlPassword() {
        return sqlPassword;
    }

    public static void setSqlPassword(String sqlPassword) {
        Settings.sqlPassword = sqlPassword;
    }

    public static String getWebProt() {
        return webProt;
    }

    public static void setWebProt(String webProt) {
        Settings.webProt = webProt;
    }

    public static String getWebHost() {
        return webHost;
    }

    public static void setWebHost(String webHost) {
        Settings.webHost = webHost;
    }
}
