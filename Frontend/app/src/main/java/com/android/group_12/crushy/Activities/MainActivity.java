package com.android.group_12.crushy.Activities;

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
import com.android.group_12.crushy.Constants.IntentExtraParameterName;
import com.android.group_12.crushy.Constants.RequestCode;
import com.android.group_12.crushy.Constants.ResultCode;
import com.android.group_12.crushy.Enums.MainActivityFragmentEnum;
import com.android.group_12.crushy.Fragments.ContactsFragment;
import com.android.group_12.crushy.Fragments.FriendListFragment;
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

import static com.android.group_12.crushy.Enums.MainActivityFragmentEnum.FRIEND_LIST_CHAT;
import static com.android.group_12.crushy.Enums.MainActivityFragmentEnum.FRIEND_LIST_USERS;
import static com.android.group_12.crushy.Enums.MainActivityFragmentEnum.LOCATION_BASED_FRIENDING;
import static com.android.group_12.crushy.Enums.MainActivityFragmentEnum.PERSONAL_AREA;

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
    private MainActivityFragmentEnum fragmentEnum;
    private Boolean showWelcomeToast;

    private void updateFragment() {
        Fragment fragment = new Fragment();

        if (this.fragmentEnum == null || this.fragmentEnum.equals(LOCATION_BASED_FRIENDING)) {
            System.out.println("Location Based Friending");
            fragment = new LocationBaseFriendingFragment(this.fragmentHeight, this.screenSize.x);
        } else if (this.fragmentEnum.equals(FRIEND_LIST_USERS) || this.fragmentEnum.equals(FRIEND_LIST_CHAT)) {
            System.out.println("Friend list");
            // fragment = new FriendListFragment(this.fragmentHeight, this.screenSize.x);
            fragment = new FriendListFragment(this.fragmentHeight, this.screenSize.x);
        } else if (this.fragmentEnum.equals(PERSONAL_AREA)) {
            System.out.println("Personal Area");
            fragment = PersonalAreaFragment.newInstance(this.fragmentHeight, this.screenSize.x);
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.first_level_fragment, fragment)
                .commit();
//                .addToBackStack(null)
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            try {
                switch (item.getItemId()) {
                    case R.id.navigation_location: {
                        fragmentEnum = LOCATION_BASED_FRIENDING;
                        break;
                    }
                    case R.id.navigation_friend_list: {
                        fragmentEnum = FRIEND_LIST_USERS;
                        break;
                    }
                    case R.id.navigation_personal_area: {
                        fragmentEnum = PERSONAL_AREA;
                        break;
                    }
                    default: {
                        break;
                    }
                }
                updateFragment();
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

        Intent intent = this.getIntent();
        this.fragmentEnum = (MainActivityFragmentEnum) intent.getSerializableExtra(IntentExtraParameterName.MAIN_ACTIVITY_TARGETING_FRAGMENT);
        this.showWelcomeToast = intent.getBooleanExtra(IntentExtraParameterName.MAIN_ACTIVITY_SHOW_WELCOME_TOAST, false);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        sendUserToLoginActivity();
//        sendUserToExtraInfoActivity();

//        mAuth.signOut();
//        currentUser = null;

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
            this.updateFragment();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println(requestCode);
        System.out.println(resultCode);
        if (requestCode == RequestCode.PersonalArea) {
            switch (resultCode) {
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
                    Toast.makeText(MainActivity.this, "test default", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendUserToLoginActivity() {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginIntent);
    }

    private void sendUserToExtraInfoActivity() {
        Intent loginIntent = new Intent(MainActivity.this, RegistrationExtraInfoActivity.class);
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
                if (dataSnapshot.child(DatabaseConstant.USER_TABLE_COL_USER_NAME).exists()) {
                    if (showWelcomeToast) {
                        Toast.makeText(MainActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                        showWelcomeToast = false;
                    }
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