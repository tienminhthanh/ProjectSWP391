package model;

import java.sql.Timestamp;

/**
 *
 * @author ADMIN
 */
public class Chat {

    private int senderID;
    private int receiverID;
    private String messageContent;
    private Timestamp sentAt;
    private boolean senderDeleted;
    private boolean receiverDeleted;
    private int dialogueID;
    private boolean isSeen;

    public Chat() {
    }

    public Chat(int senderID, int receiverID, String messageContent, Timestamp sentAt,
            boolean senderDeleted, boolean receiverDeleted, int dialogueID, boolean isSeen) {
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.messageContent = messageContent;
        this.sentAt = sentAt;
        this.senderDeleted = senderDeleted;
        this.receiverDeleted = receiverDeleted;
        this.dialogueID = dialogueID;
        this.isSeen = isSeen;
    }

    public int getSenderID() {
        return senderID;
    }

    public void setSenderID(int senderID) {
        this.senderID = senderID;
    }

    public int getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(int receiverID) {
        this.receiverID = receiverID;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public Timestamp getSentAt() {
        return sentAt;
    }

    public void setSentAt(Timestamp sentAt) {
        this.sentAt = sentAt;
    }

    public boolean isSenderDeleted() {
        return senderDeleted;
    }

    public void setSenderDeleted(boolean senderDeleted) {
        this.senderDeleted = senderDeleted;
    }

    public boolean isReceiverDeleted() {
        return receiverDeleted;
    }

    public void setReceiverDeleted(boolean receiverDeleted) {
        this.receiverDeleted = receiverDeleted;
    }

    public int getDialogueID() {
        return dialogueID;
    }

    public void setDialogueID(int dialogueID) {
        this.dialogueID = dialogueID;
    }

    public boolean isIsSeen() {
        return isSeen;
    }

    public void setIsSeen(boolean isSeen) {
        this.isSeen = isSeen;
    }
}
