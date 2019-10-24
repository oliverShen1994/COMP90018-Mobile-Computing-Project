package com.android.group_12.crushy.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.group_12.crushy.Constants.DatabaseConstant;
import com.android.group_12.crushy.Constants.IntentExtraParameterName;
import com.android.group_12.crushy.DatabaseWrappers.User;
import com.android.group_12.crushy.DatabaseWrappers.UserFollow;
import com.android.group_12.crushy.R;
import com.android.group_12.crushy.Utils.EmailValidationUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private MaterialButton createAccountButton, alreadyHaveAccountLink;
    private TextInputEditText userEmail, userPassword, userPreferredName;
    private FirebaseAuth mAuth;
    private ProgressBar loadingBar;
    private DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initializeFields();
        mAuth = FirebaseAuth.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference();

        alreadyHaveAccountLink.setOnClickListener(view -> {
            // Finish current activity, and go back to the login activity.
            finish();
        });

        createAccountButton.setOnClickListener(view -> createNewAccount());
    }

    private void createNewAccount() {
        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();
        String preferredName = userPreferredName.getText().toString();

        System.out.println("Email: " + email);
        System.out.println("Password: " + password);
        System.out.println("Preferred Name: " + preferredName);

        if (TextUtils.isEmpty(email) || !EmailValidationUtil.isValid(email)) {
            Toast.makeText(RegisterActivity.this, "Please enter a valid email...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(RegisterActivity.this, "Please enter password...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(preferredName)) {
            Toast.makeText(RegisterActivity.this, "Please enter your preferred name...", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setVisibility(View.VISIBLE);

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        String preferredNameValue = userPreferredName.getText().toString();
                        String emailValue = userEmail.getText().toString();

                        if (task.isSuccessful()) {
                            String currentUserID = mAuth.getCurrentUser().getUid();

                            //HashMap<String, String> userProfile = new HashMap<>();
                            String userid = currentUserID;
                            String name = preferredNameValue;
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

                            User user = new User(userid, name, birthday, emailValue, bodyType, city, description, gender, hobbies, occupation, profileImageUrl, relationshipStatus, height, weight);
                            UserFollow userFollow = new UserFollow(fansList, likeList, friendsList, blockList, dislikeList, followerNum, followingNum);
                            // wrap the user info content
                            Map<String, Object> postValues = user.toMap();
                            Map<String, Object> userFollowValues = userFollow.toMap();
                            //set value to User table
                            currentRecordUser.setValue(postValues);
                            //set value to UserFollow table
                            currentUserFollowers.setValue(userFollowValues);

//                            sendUserToMainActivity();
                            sendUserToRegistrationProfileActivity(preferredNameValue, currentUserID, emailValue);
                            Toast.makeText(RegisterActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            String message = task.getException().toString();
                            Toast.makeText(RegisterActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                        loadingBar.setVisibility(View.GONE);
                    });
        }
    }

    private void sendUserToMainActivity() {
        Intent mainActivityIntent = new Intent(RegisterActivity.this, MainActivity.class);
        // Make sure user will not go back to the register activity when press back button.
        mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainActivityIntent);
    }

    private void sendUserToRegistrationProfileActivity(String userName, String userID, String email) {
        Intent registrationActivityIntent = new Intent(RegisterActivity.this, RegistrationExtraInfoActivity.class);
        // Make sure user will not go back to the register activity when press back button.
        registrationActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        registrationActivityIntent.putExtra(IntentExtraParameterName.REGISTRATION_EXTRA_INFO_ACTIVITY_NAME, userName);
        registrationActivityIntent.putExtra(IntentExtraParameterName.REGISTRATION_EXTRA_INFO_ACTIVITY_EMAIL, email);
        registrationActivityIntent.putExtra(IntentExtraParameterName.REGISTRATION_EXTRA_INFO_ACTIVITY_USER_ID, userID);

        startActivity(registrationActivityIntent);
    }


    private void initializeFields() {
        createAccountButton = findViewById(R.id.register_button);
        userEmail = findViewById(R.id.register_email);
        userPassword = findViewById(R.id.register_password);
        userPreferredName = findViewById(R.id.register_name);
        alreadyHaveAccountLink = findViewById(R.id.already_have_account_link);
        loadingBar = findViewById(R.id.registration_progress_bar);
    }
}
