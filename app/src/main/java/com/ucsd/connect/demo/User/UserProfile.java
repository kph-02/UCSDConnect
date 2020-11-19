package com.ucsd.connect.demo.User;

/**
 * Created by Dheeraj_Kamath on 2/10/2018.
 */

import java.io.Serializable;

public class UserProfile implements Serializable {
    public String userAge;
    public String userEmail;
    public String userName;
    public String uid;
    public String notificationKey;
    private Boolean selected = false;

    public UserProfile(){}

    public UserProfile(String uid){
        this.uid = uid;
    }

    public UserProfile(String uid, String userAge, String userEmail, String userName) {
        this.uid = uid;
        this.userAge = userAge;
        this.userEmail = userEmail;
        this.userName = userName;
    }
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.userEmail = uid;
    }

    public String getUserAge() {
        return userAge;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Boolean getSelected() {
        return selected;
    }
    public void setSelected(Boolean selected) { this.selected = selected; }

    public String getNotificationKey() {
        return notificationKey;
    }
    public void setNotificationKey(String notificationKey) {
        this.notificationKey = notificationKey;
    }


}
