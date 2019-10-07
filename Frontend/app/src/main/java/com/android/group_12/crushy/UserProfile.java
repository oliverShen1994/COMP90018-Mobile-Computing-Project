package com.android.group_12.crushy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity {

    private CircleImageView UserProfileImage;
    private TextView UserID, UserName, FollowerNum, FollowingNum, UserDescription, UserEmail,
                     UserGender, UserHeight, UserWeight, UserCity, UserBirthday, UserOccupation,
                     UserHobbies, UserRelationshipStatus, UserBodyType;
    private LinearLayout EditButton, PreviousButton;
    private static final String TAG = "UserProfileActivity";

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mDatabase = FirebaseDatabase.getInstance().getReference();


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

        retrivePost("0001");
    }

    private void retrivePost(String uid) {
        Log.e(TAG, "User " + uid + " is 111111111");
        // Disable button so there are no multi-posts
        Toast.makeText(this, "Posting...", Toast.LENGTH_SHORT).show();

        // [START single_value_read]
        final String userId = uid;
        mDatabase.child("user-profiles").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);

                        //public User(String UserProfileImage, String UserID, String UserName, String FollowerNum,
                        // String FollowingNum, String UserDescription, String UserEmail, String UserGender, String UserHeight,
                        // String UserWeight, String UserCity, String UserBirthday, String UserOccupation, String UserHobbies,
                        // String UserRelationshipStatus, String UserBodyType)

                        //Log.e(TAG, "User " + userId + " is ");
                        //Log.e(TAG, "the author is " + user.author);
                        //Log.e(TAG, user.title);
                        //Log.e(TAG, user.author);
                        //Toast.makeText(EditUserProfile.this,
                        //        "Error: could not fetch user.",
                        //        Toast.LENGTH_SHORT).show();
                        // Finish this Activity, back to the stream
                        // [END_EXCLUDE]

                        String UserProfileImage_ = user.UserProfileImage;
                        Log.e(TAG, UserProfileImage_);
                        String UserID_ = user.UserID;
                        Log.e(TAG, UserID_);
                        String UserName_ = user.UserName;
                        Log.e(TAG, UserName_);
                        String FollowerNum_ = user.FollowerNum;
                        Log.e(TAG, FollowerNum_);
                        String FollowingNum_ = user.FollowingNum;
                        Log.e(TAG, FollowingNum_);
                        String UserDescription_ = user.UserDescription;
                        Log.e(TAG, UserDescription_);
                        String UserEmail_ = user.UserEmail;
                        Log.e(TAG, UserEmail_);
                        String UserGender_ = user.UserGender;
                        Log.e(TAG, UserGender_);
                        String UserHeight_ = user.UserHeight;
                        Log.e(TAG, UserHeight_);
                        String UserWeight_ = user.UserWeight;
                        Log.e(TAG, UserWeight_);
                        String UserCity_ = user.UserCity;
                        Log.e(TAG, UserCity_);
                        String UserBirthday_ = user.UserBirthday;
                        Log.e(TAG, UserBirthday_);
                        String UserOccupation_ = user.UserOccupation;
                        Log.e(TAG, UserOccupation_);
                        String UserHobbies_ = user.UserHobbies;
                        Log.e(TAG, UserHobbies_);
                        String UserRelationshipStatus_ = user.UserRelationshipStatus;
                        Log.e(TAG, UserRelationshipStatus_);
                        String UserBodyType_ = user.UserBodyType;
                        Log.e(TAG, UserBodyType_);
                        //UserProfileImage = (CircleImageView) findViewById(R.id.profile_image);

                        FollowerNum.setText(FollowerNum_);
                        FollowingNum.setText(FollowingNum_);
                        UserID.setText(UserID_);
                        UserName.setText(UserName_);
                        UserDescription.setText(UserDescription_);
                        UserEmail.setText(UserEmail_);
                        UserGender.setText(UserGender_);
                        UserHeight.setText(UserHeight_);
                        UserWeight.setText(UserWeight_);
                        UserCity.setText(UserCity_);
                        UserBirthday.setText(UserBirthday_);
                        UserOccupation.setText(UserOccupation_);
                        UserHobbies.setText(UserHobbies_);
                        UserRelationshipStatus.setText(UserRelationshipStatus_);
                        UserBodyType.setText(UserBodyType_);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        // [START_EXCLUDE]
                        // [END_EXCLUDE]
                    }
                });
        // [END single_value_read]
    }


    public void onStart() {

        super.onStart();

    }
}