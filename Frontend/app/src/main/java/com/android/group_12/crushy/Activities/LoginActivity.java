package com.android.group_12.crushy.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.group_12.crushy.Constants.IntentExtraParameterName;
import com.android.group_12.crushy.R;
import com.android.group_12.crushy.Utils.EmailValidationUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private MaterialButton loginButton, phoneLoginButton, needNewAccountLink, forgetPasswordLink;
    private TextInputEditText userEmail, userPassword;

    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private ProgressBar loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        initializeFields();

        needNewAccountLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                sendUserToRegisterActivity();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                allowUserToLogin();
            }
        });
    }

    private void initializeFields() {
        loginButton = findViewById(R.id.login_button);
//        phoneLoginButton = findViewById(R.id.phone_login_button);
        userEmail = findViewById(R.id.login_email);
        userPassword = findViewById(R.id.login_password);
        needNewAccountLink = findViewById(R.id.need_new_account_link);
        forgetPasswordLink = findViewById(R.id.forget_password_link);
        loadingBar = findViewById(R.id.login_progress_bar);
    }

    private void sendUserToMainActivity() {
        Intent mainActivityIntent = new Intent(LoginActivity.this, MainActivity.class);
        // Make sure user will not go back to the login activity when press back button.
        mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mainActivityIntent.putExtra(IntentExtraParameterName.MAIN_ACTIVITY_SHOW_WELCOME_TOAST, true);

        startActivity(mainActivityIntent);
    }

    private void sendUserToRegisterActivity() {
        Intent registerActivityIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(registerActivityIntent);
    }

    private void allowUserToLogin() {
        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();

        if (TextUtils.isEmpty(email) || !EmailValidationUtil.isValid(email)) {
            Toast.makeText(LoginActivity.this, "Please enter a valid email...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "Please enter password...", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setVisibility(View.VISIBLE);

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                sendUserToMainActivity();
                                Toast.makeText(LoginActivity.this, "Logged in successful...", Toast.LENGTH_SHORT).show();
//                                currentUser = mAuth.getCurrentUser();
                            } else {
                                String message = task.getException().toString();
                                System.out.println("Error:" + message);
                                Toast.makeText(LoginActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                            }
                            loadingBar.setVisibility(View.GONE);
                        }
                    });
        }
    }
}
