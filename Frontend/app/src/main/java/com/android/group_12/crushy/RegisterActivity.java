package com.android.group_12.crushy;

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

import com.android.group_12.crushy.Constants.Database;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private Button createAccountButton;
    private EditText userEmail, userPassword, userPreferredName;
    private TextView alreadyHaveAccountLink;

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

        alreadyHaveAccountLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Finish current activity, and go back to the login activity.
                finish();
            }
        });

        createAccountButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                createNewAccount();
            }
        });
    }

    private void createNewAccount() {
        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();
        String preferredName = userPreferredName.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(RegisterActivity.this, "Please enter email...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(RegisterActivity.this, "Please enter password...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(preferredName)) {
            Toast.makeText(RegisterActivity.this, "Please enter your preferred name...", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setVisibility(View.VISIBLE);

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            String preferredName = userPreferredName.getText().toString();
                            String email = userEmail.getText().toString();

                            if (task.isSuccessful()) {
                                String currentUserID = mAuth.getCurrentUser().getUid();

                                HashMap<String, String> userProfile = new HashMap<>();
                                userProfile.put("name", preferredName);
                                userProfile.put("email", email);

                                userProfile.put("birthday", "");
                                userProfile.put("bodyType", "");
                                userProfile.put("city", "");
                                userProfile.put("description", "");
                                userProfile.put("gender", "");
                                userProfile.put("hobbies", "");
                                userProfile.put("occupation", "");
                                userProfile.put("profileImageUrl", "");
                                userProfile.put("relationshipStatus", "");
                                userProfile.put("height", "");
                                userProfile.put("weight", "");

                                DatabaseReference currentRecord =rootRef.child(Database.USER_TABLE_NAME).child(currentUserID);
                                currentRecord.setValue(userProfile);
                                currentRecord.child("fansList").setValue("");
                                currentRecord.child("likeList").setValue("");
                                currentRecord.child("friendsList").setValue("");

                                sendUserToMainActivity();
                                Toast.makeText(RegisterActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                String message = task.getException().toString();
                                Toast.makeText(RegisterActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                            }
                            loadingBar.setVisibility(View.GONE);
                        }
                    });
        }
    }

    private void sendUserToMainActivity() {
        Intent mainActivityIntent = new Intent(RegisterActivity.this, MainActivity.class);
        // Make sure user will not go back to the register activity when press back button.
        mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainActivityIntent);
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
