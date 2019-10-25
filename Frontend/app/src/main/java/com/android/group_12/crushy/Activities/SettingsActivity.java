package com.android.group_12.crushy.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.android.group_12.crushy.R;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity {
    private Button logoutButton, Back;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mAuth = mAuth.getInstance();
        logoutButton = (Button) findViewById(R.id.logout_button);
        Back = (Button) findViewById(R.id.BackButton);

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                sendUserToLoginActivity();
            }
        });
    }

    private void sendUserToLoginActivity() {
        Intent loginIntent = new Intent(SettingsActivity.this, LoginActivity.class);
        startActivity(loginIntent);
    }

    @Override
    public void onBackPressed() {
        System.out.println("In settings activity, back pressed.");
        super.onBackPressed();
    }
}
