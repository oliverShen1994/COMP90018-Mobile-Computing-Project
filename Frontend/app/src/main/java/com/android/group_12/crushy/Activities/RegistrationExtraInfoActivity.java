package com.android.group_12.crushy.Activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.android.group_12.crushy.Constants.DatabaseConstant;
import com.android.group_12.crushy.Constants.GenderConstant;
import com.android.group_12.crushy.Constants.IntentExtraParameterName;
import com.android.group_12.crushy.DatabaseWrappers.User;
import com.android.group_12.crushy.DatabaseWrappers.UserFollow;
import com.android.group_12.crushy.R;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_WIFI_STATE;
import static android.widget.Toast.LENGTH_SHORT;
import static com.android.group_12.crushy.Constants.DatabaseConstant.USER_TABLE_COL_PROFILE_IMAGE;
import static com.android.group_12.crushy.Constants.ImageDIalog.CAPTURE_IMAGE_REQUEST;
import static com.android.group_12.crushy.Constants.ImageDIalog.PICK_IMAGE_REQUEST;
import static com.android.group_12.crushy.Constants.StorageConstant.PROFILE_PICTURE;

public class RegistrationExtraInfoActivity extends AppCompatActivity {
    private TextView welcomeText;
    private TextInputEditText descriptionInput, occupationInput, locationInput, heightInput, weightInput, bodyTypeInput, dobInput, hobbiesInput, relationshipInput;
    private AutoCompleteTextView genderInput;
    private MaterialButton updateButton, skipButton;
    private RelativeLayout imageLayout;
    private ImageView imageView;
    private ProgressBar progressBar;

    private File photoFile = null;
    private Uri mImageUri;
    private String mCurrentPhotoPath, profileImageUrl, userLocation;

    private String userName, email, currentUserID;

    private DatabaseReference rootRef;
    private LocationManager locationManager;
    private StorageReference mStorageRef;
    private Geocoder mGeocoder;

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            System.out.println("onLocationChanged called");
            System.out.println(location);

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            System.out.println("onStatusChanged called");
            System.out.println(status);
            System.out.println(extras);
        }

        @Override
        public void onProviderEnabled(String provider) {
            System.out.println("onProviderEnabled called, provider = " + provider);
        }

        @Override
        public void onProviderDisabled(String provider) {
            System.out.println("onProviderDisabled called, provider = " + provider);
        }
    };



    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("OnResume called");
        System.out.println(this.mImageUri);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_extra_info);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getApplicationContext(),
                R.layout.dropdown_menu_popup_item,
                GenderConstant.OPTIONS);

        AutoCompleteTextView editTextFilledExposedDropdown = findViewById(R.id.gender_dropdown);
        editTextFilledExposedDropdown.setAdapter(adapter);

        this.rootRef = FirebaseDatabase.getInstance().getReference();
        this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        this.mGeocoder = new Geocoder(getApplicationContext(), Locale.US);
        this.mStorageRef = FirebaseStorage.getInstance().getReference(PROFILE_PICTURE);

        Intent intent = getIntent();
        if (intent != null) {
            this.userName = intent.getStringExtra(IntentExtraParameterName.REGISTRATION_EXTRA_INFO_ACTIVITY_NAME);
            this.email = intent.getStringExtra(IntentExtraParameterName.REGISTRATION_EXTRA_INFO_ACTIVITY_EMAIL);
            this.currentUserID = intent.getStringExtra(IntentExtraParameterName.REGISTRATION_EXTRA_INFO_ACTIVITY_USER_ID);
        }

        this.welcomeText = findViewById(R.id.extra_info_welcome_text);
        this.descriptionInput = findViewById(R.id.register_description);
        this.occupationInput = findViewById(R.id.register_occupation);
        this.locationInput = findViewById(R.id.register_location);
        this.genderInput = findViewById(R.id.gender_dropdown);
        this.heightInput = findViewById(R.id.register_height);
        this.weightInput = findViewById(R.id.register_weight);
        this.bodyTypeInput = findViewById(R.id.register_body_type);
        this.dobInput = findViewById(R.id.register_dob);
        this.hobbiesInput = findViewById(R.id.register_hobbies);
        this.relationshipInput = findViewById(R.id.register_relationship);
        this.imageLayout = findViewById(R.id.registraion_image_layout);
        this.imageView = findViewById(R.id.registration_image);
        this.progressBar = findViewById(R.id.registration_progress_bar);

        this.updateButton = findViewById(R.id.update_registration_profile_button);
        this.skipButton = findViewById(R.id.registraion_profile_skip_button);

        if (!TextUtils.isEmpty(this.userName)) {
            this.welcomeText.setText("Welcome to Crushy, " + this.userName);
        }

        this.updateButton.setOnClickListener(v -> {
            System.out.println("Update button clicked");
            updateButtonOnClickListener();
        });

        this.skipButton.setOnClickListener(v -> {
            System.out.println("Skip button clicked");
            skipButtonOnClickListener();
        });

        this.locationInput.setOnClickListener(v -> {
            System.out.println("Location input clicked");
            getUserLocation();
        });

        this.imageLayout.setOnClickListener(v -> {
            System.out.println("Image layout clicked");
            this.showBottomDialog();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Glide.with(this)
                    .load(mImageUri)
                    .into(imageView);
            uploadProfileImage();
        } else if ((requestCode == CAPTURE_IMAGE_REQUEST) && resultCode == RESULT_OK) {
            Bitmap myBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
            imageView.setImageBitmap(myBitmap);

            System.out.println("line 212");
            uploadProfileImage();
        } else {
            displayMessage(getBaseContext(), "Request cancelled or something went wrong.");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        List<String> permissionList = Arrays.asList(permissions);

        if (requestCode == 0) {
            if (permissions.length == 2 && permissionList.contains(Manifest.permission.CAMERA)
                    && permissionList.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    && grantResults.length == 2
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                captureImage();
            } else if (permissions.length == 1 && permissionList.contains(ACCESS_FINE_LOCATION)
                    && grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getUserLocation();
            } else {
            }
        }
    }

    private void getUserLocation() {
        // Change the text once clicked.
        locationInput.setText("Getting your location, please wait...");

        int permissionLocation = ContextCompat.checkSelfPermission(RegistrationExtraInfoActivity.this, ACCESS_FINE_LOCATION);
        int permissionWifi = ContextCompat.checkSelfPermission(RegistrationExtraInfoActivity.this, ACCESS_WIFI_STATE);

        if (permissionLocation == PackageManager.PERMISSION_GRANTED && permissionWifi == PackageManager.PERMISSION_GRANTED) {
            if (locationManager != null) {
//                    System.out.println("location manager is not null");

                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (lastKnownLocation == null) {
//                        System.out.println("Last known location is null.");
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
                } else {
//                        System.out.println("lastKnownLocation = " + lastKnownLocation);
                    try {
                        this.userLocation = getLocationInfoByLatLong(lastKnownLocation);
                    } catch (Exception e) {

                    }

                    locationInput.setText(this.userLocation);
//                        System.out.println("last known place = " + lastKnownAddress);
                }
            }
        } else {
            if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(RegistrationExtraInfoActivity.this,
                        new String[]{ACCESS_FINE_LOCATION}, 0);
            }

            if (permissionWifi != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(RegistrationExtraInfoActivity.this,
                        new String[]{ACCESS_WIFI_STATE}, 0);
            }

            Toast.makeText(RegistrationExtraInfoActivity.this, "Oops, location and/or WiFi permission is not granted", Toast.LENGTH_SHORT).show();
        }
    }


    private String getLocationInfoByLatLong(Location lastKnownLocation) throws IOException {
        if (lastKnownLocation != null) {

            List<Address> addresses = this.mGeocoder.getFromLocation(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), 1);
            if (addresses != null && addresses.size() > 0) {
//                System.out.println("addresses: ");
//                System.out.println(addresses);

                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();

//                System.out.println("city = " + city);
//                System.out.println("state = " + state);
//                System.out.println("country = " + country);

                ArrayList<String> locationComponents = new ArrayList<String>();
                if (!TextUtils.isEmpty(city)) {
                    locationComponents.add(city);
                }

                if (!TextUtils.isEmpty(state)) {
                    locationComponents.add(state);
                }

                if (!TextUtils.isEmpty(country)) {
                    locationComponents.add(country);
                }

                return String.join(", ", locationComponents);
            }
        }

        return null;
    }

    private void skipButtonOnClickListener() {
        String userid = currentUserID;
        String name = userName;
        String birthday = "";
        String bodyType = "";
        String city = "";
        if (this.userLocation != null && !TextUtils.isEmpty(this.userLocation) && !this.userLocation.equals("N/A")) {
            city = this.userLocation;
        }

        String description = "";
        String gender = "";
        String hobbies = "";
        String occupation = "";
        String profileImageUrl = "";
        if (this.profileImageUrl != null && !TextUtils.isEmpty(this.profileImageUrl) && !this.profileImageUrl.equals("N/A")) {
            profileImageUrl = this.profileImageUrl;
        }

        String relationshipStatus = "";
        String height = "";
        String weight = "";
        ArrayList<String> fansList = new ArrayList<>();
        ArrayList<String> likeList = new ArrayList<>();
        ArrayList<String> friendsList = new ArrayList<>();
        ArrayList<String> blockList = new ArrayList<>();
        ArrayList<String> dislikeList = new ArrayList<>();
        String followerNum = "0";
        String followingNum = "0";

        // The firebase route to the current user
        DatabaseReference currentRecordUser = rootRef.child(DatabaseConstant.USER_TABLE_NAME).child(currentUserID);
        DatabaseReference currentUserFollowers = rootRef.child(DatabaseConstant.USER_FOLLOW_TABLE).child(currentUserID);

        User user = new User(userid, name, birthday, email, bodyType, city, description, gender, hobbies, occupation, profileImageUrl, relationshipStatus, height, weight);
        UserFollow userFollow = new UserFollow(fansList, likeList, friendsList, blockList, dislikeList, followerNum, followingNum);
        // wrap the user info content
        Map<String, Object> postValues = user.toMap();
        Map<String, Object> userFollowValues = userFollow.toMap();
        //set value to User table
        currentRecordUser.setValue(postValues);
        //set value to UserFollow table
        currentUserFollowers.setValue(userFollowValues);

        sendUserToMainActivity();
    }

    private void updateButtonOnClickListener() {
        String typedDescription = descriptionInput.getText().toString();
        String typedOccupation = occupationInput.getText().toString();
        String typedGender = genderInput.getText().toString();
        String typedHeight = heightInput.getText().toString();
        String typedWeight = weightInput.getText().toString();
        String typedBodyType = bodyTypeInput.getText().toString();
        String typedDOB = dobInput.getText().toString();
        String typedHobbies = hobbiesInput.getText().toString();
        String typedRelationship = relationshipInput.getText().toString();

        String userid = currentUserID;
        String name = userName;
        String birthday = TextUtils.isEmpty(typedDOB) ? "N/A" : typedDOB;
        String bodyType = TextUtils.isEmpty(typedBodyType) ? "N/A" : typedBodyType;
        String city = "";
        if (this.userLocation != null && !TextUtils.isEmpty(this.userLocation) && !this.userLocation.equals("N/A")) {
            city = this.userLocation;
        }

        String description = TextUtils.isEmpty(typedDescription) ? "N/A" : typedDescription;
        String gender = TextUtils.isEmpty(typedGender) ? "N/A" : typedGender;
        String hobbies = TextUtils.isEmpty(typedHobbies) ? "N/A" : typedHobbies;
        String occupation = TextUtils.isEmpty(typedOccupation) ? "N/A" : typedOccupation;
        String profileImageUrl = "";
        if (this.profileImageUrl != null && !TextUtils.isEmpty(this.profileImageUrl) && !this.profileImageUrl.equals("N/A")) {
            profileImageUrl = this.profileImageUrl;
        }

        String relationshipStatus = TextUtils.isEmpty(typedRelationship) ? "N/A" : typedRelationship;
        String height = TextUtils.isEmpty(typedHeight) ? "N/A" : typedHeight;
        String weight = TextUtils.isEmpty(typedWeight) ? "N/A" : typedWeight;
        ArrayList<String> fansList = new ArrayList<>();
        ArrayList<String> likeList = new ArrayList<>();
        ArrayList<String> friendsList = new ArrayList<>();
        ArrayList<String> blockList = new ArrayList<>();
        ArrayList<String> dislikeList = new ArrayList<>();
        String followerNum = "0";
        String followingNum = "0";

        // The firebase route to the current user
        DatabaseReference currentRecordUser = rootRef.child(DatabaseConstant.USER_TABLE_NAME).child(currentUserID);
        DatabaseReference currentUserFollowers = rootRef.child(DatabaseConstant.USER_FOLLOW_TABLE).child(currentUserID);

        User user = new User(userid, name, birthday, email, bodyType, city, description, gender, hobbies, occupation, profileImageUrl, relationshipStatus, height, weight);
        UserFollow userFollow = new UserFollow(fansList, likeList, friendsList, blockList, dislikeList, followerNum, followingNum);
        // wrap the user info content
        Map<String, Object> postValues = user.toMap();
        Map<String, Object> userFollowValues = userFollow.toMap();
        //set value to User table
        currentRecordUser.setValue(postValues);
        //set value to UserFollow table
        currentUserFollowers.setValue(userFollowValues);

        sendUserToMainActivity();
        Toast.makeText(RegistrationExtraInfoActivity.this, "Profile set successfully", Toast.LENGTH_SHORT).show();
    }


    private void sendUserToMainActivity() {
        Intent mainActivityIntent = new Intent(RegistrationExtraInfoActivity.this, MainActivity.class);
        // Make sure user will not go back to the register activity when press back button.
        mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mainActivityIntent.putExtra(IntentExtraParameterName.MAIN_ACTIVITY_SHOW_WELCOME_TOAST, true);
        startActivity(mainActivityIntent);
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
            this.openFileChooser();
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
                    displayMessage(getBaseContext(), ex.getMessage().toString());
                }
            } else {
                displayMessage(getBaseContext(), "Nullll");
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

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void uploadProfileImage() {
        System.out.println("line 513");
        if (this.mImageUri != null && this.currentUserID != null) {
            System.out.println("line 515");
            final StorageReference fileReference = mStorageRef.child(this.currentUserID);
            this.progressBar.setVisibility(View.VISIBLE);

            fileReference.putFile(this.mImageUri).addOnSuccessListener(taskSnapshot -> {
                System.out.println("line 520");
                Toast.makeText(RegistrationExtraInfoActivity.this, "Succeed", LENGTH_SHORT).show();
                fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    System.out.println("line 523");
                    Uri downloadUrl = uri;
//                    Toast.makeText(getBaseContext(), "Upload success! URL - " + downloadUrl.toString(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getBaseContext(), "Profile Image Uploaded Successfully!", Toast.LENGTH_SHORT).show();
                    this.profileImageUrl = downloadUrl.toString();

                    rootRef.child(DatabaseConstant.USER_TABLE_NAME).child(this.currentUserID).child(USER_TABLE_COL_PROFILE_IMAGE).setValue(downloadUrl.toString());

                    progressBar.setVisibility(View.INVISIBLE);
                });
            }).addOnFailureListener(e -> {
                System.out.println("line 533");
                Toast.makeText(RegistrationExtraInfoActivity.this, "Failed", LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            });
        } else {
            if (this.mImageUri == null) {
                Toast.makeText(this, "No Picture Uri", LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "User ID is not found", LENGTH_SHORT).show();
            }
        }
    }
}
