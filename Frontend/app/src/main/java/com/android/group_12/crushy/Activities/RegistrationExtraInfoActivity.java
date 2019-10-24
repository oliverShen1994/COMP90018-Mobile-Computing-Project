package com.android.group_12.crushy.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.group_12.crushy.Constants.DatabaseConstant;
import com.android.group_12.crushy.Constants.IntentExtraParameterName;
import com.android.group_12.crushy.DatabaseWrappers.User;
import com.android.group_12.crushy.DatabaseWrappers.UserFollow;
import com.android.group_12.crushy.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Map;

public class RegistrationExtraInfoActivity extends AppCompatActivity {
    private TextView welcomeText;
    private TextInputEditText descriptionInput, occupationInput, heightInput, weightInput, bodyTypeInput, dobInput, hobbiesInput, relationshipInput;
    private AutoCompleteTextView genderInput;
    private MaterialButton updateButton, skipButton;
    private String userName, email, currentUserID;

    private DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_extra_info);

        String[] GENDERS = new String[]{"Female", "Male", "Other", "I'd rather not say"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getApplicationContext(),
                R.layout.dropdown_menu_popup_item,
                GENDERS);

        AutoCompleteTextView editTextFilledExposedDropdown = findViewById(R.id.gender_dropdown);
        editTextFilledExposedDropdown.setAdapter(adapter);

        rootRef = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        if (intent != null) {
            this.userName = intent.getStringExtra(IntentExtraParameterName.REGISTRATION_EXTRA_INFO_ACTIVITY_NAME);
            this.email = intent.getStringExtra(IntentExtraParameterName.REGISTRATION_EXTRA_INFO_ACTIVITY_EMAIL);
            this.currentUserID = intent.getStringExtra(IntentExtraParameterName.REGISTRATION_EXTRA_INFO_ACTIVITY_USER_ID);
        }

        this.welcomeText = findViewById(R.id.extra_info_welcome_text);
        this.descriptionInput = findViewById(R.id.register_description);
        this.occupationInput = findViewById(R.id.register_occupation);
        this.genderInput = findViewById(R.id.gender_dropdown);
        this.heightInput = findViewById(R.id.register_height);
        this.weightInput = findViewById(R.id.register_weight);
        this.bodyTypeInput = findViewById(R.id.register_body_type);
        this.dobInput = findViewById(R.id.register_dob);
        this.hobbiesInput = findViewById(R.id.register_hobbies);
        this.relationshipInput = findViewById(R.id.register_relationship);

        this.updateButton = findViewById(R.id.update_registration_profile_button);
        this.skipButton = findViewById(R.id.registraion_profile_skip_button);

        if (!TextUtils.isEmpty(this.userName)) {
            this.welcomeText.setText("Welcome to Crushy, " + this.userName);
        }

        this.updateButton.setOnClickListener(v -> {
            System.out.println("Update button clicked");

            String typedDescription = descriptionInput.getText().toString();
            String typedOccupation = occupationInput.getText().toString();
            String typedGender = genderInput.getText().toString();
            String typedHeight = heightInput.getText().toString();
            String typedWeight = weightInput.getText().toString();
            String typedBodyType = bodyTypeInput.getText().toString();
            String typedDOB = dobInput.getText().toString();
            String typedHobbies = hobbiesInput.getText().toString();
            String typedRelationship = relationshipInput.getText().toString();

            String userid = currentUserID;
            String name = userName;
            String birthday = TextUtils.isEmpty(typedDOB) ? "N/A" : typedDOB;
            String bodyType = TextUtils.isEmpty(typedBodyType) ? "N/A" : typedBodyType;
            String city = "";
            String description = TextUtils.isEmpty(typedDescription) ? "N/A" : typedDescription;
            String gender = TextUtils.isEmpty(typedGender) ? "N/A" : typedGender;
            String hobbies = TextUtils.isEmpty(typedHobbies) ? "N/A" : typedHobbies;
            String occupation = TextUtils.isEmpty(typedOccupation) ? "N/A" : typedOccupation;
            String profileImageUrl = "";
            String relationshipStatus = TextUtils.isEmpty(typedRelationship) ? "N/A" : typedRelationship;
            String height = TextUtils.isEmpty(typedHeight) ? "N/A" : typedHeight;
            String weight = TextUtils.isEmpty(typedWeight) ? "N/A" : typedWeight;
            ArrayList<String> fansList = new ArrayList<>();
            ArrayList<String> likeList = new ArrayList<>();
            ArrayList<String> friendsList = new ArrayList<>();
            ArrayList<String> blockList = new ArrayList<>();
            ArrayList<String> dislikeList = new ArrayList<>();
            String followerNum = "0";
            String followingNum = "0";

            // The firebase route to the new user
            DatabaseReference currentRecordUser = rootRef.child(DatabaseConstant.USER_TABLE_NAME).child(currentUserID);
            DatabaseReference currentUserFollowers = rootRef.child(DatabaseConstant.USER_FOLLOW_TABLE).child(currentUserID);

            User user = new User(userid, name, birthday, email, bodyType, city, description, gender, hobbies, occupation, profileImageUrl, relationshipStatus, height, weight);
            UserFollow userFollow = new UserFollow(fansList, likeList, friendsList, blockList, dislikeList, followerNum, followingNum);
            // wrap the user info content
            Map<String, Object> postValues = user.toMap();
            Map<String, Object> userFollowValues = userFollow.toMap();
            //set value to User table
            currentRecordUser.setValue(postValues);
            //set value to UserFollow table
            currentUserFollowers.setValue(userFollowValues);

            sendUserToMainActivity();
            Toast.makeText(RegistrationExtraInfoActivity.this, "Profile set successfully", Toast.LENGTH_SHORT).show();
        });

        this.skipButton.setOnClickListener(v -> {
            System.out.println("Skip button clicked");

            //HashMap<String, String> userProfile = new HashMap<>();
            String userid = currentUserID;
            String name = userName;
            String birthday = "";
            String bodyType = "";
            String city = "";
            String description = "";
            String gender = "";
            String hobbies = "";
            String occupation = "";
            String profileImageUrl = "";
            String relationshipStatus = "";
            String height = "";
            String weight = "";
            ArrayList<String> fansList = new ArrayList<>();
            ArrayList<String> likeList = new ArrayList<>();
            ArrayList<String> friendsList = new ArrayList<>();
            ArrayList<String> blockList = new ArrayList<>();
            ArrayList<String> dislikeList = new ArrayList<>();
            String followerNum = "0";
            String followingNum = "0";
            // The firebase route to the new user
            DatabaseReference currentRecordUser = rootRef.child(DatabaseConstant.USER_TABLE_NAME).child(currentUserID);
            DatabaseReference currentUserFollowers = rootRef.child(DatabaseConstant.USER_FOLLOW_TABLE).child(currentUserID);

            User user = new User(userid, name, birthday, email, bodyType, city, description, gender, hobbies, occupation, profileImageUrl, relationshipStatus, height, weight);
            UserFollow userFollow = new UserFollow(fansList, likeList, friendsList, blockList, dislikeList, followerNum, followingNum);
            // wrap the user info content
            Map<String, Object> postValues = user.toMap();
            Map<String, Object> userFollowValues = userFollow.toMap();
            //set value to User table
            currentRecordUser.setValue(postValues);
            //set value to UserFollow table
            currentUserFollowers.setValue(userFollowValues);

            sendUserToMainActivity();
        });

    }


    private void sendUserToMainActivity() {
        Intent mainActivityIntent = new Intent(RegistrationExtraInfoActivity.this, MainActivity.class);
        // Make sure user will not go back to the register activity when press back button.
        mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mainActivityIntent.putExtra(IntentExtraParameterName.MAIN_ACTIVITY_SHOW_WELCOME_TOAST, true);
        startActivity(mainActivityIntent);
    }
}
