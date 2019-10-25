package com.android.group_12.crushy.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.group_12.crushy.Constants.DatabaseConstant;
import com.android.group_12.crushy.DatabaseWrappers.User;
import com.android.group_12.crushy.DatabaseWrappers.UserFollow;
import com.android.group_12.crushy.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {

    private CircleImageView userProfileImage;
    private TextView userName, followerNum, followingNum, userDescription, userEmail,
            userGender, userHeight, userWeight, userCity, userBirthday, userOccupation,
            userHobbies, userRelationshipStatus, userBodyType, editView;
    private LinearLayout editButton, previousButton, following, follower;
    private ProgressBar progressBar;

    private static final String TAG = "UserProfileActivity";

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        currentUserId = currentUser.getUid();
        initializeFields();

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent EditProfileIntent = new Intent(UserProfileActivity.this, EditUserProfileActivity.class);
//                startActivityForResult(EditProfileIntent, RequestCode.UserProfileActivity);
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
                followerIntent = new Intent(UserProfileActivity.this, FollowerListActivity.class);
                startActivity(followerIntent);
            }
        });

        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent followingIntent;
                followingIntent = new Intent(UserProfileActivity.this, FollowingListActivity.class);
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
        this.userProfileImage = findViewById(R.id.profile_image);
        this.userName = findViewById(R.id.UserName);
        this.followerNum = findViewById(R.id.FollowersNum);
        this.followingNum = findViewById(R.id.FollowingNum);
        this.userDescription = findViewById(R.id.UserDescription);
        this.userEmail = findViewById(R.id.UserEmail);
        this.userGender = findViewById(R.id.UserGender);
        this.userHeight = findViewById(R.id.UserHeight);
        this.userWeight = findViewById(R.id.UserWeight);
        this.userCity = findViewById(R.id.UserCity);
        this.userBirthday = findViewById(R.id.UserBirthday);
        this.userOccupation = findViewById(R.id.UserOccupation);
        this.userHobbies = findViewById(R.id.UserHobbies);
        this.userRelationshipStatus = findViewById(R.id.UserRelationshipStatus);
        this.userBodyType = findViewById(R.id.UserBodyType);
        this.follower = findViewById(R.id.Follower);
        this.following = findViewById(R.id.Following);
        this.editButton = findViewById(R.id.EditButton);
        this.previousButton = findViewById(R.id.pro_previous);
        this.progressBar = findViewById(R.id.user_profile_progress_bar);

        retrivePost(currentUserId);
    }

    private void retrivePost(String uid) {
        Log.e(TAG, "User " + uid + " is 111111111");
        // Disable button so there are no multi-posts
//        Toast.makeText(this, "Posting...", Toast.LENGTH_SHORT).show();

        this.progressBar.setVisibility(View.VISIBLE);

        // [START single_value_read]
        final String userId = uid;
        mDatabase.child(DatabaseConstant.USER_TABLE_NAME).child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);
                        String UserProfileImage_ = user.profileImageUrl;
                        String UserName_ = user.name;
                        String UserDescription_ = user.description;
                        String UserEmail_ = user.email;
                        String UserGender_ = user.gender;
                        String UserHeight_ = user.height;
                        String UserWeight_ = user.weight;
                        String UserCity_ = user.city;
                        String UserBirthday_ = user.birthday;
                        String UserOccupation_ = user.occupation;
                        String UserHobbies_ = user.hobbies;
                        String UserRelationshipStatus_ = user.relationshipStatus;
                        String UserBodyType_ = user.bodyType;

                        userName.setText(UserName_);
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

                        if (UserProfileImage_ == null || UserProfileImage_.equals("") || UserProfileImage_.equals("N/A")) {
                            userProfileImage.setImageResource(R.drawable.profile_image);
                        } else {
                            Glide.with(UserProfileActivity.this)
                                    .load(user.profileImageUrl)
                                    .into(userProfileImage);
                        }

                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        progressBar.setVisibility(View.INVISIBLE);
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
                            if (user.followerNum != null) {
                                followerNumValue = user.followerNum;
                                //Log.i(TAG, FollowerNum_);
                            }
                            if (user.followingNum != null) {
                                followingNumValue = user.followingNum;
                                //Log.i(TAG, FollowingNum_);
                            }
                            //UserProfileImarge = (CircleImageView) findViewById(R.id.profile_image);
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
