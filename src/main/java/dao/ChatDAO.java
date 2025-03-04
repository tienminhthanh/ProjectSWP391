package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Chat;
import utils.DBContext;

public class ChatDAO {

    private DBContext context;
    private static final int ADMIN_ID = 1; // Assuming admin's userID is 1

    public ChatDAO() {
        context = new DBContext();
    }

    public boolean insertChat(Chat chat) throws SQLException {
        String sql = "INSERT INTO [dbo].[Message] "
                + "(senderID, receiverID, messageContent, sentAt, senderDeleted, receiverDeleted, dialogueID, isSeen) \n"
                + "VALUES (?, ?, ?, GETDATE(), 0, 0,(SELECT TOP 1 dialogueID FROM [dbo].[Dialogue]\n"
                + "WHERE (user1ID = ? AND user2ID = ?)\n"
                + "ORDER BY createdAt DESC), 0)";
        Object[] params = {
            chat.getSenderID(),
            chat.getReceiverID(),
            chat.getMessageContent(),
            chat.getSentAt(),
            chat.isSenderDeleted(),
            chat.isReceiverDeleted(),
            chat.getDialogueID(),
            chat.isIsSeen()
        };
        int rowsAffected = context.exeNonQuery(sql, params);
        return rowsAffected > 0;
    }

    public List<Chat> getChatsBetweenUsers(int userID1, int userID2) throws SQLException {
        List<Chat> chats = new ArrayList<>();
        String sql = "SELECT * FROM [dbo].[Message] WHERE "
                + "((senderID = ? AND receiverID = ?) OR (senderID = ? AND receiverID = ?)) "
                + "AND senderDeleted = 0 AND receiverDeleted = 0 "
                + "ORDER BY sentAt ASC"; // Sắp xếp theo thời gian gửi
        Object[] params = {userID1, userID2, userID2, userID1};
        ResultSet rs = context.exeQuery(sql, params);
        while (rs.next()) {
            chats.add(mapResultSetToChat(rs));
        }
        return chats;
    }

    public Set<Integer> getAdminChatCustomers() throws SQLException {
        Set<Integer> customerIDs = new HashSet<>();
        String sql = "SELECT DISTINCT CASE WHEN senderID = ? THEN receiverID ELSE senderID END AS customerID "
                + "FROM [dbo].[Message] WHERE (senderID = ? OR receiverID = ?) "
                + "AND senderDeleted = 0 AND receiverDeleted = 0";
        Object[] params = {ADMIN_ID, ADMIN_ID, ADMIN_ID};
        ResultSet rs = context.exeQuery(sql, params);
        while (rs.next()) {
            customerIDs.add(rs.getInt("customerID"));
        }
        return customerIDs;
    }

    private Chat mapResultSetToChat(ResultSet rs) throws SQLException {
        return new Chat(
                rs.getInt("senderID"),
                rs.getInt("receiverID"),
                rs.getString("messageContent"),
                rs.getTimestamp("sentAt"), // Đổi thành Timestamp
                rs.getBoolean("senderDeleted"),
                rs.getBoolean("receiverDeleted"),
                rs.getInt("dialogueID"),
                rs.getBoolean("isSeen")
        );
    }
}
