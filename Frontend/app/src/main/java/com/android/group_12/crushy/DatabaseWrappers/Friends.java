package com.android.group_12.crushy.DatabaseWrappers;

public class Friends {
    private String userID;
    private String name;
    private String profileImageUrl;

    public Friends() {

    }

    public Friends(String userID, String name, String profileImageUrl) {
        this.setUserID(userID);
        this.setName(name);
        this.setProfileImageUrl(profileImageUrl);
    }


    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}