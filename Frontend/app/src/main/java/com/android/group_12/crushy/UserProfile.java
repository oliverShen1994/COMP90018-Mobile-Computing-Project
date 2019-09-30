package com.android.group_12.crushy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity {
    private CircleImageView UserProfileImage;
    private TextView UserID, UserName, FollowerNum, FollowingNum, UserDescription, UserEmail,
                     UserGender, UserHeight, UserWeight, UserCity, UserBirthday, UserOccupation,
                     UserHobbies, UserRelationshipStatus, UserBodyType;
    private LinearLayout EditButton, PreviousButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        InitializeFields();

        EditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent EditProfileIntent = new Intent(UserProfile.this, EditUserProfile.class);
                startActivity(EditProfileIntent);
            }
        });

//        PreviousButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
    }



    private void InitializeFields() {
        UserProfileImage = (CircleImageView) findViewById(R.id.profile_image);
        UserID = (TextView) findViewById(R.id.UserID);
        UserName = (TextView) findViewById(R.id.UserName);
        FollowerNum = (TextView)findViewById(R.id.FollowersNum);
        FollowingNum = (TextView) findViewById(R.id.FollowingNum);
        UserDescription = (TextView) findViewById(R.id.UserDescription);
        UserEmail = (TextView) findViewById(R.id.UserEmail);
        UserGender = (TextView) findViewById(R.id.UserGender);
        UserHeight = (TextView) findViewById(R.id.UserHeight);
        UserWeight = (TextView) findViewById(R.id.UserWeight);
        UserCity = (TextView) findViewById(R.id.UserCity);
        UserBirthday = (TextView) findViewById(R.id.UserBirthday);
        UserOccupation = (TextView) findViewById(R.id.UserOccupation);
        UserHobbies = (TextView) findViewById(R.id.UserHobbies);
        UserRelationshipStatus = (TextView) findViewById(R.id.UserRelationshipStatus);
        UserBodyType = (TextView) findViewById(R.id.UserBodyType);
        EditButton = (LinearLayout) findViewById(R.id.EditButton);
        PreviousButton = (LinearLayout) findViewById(R.id.pro_previous);
    }

    public void onStart() {

        super.onStart();

    }
}