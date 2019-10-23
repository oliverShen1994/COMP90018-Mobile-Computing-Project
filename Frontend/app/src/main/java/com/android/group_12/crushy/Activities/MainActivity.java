package com.android.group_12.crushy.Activities;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.group_12.crushy.Constants.DatabaseConstant;
import com.android.group_12.crushy.Constants.RequestCode;
import com.android.group_12.crushy.Constants.ResultCode;
import com.android.group_12.crushy.Fragments.LocationBaseFriendingFragment;
import com.android.group_12.crushy.Fragments.PersonalAreaFragment;
import com.android.group_12.crushy.R;
import com.android.group_12.crushy.Utils.ScreenUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private Point screenSize;
    private BottomNavigationView navView;
    private int phoneNavigationBarHeight;
    private int appNavigationBarHeight;
    private int statusBarHeight;
    private int fragmentHeight;

    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;

    private void updateFragment(int selectedNavigationItemID) {
        Fragment fragment = new Fragment();

        switch (selectedNavigationItemID) {
            case R.id.navigation_location: {
                System.out.println("Location Based Friending");
                fragment = new LocationBaseFriendingFragment(this.fragmentHeight, this.screenSize.x);
                break;
            }
            case R.id.navigation_friend_list: {
                System.out.println("Friend list");
                break;
            }
            case R.id.navigation_personal_area: {
                System.out.println("Personal Area");
                fragment = PersonalAreaFragment.newInstance(this.fragmentHeight, this.screenSize.x);
                break;
            }
            default: {
                // do nothing
            }

        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.first_level_fragment, fragment)
                .addToBackStack(null)
                .commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            try {
                updateFragment(item.getItemId());
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.navView = findViewById(R.id.button_nav);
        // Bind the event listener with the navigation view.
        this.navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        this.mAuth = mAuth.getInstance();
        this.rootRef = FirebaseDatabase.getInstance().getReference();
        this.currentUser = mAuth.getCurrentUser();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (currentUser == null) {
            System.out.println("User is null");
            sendUserToLoginActivity();
        } else {
            System.out.println("User is not null");
            System.out.println("Current user: ");
            System.out.println(currentUser.getUid());
//            System.out.println(currentUser.toString());
            verifyUserExistence(); // Verify user's existence.

            // Height information
            Context currentContext = getApplicationContext();
            this.screenSize = ScreenUtil.getScreenSize(currentContext);
            this.phoneNavigationBarHeight = ScreenUtil.getHeightOfNavigationBar(currentContext);
            this.statusBarHeight = ScreenUtil.getHeightOfStatusBar(currentContext);
            this.appNavigationBarHeight = this.navView.getLayoutParams().height;
            this.fragmentHeight = this.screenSize.y - this.phoneNavigationBarHeight - this.appNavigationBarHeight - this.statusBarHeight;

            this.printHeightInfo();

            // Open navigation location fragment by default.
            this.updateFragment(R.id.navigation_location);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println(requestCode);
        System.out.println(resultCode);
        if (requestCode== RequestCode.PersonalArea){
            switch(resultCode){
                case ResultCode.Follower:
                    Toast.makeText(MainActivity.this, "return PersonalAreaFragment", Toast.LENGTH_SHORT).show();
                case ResultCode.Following:
                    Toast.makeText(MainActivity.this, "return PersonalAreaFragment", Toast.LENGTH_SHORT).show();
                case ResultCode.Setting:
                    Toast.makeText(MainActivity.this, "return PersonalAreaFragment", Toast.LENGTH_SHORT).show();
                case ResultCode.About:
                    Toast.makeText(MainActivity.this, "return PersonalAreaFragment", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(MainActivity.this, "test default" , Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendUserToLoginActivity() {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginIntent);
    }

    private void sendUserToSettingsActivity() {
//        Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
//        settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(settingsIntent);
    }

    private void verifyUserExistence() {
        String currentUserID = this.currentUser.getUid();
        rootRef.child(DatabaseConstant.USER_TABLE_NAME).child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(DatabaseConstant.USER_TABLE__USER_NAME).exists()) {
                    Toast.makeText(MainActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Oops, your name is not set...", Toast.LENGTH_SHORT).show();
                    sendUserToSettingsActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void printHeightInfo() {
        System.out.println("this.phoneNavigationBarHeight = " + this.phoneNavigationBarHeight);
        System.out.println("this.statusBarHeight = " + this.statusBarHeight);
        System.out.println("this.appNavigationBarHeight = " + this.appNavigationBarHeight);
        System.out.println("this.screenSize = " + this.screenSize);
        System.out.println("this.fragmentHeight = " + this.fragmentHeight);
    }
}
