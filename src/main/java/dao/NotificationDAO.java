/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.Notification;
import utils.DBContext;

/**
 *
 * @author ADMIN
 */
public class NotificationDAO {

    private DBContext context;

    public NotificationDAO() {
        context = new DBContext();
    }

    // Thêm thông báo mới
    public boolean insertNotification(Notification notification) throws SQLException {
        String sql = "INSERT INTO Notification (senderID, receiverID, notificationDetails, dateCreated, isDeleted, notificationTitle, isRead) VALUES (?, ?, ?, ?, ?, ?, ?)";
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

    // Lấy danh sách thông báo theo receiverID
    public List<Notification> getNotificationsByReceiver(int receiverID) throws SQLException {
        String sql = "SELECT * FROM Notification WHERE receiverID = ? AND isDeleted = 0 ORDER BY dateCreated DESC";
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
                        rs.getDate("dateCreated"),
                        rs.getBoolean("isDeleted"),
                        rs.getString("notificationTitle"),
                        rs.getBoolean("isRead")
                );
                notifications.add(notification);
            }
        } finally {
            rs.close(); // Đảm bảo đóng ResultSet trong khối finally
        }

        return notifications;
    }

    // Đánh dấu thông báo là đã đọc
    public boolean markAsRead(int notificationID) throws SQLException {
        String sql = "UPDATE Notification SET isRead = 1 WHERE notificationID = ?";
        Object[] params = {notificationID};
        int rowsAffected = context.exeNonQuery(sql, params);
        return rowsAffected > 0;
    }

    // Xóa thông báo (cập nhật trạng thái isDeleted thành true)
    public boolean deleteNotification(int notificationID) throws SQLException {
        String sql = "UPDATE Notification SET isDeleted = 1 WHERE notificationID = ?";
        Object[] params = {notificationID};
        int rowsAffected = context.exeNonQuery(sql, params);
        return rowsAffected > 0;
    }
}
