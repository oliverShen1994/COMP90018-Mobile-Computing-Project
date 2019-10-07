package com.android.group_12.crushy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import de.hdodenhof.circleimageview.CircleImageView;

public class EditUserProfile extends AppCompatActivity {
    private CircleImageView UserProfileImage;
    private TextView UserID, UserName, FollowerNum, FollowingNum;
    private EditText EditUserName,  UserDescription, UserEmail,
            UserGender, UserHeight, UserWeight, UserCity, UserBirthday, UserOccupation,
            UserHobbies, UserRelationshipStatus, UserBodyType;
    private LinearLayout SaveButton, PreviousButton;

    private DatabaseReference mDatabase;
    private static final String TAG = "EditUserProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);

        mDatabase = FirebaseDatabase.getInstance().getReference();

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

        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String EditUserName_ = EditUserName.getText().toString();
                String UserDescription_ = UserDescription.getText().toString();
                String UserEmail_ = UserEmail.getText().toString();
                String UserGender_ = UserGender.getText().toString();
                String UserHeight_ = UserHeight.getText().toString();
                String UserWeight_ = UserWeight.getText().toString();
                String UserCity_ = UserCity.getText().toString();
                String UserBirthday_ = UserBirthday.getText().toString();
                String UserOccupation_ = UserOccupation.getText().toString();
                String UserHobbies_ = UserHobbies.getText().toString();
                String UserRelationshipStatus_ = UserRelationshipStatus.getText().toString();
                String UserBodyType_ = UserBodyType.getText().toString();

                writeNewPost(EditUserName_, UserDescription_, UserEmail_, UserGender_, UserHeight_, UserWeight_, UserCity_, UserBirthday_,
                        UserOccupation_, UserHobbies_, UserRelationshipStatus_, UserBodyType_);
            }
        });
    }

    private void InitializeFields() {

        UserProfileImage = (CircleImageView) findViewById(R.id.profile_image);
        UserID = (TextView) findViewById(R.id.UserID);
        UserName = (TextView) findViewById(R.id.UserName);
        FollowerNum = (TextView)findViewById(R.id.FollowersNum);
        FollowingNum = (TextView) findViewById(R.id.FollowingNum);

        EditUserName = (EditText) findViewById(R.id.EditUserName);
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
        retrivePost("0001");
        //Test firebase retrieve:

    }

    public void onStart() {

        super.onStart();

    }


    public void retrivePost(String uid) {
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

                        UserID.setText(UserID_);
                        UserName.setText(UserName_);
                        EditUserName.setText(UserName_);
                        //FollowerNum.setText(FollowerNum_);
                        //FollowingNum.setText(FollowingNum_);
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

    public void writeNewPost(String EditUserName_, String UserDescription_, String UserEmail_, String UserGender_,
                             String UserHeight_, String UserWeight_, String UserCity_, String UserBirthday_,
                 String UserOccupation_, String UserHobbies_, String UserRelationshipStatus_, String UserBodyType_){
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        //String key = mDatabase.child("posts").push().getKey();

         //public User(String UserProfileImage, String UserID, String UserName, String FollowerNum,
        // String FollowingNum, String UserDescription, String UserEmail, String UserGender, String UserHeight,
        // String UserWeight, String UserCity, String UserBirthday, String UserOccupation, String UserHobbies,
        // String UserRelationshipStatus, String UserBodyType)

        String UserProfileImage_ = "";
        String UserID_ = "0001";
        String FollowerNum_ = "0";
        String FollowingNum_ = "0";

        User post = new User(UserProfileImage_, UserID_, EditUserName_, FollowerNum_, FollowingNum_, UserDescription_, UserEmail_, UserGender_,
                UserHeight_, UserWeight_, UserCity_, UserBirthday_, UserOccupation_, UserHobbies_, UserRelationshipStatus_, UserBodyType_);

        Map<String, Object> postValues = post.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/user-profiles/" + UserID_ + '/', postValues);
        //childUpdates.put("/user-posts/" + UserID_ + "/", postValues);

        mDatabase.updateChildren(childUpdates);
    }


}
