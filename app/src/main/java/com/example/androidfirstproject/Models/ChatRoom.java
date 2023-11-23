package com.example.androidfirstproject.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class ChatRoom implements Serializable {
    String id;
    ArrayList<String> messageList ;
    String lastMessageId,nameUser2;
    ArrayList<String> userPhoneNumber;
    Date lastMessageDate;

    public ChatRoom() {
    }

    public ChatRoom(String id, ArrayList<String> messageList, String lastMessageId, String nameUser2, ArrayList<String> userPhoneNumber, Date lastMessageDate) {
        this.id = id;
        this.messageList = messageList;
        this.lastMessageId = lastMessageId;
        this.nameUser2= nameUser2;
        this.userPhoneNumber = userPhoneNumber;
        this.lastMessageDate = lastMessageDate;
    }

    public Date getLastMessageDate() {
        return lastMessageDate;
    }

    public void setLastMessageDate(Date lastMessageDate) {
        this.lastMessageDate = lastMessageDate;
    }

    public ArrayList<String> getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(ArrayList<String> userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getMessageList() {
        return messageList;
    }

    public void setMessageList(ArrayList<String> messageList) {
        this.messageList = messageList;
    }

    public String getLastMessageId() {
        return lastMessageId;
    }

    public void setLastMessageId(String lastMessageId) {
        this.lastMessageId = lastMessageId;
    }

    public String getNameUser2() {
        return nameUser2;
    }

    public void setNameUser2(String nameUser2) {
        this.nameUser2 = nameUser2;
    }
}
