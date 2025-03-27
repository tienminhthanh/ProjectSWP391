package dao;

import model.Chat;
import utils.DBContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChatDAO {

    private DBContext context;
    private static final int ADMIN_ID = 1;

    public ChatDAO() {
        context = new DBContext();
    }

   
    public String getCustomerName(int customerID) throws SQLException {
        String sql = "SELECT firstName FROM [dbo].[Account] WHERE accountID = ?";
        Object[] params = {customerID};
        ResultSet rs = context.exeQuery(sql, params);
        if (rs.next()) {
            return rs.getString("firstName");
        }
        return "Unknown Customer";
    }

    public boolean insertChat(Chat chat) throws SQLException {
        int dialogueID = getOrCreateDialogueID(chat.getSenderID(), chat.getReceiverID());
        if (dialogueID == -1) {
            throw new SQLException("Cannot generate dialogueID");
        }
        String sql = "INSERT INTO [dbo].[Message] "
                + "(senderID, receiverID, messageContent, sentAt, senderDeleted, receiverDeleted, dialogueID, isSeen) "
                + "VALUES (?, ?, ?, GETDATE(), 0, 0, ?, 0)";
        Object[] params = {
            chat.getSenderID(),
            chat.getReceiverID(),
            chat.getMessageContent(),
            dialogueID
        };
        int rowsAffected = context.exeNonQuery(sql, params);
        return rowsAffected > 0;
    }
    
     public boolean updateStatusSeen(int user, Chat chat) throws SQLException {
        int dialogueID = getOrCreateDialogueID(chat.getSenderID(), chat.getReceiverID());
        String sql = "UPDATE [dbo].[Message] SET isSeen = 1 WHERE senderID = ? AND dialogueID = ? AND isSeen = 0";
        Object[] params = {
            user,
            dialogueID
        };
        int rowsAffected = context.exeNonQuery(sql, params);
         return rowsAffected > 0;
    }

    private int getOrCreateDialogueID(int user1ID, int user2ID) throws SQLException {
        String selectSql = "SELECT TOP 1 dialogueID FROM [dbo].[Dialogue] "
                + "WHERE (user1ID = ? AND user2ID = ?) OR (user1ID = ? AND user2ID = ?) "
                + "ORDER BY createdAt DESC";
        Object[] selectParams = {user1ID, user2ID, user2ID, user1ID};
        ResultSet rs = context.exeQuery(selectSql, selectParams);
        if (rs.next()) {
            return rs.getInt("dialogueID");
        }
        String insertSql = "INSERT INTO [dbo].[Dialogue] (user1ID, user2ID, createdAt) "
                + "VALUES (?, ?, GETDATE()); SELECT SCOPE_IDENTITY() AS dialogueID";
        Object[] insertParams = {user1ID, user2ID};
        ResultSet insertRs = context.exeQuery(insertSql, insertParams);
        if (insertRs.next()) {
            return insertRs.getInt("dialogueID");
        }
        return -1;
    }

    public List<Chat> getChatsBetweenUsers(int userID1, int userID2) throws SQLException {
        List<Chat> chats = new ArrayList<>();
        String sql = "SELECT * FROM [dbo].[Message] WHERE "
                + "((senderID = ? AND receiverID = ?) OR (senderID = ? AND receiverID = ?)) "
                + "AND senderDeleted = 0 AND receiverDeleted = 0 "
                + "ORDER BY sentAt ASC";
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
                rs.getTimestamp("sentAt"),
                rs.getBoolean("senderDeleted"),
                rs.getBoolean("receiverDeleted"),
                rs.getInt("dialogueID"),
                rs.getBoolean("isSeen")
        );
    }
    public static void main(String[] args) throws SQLException {
        ChatDAO cd = new ChatDAO();
        Chat chat = new Chat();
        chat.setReceiverID(5);
        chat.setSenderID(ADMIN_ID);
        cd.updateStatusSeen(ADMIN_ID, chat);
    }
}