package dao;

import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Notification;
import utils.DBContext;

public class NotificationDAO {

    private DBContext context;

    public NotificationDAO() {
        context = new DBContext();
    }

    // Existing methods remain unchanged...
    // New method to get notification by ID
    public Notification getNotificationById(int notificationID) throws SQLException {
        String sql = "SELECT * FROM Notification WHERE notificationID = ?";
        Object[] params = {notificationID};
        ResultSet rs = context.exeQuery(sql, params);
        Notification notification = null;

        try {
            if (rs.next()) {
                notification = new Notification(
                        rs.getInt("notificationID"),
                        rs.getInt("senderID"),
                        rs.getInt("receiverID"),
                        rs.getString("notificationDetails"),
                        rs.getDate("notificationDateCreated"),
                        rs.getBoolean("isDeleted"),
                        rs.getString("notificationTitle"),
                        rs.getBoolean("isRead")
                );
            }
        } finally {
            rs.close();
        }
        return notification;
    }

    // New method to update notification
    public boolean updateNotification(String notificationNewTitle, String notificationNewDetails, String notificationTitle, String notificationDetails) throws SQLException {
        String sql = "UPDATE Notification SET notificationTitle = ?, notificationDetails = ? WHERE notificationTitle = ? AND notificationDetails = ?";
        Object[] params = {
            notificationNewTitle,
            notificationNewDetails,
            notificationTitle,
            notificationDetails
        };
        int rowsAffected = context.exeNonQuery(sql, params);
        return rowsAffected > 0;
    }

    // Existing methods...
    public boolean insertNotification(Notification notification) throws SQLException {
        String sql = "INSERT INTO Notification (senderID, receiverID, notificationDetails, notificationDateCreated, isDeleted, notificationTitle, isRead) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Object[] params = {
            notification.getSenderID(),
            notification.getReceiverID(),
            notification.getNotificationDetails(),
            notification.getDateCreated(),
            notification.isDeleted(),
            notification.getNotificationTitle(),
            notification.isRead()
        };
        int rowsAffected = context.exeNonQuery(sql, params);
        return rowsAffected > 0;
    }

    public List<Notification> getNotificationsByReceiver(int receiverID) throws SQLException {
        String sql = "SELECT * FROM Notification WHERE receiverID = ? AND isDeleted = 0 ORDER BY notificationDateCreated DESC, isRead ASC";
        Object[] params = {receiverID};
        ResultSet rs = context.exeQuery(sql, params);
        List<Notification> notifications = new ArrayList<>();

        try {
            while (rs.next()) {
                Notification notification = new Notification(
                        rs.getInt("notificationID"),
                        rs.getInt("senderID"),
                        rs.getInt("receiverID"),
                        rs.getString("notificationDetails"),
                        rs.getDate("notificationDateCreated"),
                        rs.getBoolean("isDeleted"),
                        rs.getString("notificationTitle"),
                        rs.getBoolean("isRead")
                );
                notifications.add(notification);
            }
        } finally {
            rs.close();
        }
        return notifications;
    }

    public List<Notification> getNotificationsByReceiverDESC(int receiverID) throws SQLException {
        String sql = "SELECT * FROM Notification WHERE receiverID = ? AND isDeleted = 0 ORDER BY isRead ASC, notificationDateCreated DESC";
        Object[] params = {receiverID};
        ResultSet rs = context.exeQuery(sql, params);
        List<Notification> notifications = new ArrayList<>();

        try {
            while (rs.next()) {
                Notification notification = new Notification(
                        rs.getInt("notificationID"),
                        rs.getInt("senderID"),
                        rs.getInt("receiverID"),
                        rs.getString("notificationDetails"),
                        rs.getDate("notificationDateCreated"),
                        rs.getBoolean("isDeleted"),
                        rs.getString("notificationTitle"),
                        rs.getBoolean("isRead")
                );
                notifications.add(notification);
            }
        } finally {
            rs.close();
        }
        return notifications;
    }

    public List<Notification> getAllNotifications() throws SQLException {
        String sql = "SELECT MAX(notificationID) AS notificationID, notificationTitle, notificationDetails, MAX(notificationDateCreated) AS notificationDateCreated FROM Notification GROUP BY notificationTitle, notificationDetails ORDER BY notificationDateCreated DESC, notificationID DESC";
        ResultSet rs = context.exeQuery(sql, new Object[]{});
        List<Notification> notifications = new ArrayList<>();

        try {
            while (rs.next()) {
                Notification notification = new Notification(
                        rs.getInt("notificationID"),
                        rs.getString("notificationTitle"),
                        rs.getString("notificationDetails"),
                        rs.getDate("notificationDateCreated")
                );
                notifications.add(notification);
            }
        } finally {
            rs.close();
        }
        return notifications;
    }

    public List<Notification> getAllNotificationsForStaff() throws SQLException {
        String sql = "SELECT MAX(notificationID) AS notificationID, notificationTitle, notificationDetails, MAX(notificationDateCreated) AS notificationDateCreated FROM Notification Where senderID !=1  GROUP BY notificationTitle, notificationDetails ORDER BY notificationDateCreated DESC, notificationID DESC";
        ResultSet rs = context.exeQuery(sql, new Object[]{});
        List<Notification> notifications = new ArrayList<>();

        try {
            while (rs.next()) {
                Notification notification = new Notification(
                        rs.getInt("notificationID"),
                        rs.getString("notificationTitle"),
                        rs.getString("notificationDetails"),
                        rs.getDate("notificationDateCreated")
                );
                notifications.add(notification);
            }
        } finally {
            rs.close();
        }
        return notifications;
    }

    public boolean markAsRead(int notificationID) throws SQLException {
        String sql = "UPDATE Notification SET isRead = 1 WHERE notificationID = ?";
        Object[] params = {notificationID};
        int rowsAffected = context.exeNonQuery(sql, params);
        return rowsAffected > 0;
    }

    public boolean markAsAllRead(int receiverID) throws SQLException {
        String sql = "UPDATE Notification SET isRead = 1 WHERE receiverID = ?";
        Object[] params = {receiverID};
        int rowsAffected = context.exeNonQuery(sql, params);
        return rowsAffected > 0;
    }

    public boolean deleteNotification(int notificationID) throws SQLException {
        String sql = "UPDATE Notification SET isDeleted = 1 WHERE notificationID = ?";
        Object[] params = {notificationID};
        int rowsAffected = context.exeNonQuery(sql, params);
        return rowsAffected > 0;
    }

    public static void main(String[] args) {
        try {
            NotificationDAO notiDAO = new NotificationDAO();
            for (Notification string : notiDAO.getNotificationsByReceiverDESC(5)) {
                System.out.println(string.toString());
            }
        } catch (SQLException ex) {
            Logger.getLogger(NotificationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
