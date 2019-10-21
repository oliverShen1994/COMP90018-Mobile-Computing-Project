package com.android.group_12.crushy;

import android.graphics.Point;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    private Point screenSize;
    private int phoneNavigationBarHeight;
    private int appNavigationBarHeight;
    private int fragmentHeight;

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
}
