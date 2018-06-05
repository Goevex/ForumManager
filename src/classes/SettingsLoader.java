package classes;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;

public final class SettingsLoader {
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

            Field[] fields = SettingsLoader.class.getDeclaredFields();
            for (Field field : fields) {
                root.put(field.getName(), field.get(SettingsLoader.class));
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

                Field[] fields = SettingsLoader.class.getDeclaredFields();
                for (Field field : fields) {
                    try {
                        field.set(SettingsLoader.class, root.getString(field.getName()));
                    } catch (Exception e) {
                        System.out.printf("SettingsLoader load failed: %s not found\n", field.getName());
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
        SettingsLoader.sqlHost = sqlHost;
    }

    public static String getSqlPort() {
        return sqlPort;
    }

    public static void setSqlPort(String sqlPort) {
        SettingsLoader.sqlPort = sqlPort;
    }

    public static String getSqlDatabase() {
        return sqlDatabase;
    }

    public static void setSqlDatabase(String sqlDatabase) {
        SettingsLoader.sqlDatabase = sqlDatabase;
    }

    public static String getSqlUser() {
        return sqlUser;
    }

    public static void setSqlUser(String sqlUser) {
        SettingsLoader.sqlUser = sqlUser;
    }

    public static String getSqlPassword() {
        return sqlPassword;
    }

    public static void setSqlPassword(String sqlPassword) {
        SettingsLoader.sqlPassword = sqlPassword;
    }

    public static String getWebProt() {
        return webProt;
    }

    public static void setWebProt(String webProt) {
        SettingsLoader.webProt = webProt;
    }

    public static String getWebHost() {
        return webHost;
    }

    public static void setWebHost(String webHost) {
        SettingsLoader.webHost = webHost;
    }
}
