package com.android.group_12.crushy.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.group_12.crushy.Constants.ResultCode;
import com.android.group_12.crushy.R;

public class AboutActivity extends AppCompatActivity {
    private LinearLayout Back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Back = findViewById(R.id.PreviousButton);
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
