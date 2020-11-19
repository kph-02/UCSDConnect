package com.ucsd.connect.demo.Chat;

import com.ucsd.connect.demo.User.UserProfile;

import java.io.Serializable;
import java.util.ArrayList;

public class ChatObject implements Serializable {
    private String chatId;

    private ArrayList<UserProfile> userObjectArrayList = new ArrayList<>();

    public ChatObject(String chatId){
        this.chatId = chatId;
    }

    public String getChatId() {
        return chatId;
    }
    public ArrayList<UserProfile> getUserObjectArrayList() {
        return userObjectArrayList;
    }




    public void addUserToArrayList(UserProfile mUser){
        userObjectArrayList.add(mUser);
    }
}
