package com.ucsd.connect.demo.Chat;

import com.ucsd.connect.demo.User.UserProfile;

import java.io.Serializable;
import java.util.ArrayList;

public class ChatObject implements Serializable {
    private String chatId;
    private UserProfile currUser = new UserProfile();

    private ArrayList<UserProfile> UserProfileArrayList = new ArrayList<>();

    public ChatObject(String chatId){
        this.chatId = chatId;
    }

    public String getChatId() {
        return chatId;
    }
    public ArrayList<UserProfile> getUserProfileArrayList() {
        return UserProfileArrayList;
    }

    public void addUserToArrayList(UserProfile mUser){
        UserProfileArrayList.add(mUser);
    }

    public UserProfile getCurrUser() {
        return currUser;
    }

    public void setCurrUser(UserProfile user) {
        this.currUser = user;
    }
}
