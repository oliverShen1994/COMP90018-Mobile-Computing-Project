package com.android.group_12.crushy;

import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class User {

    public String UserProfileImage;
    public String UserID;
    public String UserName;
    public String FollowerNum;
    public String FollowingNum;
    public String UserDescription;
    public String UserEmail;
    public String UserGender;
    public String UserHeight;
    public String UserWeight;
    public String UserCity;
    public String UserBirthday;
    public String UserOccupation;
    public String UserHobbies;
    public String UserRelationshipStatus;
    public String UserBodyType;

    public Map<String, Boolean> stars = new HashMap<>();

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public User(String UserProfileImage, String UserID, String UserName, String FollowerNum, String FollowingNum, String UserDescription, String UserEmail, String UserGender, String UserHeight, String UserWeight, String UserCity, String UserBirthday, String UserOccupation, String UserHobbies, String UserRelationshipStatus, String UserBodyType) {
        this.UserProfileImage = UserProfileImage;
        this.UserID = UserID;
        this.UserName = UserName;
        this.FollowerNum = FollowerNum;
        this.FollowingNum = FollowingNum;
        this.UserDescription = UserDescription;
        this.UserEmail = UserEmail;
        this.UserGender = UserGender;
        this.UserHeight = UserHeight;
        this.UserWeight = UserWeight;
        this.UserCity = UserCity;
        this.UserBirthday = UserBirthday;
        this.UserOccupation = UserOccupation;
        this.UserHobbies = UserHobbies;
        this.UserRelationshipStatus = UserRelationshipStatus;
        this.UserBodyType = UserBodyType;
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("UserProfileImage", UserProfileImage);
        result.put("UserID", UserID);
        result.put("UserName", UserName);
        result.put("FollowerNum", FollowerNum);
        result.put("FollowingNum", FollowingNum);
        result.put("UserDescription" , UserDescription);
        result.put("UserEmail" , UserEmail);
        result.put("UserGender" , UserGender);
        result.put("UserHeight" , UserHeight);
        result.put("UserWeight" , UserWeight);
        result.put("UserCity" , UserCity);
        result.put("UserBirthday" , UserBirthday);
        result.put("UserOccupation" , UserOccupation);
        result.put("UserHobbies" , UserHobbies);
        result.put("UserRelationshipStatus" , UserRelationshipStatus);
        result.put("UserBodyType", UserBodyType);
        result.put("stars", stars);
        return result;
    }

}
