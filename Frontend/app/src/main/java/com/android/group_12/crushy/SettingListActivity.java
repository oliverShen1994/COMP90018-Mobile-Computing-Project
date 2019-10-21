package com.android.group_12.crushy;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SettingListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_list);

        // TODO: setting放些啥？
//        ListDemoAdapter adapter = new ListDemoAdapter(getActivity(), R.layout.list_example, getFruits());
        ListView listView = findViewById(R.id.setting_list_view);
//        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PersonalInfo pi = (PersonalInfo)adapterView.getItemAtPosition(i);
                Toast.makeText(getApplicationContext(), pi.getName(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
