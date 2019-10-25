package com.android.group_12.crushy.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.android.group_12.crushy.Fragments.FollowingListFragment;
import com.android.group_12.crushy.R;

public class FollowingListActivity extends AppCompatActivity {
    private static final String TAG = "FollowingListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.following_list_toolbar);
        setSupportActionBar(toolbar);
        ImageButton backToProfileBtn = findViewById(R.id.back_to_profile);
        backToProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        updateFragment(R.layout.fragment_follow_list_listview);
    }

    private void updateFragment(int selectedFragmentID) {
        Fragment fragment = FollowingListFragment.newInstance(selectedFragmentID);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.personal_info_fragment, fragment)
                .commit();
//                .addToBackStack(null)
    }
}
