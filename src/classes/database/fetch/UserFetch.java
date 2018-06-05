package classes.database.fetch;

import classes.Constants;
import classes.database.ConnectionBuilder;
import classes.database.model.UserModel;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UserFetch {
    public static ArrayList<UserModel> getUsers() throws SQLException {
        ArrayList<UserModel> users = new ArrayList<>();
        Connection conn = null;
        Statement st = null;
        try {
            conn = ConnectionBuilder.connect();
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
        } finally {
            try {
                conn.close();
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static UserModel getUserByUsername(String username) throws SQLException {
        Connection conn = null;
        Statement st = null;
        try {
            conn = ConnectionBuilder.connect();
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
        } finally {
            try {
                conn.close();
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static ArrayList<String> getUsernames() throws SQLException {
        ArrayList<String> usernames = new ArrayList<>();
        Connection conn = null;
        Statement st = null;
        try {
            conn = ConnectionBuilder.connect();
            st = conn.createStatement();

            String sql = "SELECT username FROM user";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                usernames.add(rs.getString("Username"));
            }
            return usernames;
        } finally {
            try {
                conn.close();
                st.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void updateUser(UserModel user) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = ConnectionBuilder.connect();
            ps = conn.prepareStatement("UPDATE user SET id = ?, username = ?, email = ?, status_id = ?, gender = ?, gender = ?, first_name = ?, last_name = ?, birthday = ?, location = ?, website = ?, facebook = ?, skype = ?, about = ?, steamid = ? WHERE id = ?");
            ps.setInt(1, user.getId());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getEmail());
            ps.setInt(4, user.getStatusId());
            if (user.getRoleId() > 0) {
                ps.setInt(5, user.getRoleId());
            } else {
                ps.setNull(5, Types.INTEGER);
            }
            ps.setInt(6, user.getGender());
            ps.setString(7, user.getFirstname());
            ps.setString(8, user.getLastname());
            DateFormat format = new SimpleDateFormat(Constants.FORMAT_DATE_SQL);
            Date date = null;
            if (!user.getBirthday().isEmpty()) {
                try {
                    date = format.parse(user.getBirthday());
                    ps.setDate(9, new java.sql.Date(date.getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                ps.setNull(9, Types.DATE);
            }
            ps.setString(10, user.getLocation());
            ps.setString(11, user.getWebsite());
            ps.setString(12, user.getFacebook());
            ps.setString(13, user.getSkype());
            ps.setString(14, user.getAbout());
            if (user.getSteamID().isEmpty()) {
                ps.setNull(15, Types.VARCHAR);
            } else {
                ps.setString(15, user.getSteamID());
            }
            ps.setInt(16, user.getId());
            ps.executeUpdate();
        } finally {
            try {
                conn.close();
                ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void updatePassword(int userID, String newPassword) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = ConnectionBuilder.connect();
            ps = conn.prepareStatement("UPDATE user SET password = ? WHERE id = ?");
            ps.setString(1, BCrypt.hashpw(newPassword, BCrypt.gensalt(10)));
            ps.setInt(2, userID);
            ps.executeUpdate();
        } finally {
            try {
                conn.close();
                ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
