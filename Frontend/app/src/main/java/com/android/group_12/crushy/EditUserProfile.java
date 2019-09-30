package com.android.group_12.crushy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditUserProfile extends AppCompatActivity {
    private CircleImageView UserProfileImage;
    private TextView UserID, UserName, FollowerNum, FollowingNum;
    private EditText EditUserName,  UserDescription, UserEmail,
            UserGender, UserHeight, UserWeight, UserCity, UserBirthday, UserOccupation,
            UserHobbies, UserRelationshipStatus, UserBodyType;
    private LinearLayout SaveButton, PreviousButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);

        InitializeFields();

//        SaveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

        PreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ProfileIntent = new Intent(EditUserProfile.this, UserProfile.class);
                startActivity(ProfileIntent);
            }
        });
    }

    private void InitializeFields() {
        UserProfileImage = (CircleImageView) findViewById(R.id.profile_image);
        UserID = (TextView) findViewById(R.id.UserID);
        UserName = (TextView) findViewById(R.id.UserName);
        EditUserName = (EditText) findViewById(R.id.EditUserName);
        FollowerNum = (TextView)findViewById(R.id.FollowersNum);
        FollowingNum = (TextView) findViewById(R.id.FollowingNum);
        UserDescription = (EditText) findViewById(R.id.EditUserDescription);
        UserEmail = (EditText) findViewById(R.id.EditUserEmail);
        UserGender = (EditText) findViewById(R.id.EditUserGender);
        UserHeight = (EditText) findViewById(R.id.EditUserHeight);
        UserWeight = (EditText) findViewById(R.id.EditUserWeight);
        UserCity = (EditText) findViewById(R.id.EditUserCity);
        UserBirthday = (EditText) findViewById(R.id.EditUserBirthday);
        UserOccupation = (EditText) findViewById(R.id.EditUserOccupation);
        UserHobbies = (EditText) findViewById(R.id.EditUserHobbies);
        UserRelationshipStatus = (EditText) findViewById(R.id.EditUserRelationshipStatus);
        UserBodyType = (EditText) findViewById(R.id.EditUserBodyType);
        SaveButton = (LinearLayout) findViewById(R.id.SaveButton);
        PreviousButton = (LinearLayout) findViewById(R.id.PreviousButton);
    }

    public void onStart() {

        super.onStart();

    }
}
