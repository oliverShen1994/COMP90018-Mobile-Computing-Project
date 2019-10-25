package com.android.group_12.crushy.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.group_12.crushy.Constants.ResultCode;
import com.android.group_12.crushy.R;

public class AboutActivity extends AppCompatActivity {
    private Button Back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Back = (Button) findViewById(R.id.Back);
        Back.setOnClickListener(view -> {
//                setResult(ResultCode.AboutActivity);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        System.out.println("In about page, onBackPressed triggered");
        super.onBackPressed();
    }
}
