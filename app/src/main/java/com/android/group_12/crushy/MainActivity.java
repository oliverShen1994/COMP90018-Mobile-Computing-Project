package com.android.group_12.crushy;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.internal.NavigationMenuView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_location:
                    mTextMessage.setText("Location");
                    return true;
                case R.id.navigation_friend_list:
                    mTextMessage.setText(R.string.title_friend_list);
                    return true;
                case R.id.navigation_personal_area:
                    mTextMessage.setText(R.string.title_personal_area);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Get the screen size information.
        Display screen = getWindowManager().getDefaultDisplay();
        Point screenSize = new Point();
        screen.getSize(screenSize);

        int containerHeight = screenSize.y;
        int containerWidth = screenSize.x;

        // Get the system navigation bar height.
        int resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        int navigationBarHeight = 0;
        if (resourceId > 0) {
            navigationBarHeight = (int) getResources().getDimension(resourceId);
        }
        System.out.println("navigationBarHeight: " + navigationBarHeight);

        ImageView userImageView = findViewById(R.id.userImage);

        // Set the image view size, with height:width = 4:3.
        ViewGroup.LayoutParams imageViewParams = userImageView.getLayoutParams();

        // Calculate the remaining height, as the app navigation and button group height are fixed.
        LinearLayout buttonGroup = findViewById(R.id.buttonGroup);
        BottomNavigationView appNavigationBar = findViewById(R.id.nav_view);

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
    }

}
