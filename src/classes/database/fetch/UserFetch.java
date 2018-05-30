package classes.database.fetch;

import classes.database.Socket;
import classes.database.model.UserModel;
import classes.message.Error;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UserFetch {
    public static ArrayList<UserModel> getUsers() {
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

    public static UserModel getUserByUsername(String username) {
        Connection conn = Socket.getConnection();
        Statement st = null;
        try {
            st = conn.createStatement();

            String query = "SELECT * FROM user WHERE username = '" + username + "'";
            ResultSet rs = st.executeQuery(query);
            rs.last();
            if (rs.getRow() > 1) {
                throw new SQLException("Too many rows in ResultSet");
            }
            UserModel user = new UserModel();
            user.setId(rs.getInt("id"));
            user.setUsername(rs.getString("Username") != null ? rs.getString("Username") : "");
            user.setEmail(rs.getString("email") != null ? rs.getString("email") : "");
            user.setStatusId(rs.getInt("status_id"));
            user.setRoleId(0);
            user.setGender(rs.getInt("gender"));
            user.setFirstname(rs.getString("first_name") != null ? rs.getString("first_name") : "");
            user.setLastname(rs.getString("last_name") != null ? rs.getString("last_name") : "");
            user.setBirthday(rs.getString("birthday") == null ? "" : rs.getString("birthday"));
            user.setLocation(rs.getString("location") != null ? rs.getString("location") : "");
            user.setWebsite(rs.getString("website") != null ? rs.getString("website") : "");
            user.setFacebook(rs.getString("facebook") != null ? rs.getString("facebook") : "");
            user.setSkype(rs.getString("skype") != null ? rs.getString("skype") : "");
            user.setAbout(rs.getString("about") != null ? rs.getString("about") : "");
            user.setLastUpdate(rs.getString("update_time"));
            user.setCreatedOn(rs.getString("create_time"));
            user.setSteamID(rs.getString("steamid") != null ? rs.getString("steamid") : "");
            return user;
        } catch (SQLException e) {
            Error.showErr("Error", "An error has occured", e.toString());
        } finally {
            try {
                st.close();
            } catch (SQLException e) {
                Error.showErr("Error", "An error has occured", e.toString());
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

    public static boolean updateUser(UserModel user) {
        Connection conn = Socket.getConnection();
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("UPDATE user SET id = ?, username = ?, email = ?, status_id = ?, gender = ?, gender = ?, first_name = ?, last_name = ?, birthday = ?, location = ?, website = ?, facebook = ?, skype = ?, about = ?, steamid = ? WHERE id = ?");
            ps.setInt(1, user.getId());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getEmail());
            ps.setInt(4, user.getStatusId());
            ps.setInt(5, user.getGender());
            ps.setInt(6, user.getGender());
            ps.setString(7, user.getFirstname());
            ps.setString(8, user.getLastname());
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = format.parse(user.getBirthday());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (date == null) {
                ps.setDate(9, null);
            } else {
                ps.setDate(9, new java.sql.Date(date.getTime()));
            }
            ps.setString(10, user.getLocation());
            ps.setString(11, user.getWebsite());
            ps.setString(12, user.getFacebook());
            ps.setString(13, user.getSkype());
            ps.setString(14, user.getAbout());
            ps.setString(15, user.getSteamID() == "" ? null : user.getSteamID());
            ps.setInt(16, user.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            Error.showErr("Error", "An error has occured", e.toString());
        } finally {
            try {
                ps.close();
            } catch (Exception e) {
                Error.showErr("Error", "An error has occured", e.toString());
            }
        }
        return false;
    }
}
.