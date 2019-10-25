package com.android.group_12.crushy.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.group_12.crushy.Constants.DatabaseConstant;
import com.android.group_12.crushy.DatabaseWrappers.User;
import com.android.group_12.crushy.DatabaseWrappers.UserFollow;
import com.android.group_12.crushy.R;
import com.android.group_12.crushy.Utils.StringUtil;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.android.group_12.crushy.Constants.IntentExtraParameterName.UNIFORM_EXTRA_INFO_ACTIVITY_USER_ID;

public class OtherProfilePageActivity extends AppCompatActivity {
    private CircleImageView UserProfileImage;

    private TextView UserID, UserName, FollowerNum, FollowingNum, UserDescription, UserEmail,
            UserGender, UserHeight, UserWeight, UserCity, UserBirthday, UserOccupation,
            UserHobbies, UserRelationshipStatus, UserBodyType;
    private LinearLayout PreviousButton;
    private static final String TAG = "OtherUserActivity";

    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile_page);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Intent intent = getIntent();
        String aimID = intent.getStringExtra(UNIFORM_EXTRA_INFO_ACTIVITY_USER_ID);
        System.out.println(aimID);
        initializeFields(aimID);
        PreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    private void initializeFields(String id) {
        this.UserProfileImage = findViewById(R.id.UserImageView);
        this.UserID = findViewById(R.id.UserDescription);
        this.UserName = findViewById(R.id.UserName);
        this.FollowerNum = findViewById(R.id.FollowersNum);
        this.FollowingNum = findViewById(R.id.FollowingNum);
        this.UserDescription = findViewById(R.id.UserDescription);
        this.UserEmail = findViewById(R.id.UserEmail);
        this.UserGender = findViewById(R.id.UserGender);
        this.UserHeight = findViewById(R.id.UserHeight);
        this.UserWeight = findViewById(R.id.UserWeight);
        this.UserCity = findViewById(R.id.UserCity);
        this.UserBirthday = findViewById(R.id.UserBirthday);
        this.UserOccupation = findViewById(R.id.UserOccupation);
        this.UserHobbies = findViewById(R.id.UserHobbies);
        this.UserRelationshipStatus = findViewById(R.id.UserRelationshipStatus);
        this.UserBodyType = findViewById(R.id.UserBodyType);
        this.PreviousButton = findViewById(R.id.pro_previous);

        retrievePost(id);
    }

    private void retrievePost(String uid) {
        // Disable button so there are no multi-posts
        Toast.makeText(this, "Retrieving information...", Toast.LENGTH_SHORT).show();

        // [START single_value_read]
        final String userId = uid;
        mDatabase.child(DatabaseConstant.USER_TABLE_NAME).child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);
                        String UserProfileImage_ = StringUtil.replaceNullOrEmptyString(user.profileImageUrl);
//                        Log.i(TAG, UserProfileImage_);
                        String UserID_ = StringUtil.replaceNullOrEmptyString(user.userID);
//                        Log.i(TAG, UserID_);
                        String UserName_ = StringUtil.replaceNullOrEmptyString(user.name);
//                        Log.i(TAG, UserName_);
                        String UserEmail_ = StringUtil.replaceNullOrEmptyString(user.email);
//                        Log.i(TAG, UserEmail_);
                        String UserGender_ = StringUtil.replaceNullOrEmptyString(user.gender);
//                        Log.i(TAG, UserGender_);
                        String UserHeight_ = StringUtil.replaceNullOrEmptyString(user.height);
//                        Log.i(TAG, UserHeight_);
                        String UserWeight_ = StringUtil.replaceNullOrEmptyString(user.weight);
//                        Log.i(TAG, UserWeight_);
                        String UserCity_ = StringUtil.replaceNullOrEmptyString(user.city);
//                        Log.i(TAG, UserCity_);
                        String UserBirthday_ = StringUtil.replaceNullOrEmptyString(user.birthday);
//                        Log.i(TAG, UserBirthday_);
                        String UserOccupation_ = StringUtil.replaceNullOrEmptyString(user.occupation);
//                        Log.i(TAG, UserOccupation_);
                        String UserHobbies_ = StringUtil.replaceNullOrEmptyString(user.hobbies);
//                        Log.i(TAG, UserHobbies_);
                        String UserRelationshipStatus_ = StringUtil.replaceNullOrEmptyString(user.relationshipStatus);
//                        Log.i(TAG, UserRelationshipStatus_);
                        String UserBodyType_ = StringUtil.replaceNullOrEmptyString(user.bodyType);
//                        Log.i(TAG, UserBodyType_);
                        //UserProfileImage = (CircleImageView) findViewById(R.id.profile_image);

                        String UserDescription_ = user.description;
                        if (TextUtils.isEmpty(UserDescription_) || UserDescription_.equals("N/A")) {
                            UserDescription_ = "The user has not said anything...";
                        }


                        UserName.setText(UserName_);
                        UserID.setText(UserID_);
                        UserDescription.setText(UserDescription_);
                        UserEmail.setText(UserEmail_);
                        UserGender.setText(UserGender_);
                        UserHeight.setText(UserHeight_);
                        UserWeight.setText(UserWeight_);
                        UserBodyType.setText(UserBodyType_);
                        UserCity.setText(UserCity_);
                        UserBirthday.setText(UserBirthday_);
                        UserOccupation.setText(UserOccupation_);
                        UserHobbies.setText(UserHobbies_);
                        UserRelationshipStatus.setText(UserRelationshipStatus_);

                        if (user.profileImageUrl.equals("")) {
                            UserProfileImage.setImageResource(R.drawable.profile_image);
                        } else {
                            Glide.with(OtherProfilePageActivity.this)
                                    .load(user.profileImageUrl)
                                    .into(UserProfileImage);
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

                        String followerNum = "0";
                        String followingNum = "0";

                        if (user != null) {
                            if (user.followerNum != null) {
                                followerNum = user.followerNum;
                                //Log.i(TAG, FollowerNum_);
                            }
                            if (user.followingNum != null) {
                                followingNum = user.followingNum;
                                //Log.i(TAG, FollowingNum_);
                            }
                        }

                        FollowerNum.setText(followerNum);
                        FollowingNum.setText(followingNum);

                    }
                    // [END single_value_read]

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });
    }
}
