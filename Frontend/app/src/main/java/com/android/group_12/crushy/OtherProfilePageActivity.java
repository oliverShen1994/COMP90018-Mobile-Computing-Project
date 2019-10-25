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

import com.android.group_12.crushy.Constants.DatabaseConstant;
import com.android.group_12.crushy.Constants.IntentExtraParameterName;
import com.android.group_12.crushy.Constants.RequestCode;
import com.android.group_12.crushy.DatabaseWrappers.User;
import com.android.group_12.crushy.DatabaseWrappers.UserFollow;
import com.android.group_12.crushy.Enums.MainActivityFragmentEnum;
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
        UserProfileImage = (CircleImageView) findViewById(R.id.profile_image);
        UserID = (TextView) findViewById(R.id.UserDescription);
        UserName = (TextView) findViewById(R.id.UserName);
        FollowerNum = (TextView) findViewById(R.id.FollowersNum);
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
        PreviousButton = (LinearLayout) findViewById(R.id.pro_previous);

        retrivePost(id);
    }

    private void retrivePost(String uid) {
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
                        }
                        else{
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
                            if(user.followerNum != null) {
                                followerNum = user.followerNum;
                                //Log.i(TAG, FollowerNum_);
                            }
                            if(user.followingNum != null) {
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
