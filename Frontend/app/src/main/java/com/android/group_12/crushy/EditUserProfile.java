package com.android.group_12.crushy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.group_12.crushy.Constants.DatabaseConstant;
import com.android.group_12.crushy.DatabaseWrappers.User;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import de.hdodenhof.circleimageview.CircleImageView;

public class EditUserProfile extends AppCompatActivity {
    private CircleImageView UserProfileImage;
    private TextView UserID, UserName;
    private EditText EditUserName,  UserDescription, UserEmail,
            UserGender, UserHeight, UserWeight, UserCity, UserBirthday, UserOccupation,
            UserHobbies, UserRelationshipStatus, UserBodyType;
    private LinearLayout SaveButton, PreviousButton;
    private RelativeLayout EditImage;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String TAG = "EditUserProfileActivity";

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        initializeFields();

        PreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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

//                Intent ProfileIntent = new Intent(EditUserProfile.this, UserProfile.class);
//                startActivity(ProfileIntent);
                finish();
            }
        });

        EditImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent EditImageIntent = new Intent(EditUserProfile.this, EditUserImage.class);
                startActivity(EditImageIntent);
            }
        });
    }

    private void initializeFields() {
        UserProfileImage = (CircleImageView) findViewById(R.id.profile_image);
        UserID = (TextView) findViewById(R.id.UserID);
        UserName = (TextView) findViewById(R.id.UserName);
//        FollowerNum = (TextView)findViewById(R.id.FollowersNum);
//        FollowingNum = (TextView) findViewById(R.id.FollowingNum);

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

        EditImage = (RelativeLayout) findViewById(R.id.UserImage);
        SaveButton = (LinearLayout) findViewById(R.id.SaveButton);
        PreviousButton = (LinearLayout) findViewById(R.id.PreviousButton);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        currentUserId = currentUser.getUid();
        retrivePost(currentUserId);
    }

    public void onStart() {
        super.onStart();
    }


    private void retrivePost(String uid) {
        Log.i(TAG, "User " + uid + " is 111111111");
        // Disable button so there are no multi-posts
        //Toast.makeText(this, "Posting...", Toast.LENGTH_SHORT).show();

        // [START single_value_read]
        final String userId = uid;
        mDatabase.child(DatabaseConstant.USER_TABLE_NAME).child(userId).addListenerForSingleValueEvent(
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

                        String UserProfileImage_ = user.profileImageUrl;

                        String UserID_ = user.userID;
                        String UserName_ = user.name;
                        if(!UserProfileImage_.equals("")) {
                            Glide.with(EditUserProfile.this)
                                    .load(UserProfileImage_)
                                    .into(UserProfileImage);
                        }

                        UserDescription.setText(user.description);
                        UserEmail.setText(user.email);
                        UserGender.setText(user.gender);
                        UserHeight.setText(user.height);
                        UserWeight.setText(user.weight);
                        UserCity.setText(user.city);
                        UserBirthday.setText(user.birthday);
                        UserOccupation.setText(user.occupation);
                        UserHobbies.setText(user.hobbies);
                        UserRelationshipStatus.setText(user.relationshipStatus);
                        UserBodyType.setText(user.bodyType);
                        UserID.setText(UserID_);
                        UserName.setText(UserName_);
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

        String UserID_ = currentUserId;

        //fixme:only for test
        String UserProfileImage_ = "";

        User post = new User(UserID_, EditUserName_, UserBirthday_, UserEmail_, UserBodyType_, UserCity_, UserDescription_, UserGender_, UserHobbies_, UserOccupation_, UserProfileImage_, UserRelationshipStatus_, UserHeight_, UserWeight_);

        Map<String, Object> postValues = post.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Users/" + UserID_ + '/', postValues);
        //childUpdates.put("/user-posts/" + UserID_ + "/", postValues);

        mDatabase.updateChildren(childUpdates);
    }

/*
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Glide.with(this)
                    .load(mImageUri)
                    .into(UserProfileImage);
        }
    }

 */

}
