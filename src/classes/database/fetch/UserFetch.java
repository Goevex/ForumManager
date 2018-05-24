package classes.database.fetch;

import classes.database.Socket;
import classes.database.model.UserModel;
import classes.message.Error;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class UserFetch {
    public static ArrayList<UserModel> fetchUsers() {
        ArrayList<UserModel> users = new ArrayList<>();
        Connection conn = Socket.getConnection();
        Statement st = null;
        try {
            st = conn.createStatement();
            String query = "SELECT * FROM user";
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                UserModel user = new UserModel();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("Username"));
                user.setEmail(rs.getString("email"));
                user.setStatusId(rs.getInt("status"));
                user.setRoleId(0);
                user.setGender(rs.getInt("gender"));
                user.setFirstname(rs.getString("first_name"));
                user.setLastname(rs.getString("last_name"));
                user.setBirthday(rs.getString("birthday"));
                user.setLocation(rs.getString("location"));
                user.setWebsite(rs.getString("website"));
                user.setFacebook(rs.getString("facebook"));
                user.setSkype((rs.getString("skype")));
                user.setAbout(rs.getString("about"));
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            Error.showErr("Failure", "Could not get user information", e.toString());
        } finally {
            try {
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static UserModel fetchUserByUsername(String username) {
        Connection conn = Socket.getConnection();
        Statement st = null;
        try {
            st = conn.createStatement();

            String query = "SELECT * FROM user WHERE username = '" + username + "'";
            ResultSet rs = st.executeQuery(query);
            rs.last();
            if(rs.getRow() > 1){
                new Exception("Too many rows in ResultSet");
            }
            UserModel user = new UserModel();
            user.setId(rs.getInt("id"));
            user.setUsername(rs.getString("Username"));
            user.setEmail(rs.getString("email"));
            user.setStatusId(rs.getInt("status_id"));
            user.setRoleId(0);
            user.setGender(rs.getInt("gender"));
            user.setFirstname(rs.getString("first_name"));
            user.setLastname(rs.getString("last_name"));
            user.setBirthday(rs.getString("birthday"));
            user.setLocation(rs.getString("location"));
            user.setWebsite(rs.getString("website"));
            user.setFacebook(rs.getString("facebook"));
            user.setSkype((rs.getString("skype")));
            user.setAbout(rs.getString("about"));
            user.setLastUpdate(rs.getString("update_time"));
            user.setCreatedOn(rs.getString("create_time"));
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static ArrayList<String> getUsernames() {
        ArrayList<String> usernames = new ArrayList<>();
        Connection conn = Socket.getConnection();
        Statement st = null;
        try {
            st = conn.createStatement();

            String sql = "SELECT username FROM user";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                usernames.add(rs.getString("Username"));
            }
            return usernames;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
