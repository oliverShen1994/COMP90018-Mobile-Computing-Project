package com.android.group_12.crushy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditUserImage extends AppCompatActivity {
    private ImageView UserImage;
    private Button Save, Back;
    private static final String TAG = "Edit_Image";
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_image);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        UserImage = (ImageView) findViewById(R.id.profile_image);

        retrivePost("0001");

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent EditIntent = new Intent(EditUserImage.this, EditUserProfile.class);
                startActivity(EditIntent);
            }
        });
    }

    private void retrivePost(String uid) {
        Log.e(TAG, "User " + uid + " is 111111111");
        // Disable button so there are no multi-posts
        //Toast.makeText(this, "Posting...", Toast.LENGTH_SHORT).show();

        // [START single_value_read]
        final String userId = uid;
        mDatabase.child("user-profiles").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);
                        String UserProfileImage_ = user.UserProfileImage;
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });
    }

    public void onStart() {

        super.onStart();

    }
}
