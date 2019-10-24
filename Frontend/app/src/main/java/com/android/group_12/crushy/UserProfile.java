package com.android.group_12.crushy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.group_12.crushy.Activities.FollowerListActivity;
import com.android.group_12.crushy.Activities.FollowingListActivity;
import com.android.group_12.crushy.Constants.DatabaseConstant;
import com.android.group_12.crushy.Constants.RequestCode;
import com.android.group_12.crushy.DatabaseWrappers.User;
import com.android.group_12.crushy.DatabaseWrappers.UserFollow;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity {

    private CircleImageView userProfileImage;
    private TextView userID, userName, followerNum, followingNum, userDescription, userEmail,
                     userGender, userHeight, userWeight, userCity, userBirthday, userOccupation,
                     userHobbies, userRelationshipStatus, userBodyType;
    private LinearLayout editButton, previousButton, following, follower;

    private static final String TAG = "UserProfileActivity";

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        initializeFields();

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent EditProfileIntent = new Intent(UserProfile.this, EditUserProfile.class);
//                startActivityForResult(EditProfileIntent, RequestCode.UserProfile);
                startActivity(EditProfileIntent);
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        follower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent followerIntent;
                followerIntent = new Intent(UserProfile.this, FollowerListActivity.class);
                startActivity(followerIntent);
            }
        });

        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent followingIntent;
                followingIntent = new Intent(UserProfile.this, FollowingListActivity.class);
                startActivity(followingIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeFields();
    }

    private void initializeFields() {
        userProfileImage = (CircleImageView) findViewById(R.id.profile_image);
        userID = (TextView) findViewById(R.id.UserID);
        userName = (TextView) findViewById(R.id.UserName);
        followerNum = (TextView)findViewById(R.id.FollowersNum);
        followingNum = (TextView) findViewById(R.id.FollowingNum);
        userDescription = (TextView) findViewById(R.id.UserDescription);
        userEmail = (TextView) findViewById(R.id.UserEmail);
        userGender = (TextView) findViewById(R.id.UserGender);
        userHeight = (TextView) findViewById(R.id.UserHeight);
        userWeight = (TextView) findViewById(R.id.UserWeight);
        userCity = (TextView) findViewById(R.id.UserCity);
        userBirthday = (TextView) findViewById(R.id.UserBirthday);
        userOccupation = (TextView) findViewById(R.id.UserOccupation);
        userHobbies = (TextView) findViewById(R.id.UserHobbies);
        userRelationshipStatus = (TextView) findViewById(R.id.UserRelationshipStatus);
        userBodyType = (TextView) findViewById(R.id.UserBodyType);
        follower = (LinearLayout) findViewById(R.id.Follower);
        following = (LinearLayout) findViewById(R.id.Following);
        editButton = (LinearLayout) findViewById(R.id.EditButton);
        previousButton = (LinearLayout) findViewById(R.id.pro_previous);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        retrivePost(currentUser.getUid());
    }

    private void retrivePost(String uid) {
        Log.e(TAG, "User " + uid + " is 111111111");
        // Disable button so there are no multi-posts
        Toast.makeText(this, "Posting...", Toast.LENGTH_SHORT).show();

        // [START single_value_read]
        final String userId = uid;
        mDatabase.child(DatabaseConstant.USER_TABLE_NAME).child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);
                        String UserProfileImage_ = user.profileImageUrl;
                        Log.i(TAG, UserProfileImage_);
                        String UserID_ = user.userID;
                        Log.i(TAG, UserID_);
                        String UserName_ = user.name;
                        Log.i(TAG, UserName_);
                        String UserDescription_ = user.description;
                        Log.i(TAG, UserDescription_);
                        String UserEmail_ = user.email;
                        Log.i(TAG, UserEmail_);
                        String UserGender_ = user.gender;
                        Log.i(TAG, UserGender_);
                        String UserHeight_ = user.height;
                        Log.i(TAG, UserHeight_);
                        String UserWeight_ = user.weight;
                        Log.i(TAG, UserWeight_);
                        String UserCity_ = user.city;
                        Log.i(TAG, UserCity_);
                        String UserBirthday_ = user.birthday;
                        Log.i(TAG, UserBirthday_);
                        String UserOccupation_ = user.occupation;
                        Log.i(TAG, UserOccupation_);
                        String UserHobbies_ = user.hobbies;
                        Log.i(TAG, UserHobbies_);
                        String UserRelationshipStatus_ = user.relationshipStatus;
                        Log.i(TAG, UserRelationshipStatus_);
                        String UserBodyType_ = user.bodyType;
                        Log.i(TAG, UserBodyType_);
                        //UserProfileImage = (CircleImageView) findViewById(R.id.profile_image);

                        userName.setText(UserName_);
                        userID.setText(UserID_);

                        userDescription.setText(UserDescription_);
                        userEmail.setText(UserEmail_);
                        userGender.setText(UserGender_);
                        userHeight.setText(UserHeight_);
                        userWeight.setText(UserWeight_);
                        userBodyType.setText(UserBodyType_);
                        userCity.setText(UserCity_);
                        userBirthday.setText(UserBirthday_);
                        userOccupation.setText(UserOccupation_);
                        userHobbies.setText(UserHobbies_);
                        userRelationshipStatus.setText(UserRelationshipStatus_);

                        if(!user.profileImageUrl.equals("")) {
                            Glide.with(UserProfile.this)
                                    .load(user.profileImageUrl)
                                    .into(userProfileImage);
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        // [START_EXCLUDE]
                        // [END_EXCLUDE]
                    }
                });

        mDatabase.child(DatabaseConstant.USER_FOLLOW_TABLE).child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserFollow user = dataSnapshot.getValue(UserFollow.class);

                        String followerNumValue = "0";
                        String followingNumValue = "0";

                        if (user != null) {
                            followerNumValue = user.followerNum;
                            Log.e(TAG, followerNumValue);
                            followingNumValue = user.followingNum;
                            Log.e(TAG, followingNumValue);
                        }

                        followerNum.setText(followerNumValue);
                        followingNum.setText(followingNumValue);
                    }
                    // [END single_value_read]

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void onStart() {

        super.onStart();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.out.println("In user profile, back pressed");
    }
}
