package com.android.group_12.crushy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextView;
import android.os.Bundle;
import android.widget.Toast;

public class FollowListActivity extends AppCompatActivity {

    private SearchView mSearchView;
    private Switch mSwitch;
    private TextView mText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_list);

        mSearchView = (SearchView) findViewById(R.id.searchView);
        mSearchView.setSubmitButtonEnabled(true);
        mSwitch = (Switch) findViewById(R.id.viewSwitch);

        // 添加监听
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    updateFragment(R.layout.fragment_follow_list_gridview);
                    Toast.makeText(mSwitch.getContext(), "grid", Toast.LENGTH_SHORT).show();
                }else {
                    updateFragment(R.layout.fragment_follow_list_listview);
                    Toast.makeText(mSwitch.getContext(), "list", Toast.LENGTH_SHORT).show();
                }
            }
        });

        updateFragment(R.layout.fragment_follow_list_listview);
    }

    private void updateFragment(int selectedFragmentID) {
        Fragment fragment = FollowListFragment.newInstance(selectedFragmentID);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.personal_info_fragment, fragment)
                .addToBackStack(null)
                .commit();
    }
}
