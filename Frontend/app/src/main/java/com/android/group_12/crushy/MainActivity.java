package com.android.group_12.crushy;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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
    private int phoneNavigationBarHeight;
    private int appNavigationBarHeight;
    private int fragmentHeight;

    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;

    private void updateFragment(int selectedNavigationItemID) {
        Fragment fragment = new Fragment();

        switch (selectedNavigationItemID) {
            case R.id.navigation_location: {
                System.out.println("Location Based Friending");
                fragment = LocationBaseFriendingFragment.newInstance(this.fragmentHeight, this.screenSize.x);
                break;
            }
            case R.id.navigation_friend_list: {
                System.out.println("Friend list");
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

        mAuth = mAuth.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference();
        currentUser = mAuth.getCurrentUser();

        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Bind the event listener with the navigation view.
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Get the screen size information.
        this.screenSize = ScreenUtil.getScreenSize(getApplicationContext());
        System.out.println("this.screenSize" + this.screenSize);

        // Get the system navigation bar height.
//        int resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
//        if (resourceId > 0) {
//            this.phoneNavigationBarHeight = (int) getResources().getDimension(resourceId);
//        }

        this.phoneNavigationBarHeight = ScreenUtil.getHeightOfNavigationBar(getApplicationContext());

        BottomNavigationView appNavigationBar = findViewById(R.id.nav_view);
        this.appNavigationBarHeight = appNavigationBar.getLayoutParams().height;

        this.fragmentHeight = this.screenSize.y - this.phoneNavigationBarHeight - this.appNavigationBarHeight;

        System.out.println("this.phoneNavigationBarHeight:" + this.phoneNavigationBarHeight);
        System.out.println("this.appNavigationBarHeight:" + this.appNavigationBarHeight);
        System.out.println("this.screenSize" + this.screenSize);
        System.out.println("this.fragmentHeight" + this.fragmentHeight);

        this.updateFragment(R.id.navigation_location);

        /*
        ImageView userImageView = findViewById(R.id.userImage);

        // Set the image view size, with height:width = 4:3.
        ViewGroup.LayoutParams imageViewParams = userImageView.getLayoutParams();

        // Calculate the remaining height, as the app navigation and button group height are fixed.
        LinearLayout buttonGroup = findViewById(R.id.buttonGroup);

        int expectedHeight = containerWidth * 4 / 3;
        int remainingHeight = containerHeight - appNavigationBar.getLayoutParams().height - buttonGroup.getLayoutParams().height - navigationBarHeight;

        userImageView.getLayoutParams().width = containerWidth;
        userImageView.getLayoutParams().height = remainingHeight > expectedHeight ? expectedHeight : remainingHeight;

        if (expectedHeight > remainingHeight) {
            userImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }

        // Set the default image.
        userImageView.setImageResource(R.drawable.ic_poker_2);


        System.out.println("image view height = " + imageViewParams.height + ", width = " + imageViewParams.width);
        */
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
            System.out.println(currentUser.toString());

            verifyUserExistence();
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
        String currentUserID = mAuth.getCurrentUser().getUid();
        rootRef.child("Users").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("name").exists()) {
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

}
