package com.android.group_12.crushy.Activities;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.android.group_12.crushy.Constants.DatabaseConstant;
import com.android.group_12.crushy.DatabaseWrappers.User;
import com.android.group_12.crushy.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.widget.Toast.LENGTH_SHORT;
import static com.android.group_12.crushy.Constants.DatabaseConstant.USER_TABLE_COL_PROFILE_IMAGE;
import static com.android.group_12.crushy.Constants.StorageConstant.PROFILE_PICTURE;

public class EditUserImageActivity extends AppCompatActivity {
    private ImageView UserImage;
    private Button Save, Back;
    private ProgressBar progressBar;

    private static final String TAG = "Edit_Image";

    private DatabaseReference mDatabase;
    private StorageReference mStorageRef;

    private String currentUserId, mCurrentPhotoPath;
    private FirebaseAuth mAuth;
    private File photoFile = null;

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAPTURE_IMAGE_REQUEST = 2;
    private Uri mImageUri;
    private static final String IMAGE_DIRECTORY_NAME = "VLEMONN";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_image);

        mStorageRef = FirebaseStorage.getInstance().getReference(PROFILE_PICTURE);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        currentUserId = currentUser.getUid();

        UserImage = findViewById(R.id.UserImageView);
        Save = findViewById(R.id.button2);
        Back = findViewById(R.id.button);
        progressBar = findViewById(R.id.edit_image_progress_bar);

        retrievePost(currentUserId);


        Save.setOnClickListener(view -> uploadFile());

        Back.setOnClickListener(view -> {
//                Intent personalProfileIntent;
//                personalProfileIntent = new Intent(EditUserImageActivity.this, UserProfileActivity.class);
//                startActivity(personalProfileIntent);
            finish();
        });

        UserImage.setOnClickListener(view -> showBottomDialog());
    }

    private void retrievePost(String uid) {
        progressBar.setVisibility(View.VISIBLE);
        mDatabase.child(DatabaseConstant.USER_TABLE_NAME).child(uid).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        progressBar.setVisibility(View.INVISIBLE);
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);
                        if (user.profileImageUrl != null) {
                            UserImage.setImageResource(R.drawable.profile_image);
                        } else {
                            Glide.with(EditUserImageActivity.this)
                                    .load(user.profileImageUrl)
                                    .into(UserImage);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Glide.with(this)
                    .load(mImageUri)
                    .into(UserImage);
        }

        if (requestCode == CAPTURE_IMAGE_REQUEST && resultCode == RESULT_OK) {
            Bitmap myBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
            UserImage.setImageBitmap(myBitmap);
        } else {
            displayMessage(getBaseContext(), "Request cancelled or something went wrong.");
        }
    }


    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void uploadFile() {
        if (mImageUri != null) {
            final StorageReference fileReference = mStorageRef.child(currentUserId);
            progressBar.setVisibility(View.VISIBLE);

            fileReference.putFile(mImageUri).addOnSuccessListener(taskSnapshot -> {
                Toast.makeText(EditUserImageActivity.this, "Succeed", LENGTH_SHORT).show();
                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Uri downloadUrl = uri;
                        Toast.makeText(getBaseContext(), "Upload success! URL - " + downloadUrl.toString(), Toast.LENGTH_SHORT).show();
                        mDatabase.child(DatabaseConstant.USER_TABLE_NAME).child(currentUserId).child(USER_TABLE_COL_PROFILE_IMAGE).setValue(downloadUrl.toString());

                        progressBar.setVisibility(View.INVISIBLE);
                        finish();
                    }
                });
            }).addOnFailureListener(e -> {
                Toast.makeText(EditUserImageActivity.this, "Failed", LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            });
        } else {
            Toast.makeText(this, "No Picture Uri", LENGTH_SHORT).show();
        }
    }

    public void onStart() {
        super.onStart();
    }


    private void showBottomDialog() {
        //1、使用Dialog、设置style
        final Dialog dialog = new Dialog(this, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(this, R.layout.dialog_custom_layout, null);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        dialog.findViewById(R.id.tv_take_photo).setOnClickListener(view13 -> {
            captureImage();
            dialog.dismiss();
        });

        dialog.findViewById(R.id.tv_take_pic).setOnClickListener(view12 -> {
            openFileChooser();
            dialog.dismiss();
        });

        dialog.findViewById(R.id.tv_cancel).setOnClickListener(view1 -> dialog.dismiss());

    }

    /**
     * The codes below is mainly referred from the blog "Android Capture Image From Camera and Get Image Save Path" by Mayank Sanghvi.
     * Acknowledgement: https://vlemon.com/blog/android/android-capture-image-from-camera-and-get-image-save-path/
     */
    private void captureImage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        } else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                try {

                    photoFile = createImageFile();
                    displayMessage(getBaseContext(), photoFile.getAbsolutePath());
                    Log.i("Mayank", photoFile.getAbsolutePath());

                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(this,
                                "com.vlemonn.blog.captureimage.fileprovider",
                                photoFile);

                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        mImageUri = photoURI;
                        startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST);
                    }
                } catch (Exception ex) {
                    // Error occurred while creating the File
//                    displayMessage(getBaseContext(), ex.getMessage().toString());
                }


            } else {
//                displayMessage(getBaseContext(), "Null");
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void displayMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                captureImage();
            }
        }

    }
}
