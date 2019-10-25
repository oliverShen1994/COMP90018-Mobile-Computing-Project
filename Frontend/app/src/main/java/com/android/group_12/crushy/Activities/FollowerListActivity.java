package com.android.group_12.crushy.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.group_12.crushy.Fragments.FollowerListFragment;
import com.android.group_12.crushy.MessageActivity;
import com.android.group_12.crushy.R;

public class FollowerListActivity extends AppCompatActivity {
    private static final String TAG = "FollowerListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.follower_list_toolbar);
        setSupportActionBar(toolbar);
        ImageButton backToProfileBtn = findViewById(R.id.back_to_profile);
        backToProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        updateFragment(R.layout.fragment_follow_list_listview);
    }

    private void updateFragment(int selectedFragmentID) {
        Fragment fragment = FollowerListFragment.newInstance(selectedFragmentID);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.personal_info_fragment, fragment)
                .commit();
//                .addToBackStack(null)
    }

//    private void initializeFields() {
//        UserProfileImage = (CircleImageView) findViewById(R.id.profile_image);
//        UserID = (TextView) findViewById(R.id.UserID);
//        UserName = (TextView) findViewById(R.id.UserName);
////        FollowerNum = (TextView)findViewById(R.id.FollowersNum);
////        FollowingNum = (TextView) findViewById(R.id.FollowingNum);
//
//        EditUserName = (EditText) findViewById(R.id.EditUserName);
//        UserDescription = (EditText) findViewById(R.id.EditUserDescription);
//        UserEmail = (EditText) findViewById(R.id.EditUserEmail);
//        UserGender = (EditText) findViewById(R.id.EditUserGender);
//        UserHeight = (EditText) findViewById(R.id.EditUserHeight);
//        UserWeight = (EditText) findViewById(R.id.EditUserWeight);
//        UserCity = (EditText) findViewById(R.id.EditUserCity);
//        UserBirthday = (EditText) findViewById(R.id.EditUserBirthday);
//        UserOccupation = (EditText) findViewById(R.id.EditUserOccupation);
//        UserHobbies = (EditText) findViewById(R.id.EditUserHobbies);
//        UserRelationshipStatus = (EditText) findViewById(R.id.EditUserRelationshipStatus);
//        UserBodyType = (EditText) findViewById(R.id.EditUserBodyType);
//
//        EditImage = (RelativeLayout) findViewById(R.id.UserImage);
//        SaveButton = (LinearLayout) findViewById(R.id.SaveButton);
//        PreviousButton = (LinearLayout) findViewById(R.id.PreviousButton);
//
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        currentUserId = currentUser.getUid();
//        retrievePost(currentUserId);
//    }

//    private void retrievePost(String uid) {
//        Log.i(TAG, "User is " + uid);
//        // [START single_value_read]
//        final String userId = uid;
//        mDatabase.child(DatabaseConstant.USER_FOLLOW_TABLE).child(userId).addListenerForSingleValueEvent(
//                new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        // Get user value
//                        UserFollow userFollow = dataSnapshot.getValue(UserFollow.class);
//
//                        //public User(String UserProfileImage, String UserID, String UserName, String FollowerNum,
//                        // String FollowingNum, String UserDescription, String UserEmail, String UserGender, String UserHeight,
//                        // String UserWeight, String UserCity, String UserBirthday, String UserOccupation, String UserHobbies,
//                        // String UserRelationshipStatus, String UserBodyType)
//
//                        //Log.e(TAG, "User " + userId + " is ");
//                        //Log.e(TAG, "the author is " + user.author);
//                        //Log.e(TAG, user.title);
//                        //Log.e(TAG, user.author);
//                        //Toast.makeText(EditUserProfileActivity.this,
//                        //        "Error: could not fetch user.",
//                        //        Toast.LENGTH_SHORT).show();
//                        // Finish this Activity, back to the stream
//                        // [END_EXCLUDE]
//
//                        String UserID_ = user.userID;
//                        String UserName_ = user.name;
//
//                        UserDescription.setText(user.description);
//                        UserEmail.setText(user.email);
//                        UserGender.setText(user.gender);
//                        UserHeight.setText(user.height);
//                        UserWeight.setText(user.weight);
//                        UserCity.setText(user.city);
//                        UserBirthday.setText(user.birthday);
//                        UserOccupation.setText(user.occupation);
//                        UserHobbies.setText(user.hobbies);
//                        UserRelationshipStatus.setText(user.relationshipStatus);
//                        UserBodyType.setText(user.bodyType);
//                        UserID.setText(UserID_);
//                        UserName.setText(UserName_);
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
//                    }
//                });
//
//    }
}
