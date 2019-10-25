package com.android.group_12.crushy.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.android.group_12.crushy.Constants.DatabaseConstant;
import com.android.group_12.crushy.DatabaseWrappers.User;
import com.android.group_12.crushy.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditUserProfileActivity extends AppCompatActivity {
    private CircleImageView UserProfileImage;
    private EditText EditUserName, UserDescription, UserEmail,
            UserGender, UserHeight, UserWeight, UserCity, UserBirthday, UserOccupation,
            UserHobbies, UserRelationshipStatus, UserBodyType;
    private LinearLayout SaveButton, PreviousButton;
    private RelativeLayout EditImage;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String TAG = "EditUserProfileActivity";

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String currentUserId, imageUrl;

    @Override
    protected void onResume() {
        super.onResume();
        initializeFields();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        initializeFields();

        PreviousButton.setOnClickListener(view -> finish());

        SaveButton.setOnClickListener(view -> {
            saveButtonClickListener();
        });

        EditImage.setOnClickListener(view -> {
            Intent EditImageIntent = new Intent(EditUserProfileActivity.this, EditUserImageActivity.class);
            startActivity(EditImageIntent);
        });
    }

    private void saveButtonClickListener() {
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
        String UserProfileImage_ = imageUrl;
        updateUserInfo(EditUserName_, UserDescription_, UserEmail_, UserGender_, UserHeight_, UserWeight_, UserCity_, UserBirthday_,
                UserOccupation_, UserProfileImage_, UserHobbies_, UserRelationshipStatus_, UserBodyType_);

        finish();
    }

    private void initializeFields() {
        UserProfileImage = findViewById(R.id.profile_image);
        EditUserName = findViewById(R.id.EditUserName);
        UserDescription = findViewById(R.id.EditUserDescription);
        UserEmail = findViewById(R.id.EditUserEmail);
        UserGender = findViewById(R.id.EditUserGender);
        UserHeight = findViewById(R.id.EditUserHeight);
        UserWeight = findViewById(R.id.EditUserWeight);
        UserCity = findViewById(R.id.EditUserCity);
        UserBirthday = findViewById(R.id.EditUserBirthday);
        UserOccupation = findViewById(R.id.EditUserOccupation);
        UserHobbies = findViewById(R.id.EditUserHobbies);
        UserRelationshipStatus = findViewById(R.id.EditUserRelationshipStatus);
        UserBodyType = findViewById(R.id.EditUserBodyType);

        EditImage = findViewById(R.id.UserImage);
        SaveButton = findViewById(R.id.SaveButton);
        PreviousButton = findViewById(R.id.PreviousButton);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        currentUserId = currentUser.getUid();
        retrieveUserInfo(currentUserId);
    }

    public void onStart() {
        super.onStart();
    }


    private void retrieveUserInfo(String uid) {
        System.out.println("User " + uid + " is 111111111");

        final String userId = uid;
        mDatabase.child(DatabaseConstant.USER_TABLE_NAME).child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);

                        String UserProfileImage_ = user.profileImageUrl;

                        if (UserProfileImage_ == null || UserProfileImage_.equals("") || UserProfileImage_.equals("N/A")) {
                            UserProfileImage.setImageResource(R.drawable.profile_image);
                        } else {
                            Glide.with(EditUserProfileActivity.this)
                                    .load(user.profileImageUrl)
                                    .into(UserProfileImage);
                            imageUrl = user.profileImageUrl;
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
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("getUser:onCancelled" + databaseError.toException());
                    }
                });
    }

    public void updateUserInfo(String EditUserName_, String UserDescription_, String UserEmail_, String UserGender_,
                               String UserHeight_, String UserWeight_, String UserCity_, String UserBirthday_,
                               String UserOccupation_, String UserProfileImage_, String UserHobbies_, String UserRelationshipStatus_, String UserBodyType_) {
        String UserID_ = currentUserId;
        if(UserProfileImage_ == null){
            UserProfileImage_ = "";
        }

        User post = new User(UserID_, EditUserName_, UserBirthday_, UserEmail_, UserBodyType_, UserCity_, UserDescription_, UserGender_, UserHobbies_, UserOccupation_, UserProfileImage_, UserRelationshipStatus_, UserHeight_, UserWeight_);

        Map<String, Object> postValues = post.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Users/" + UserID_ + '/', postValues);

        mDatabase.updateChildren(childUpdates);
    }
}
