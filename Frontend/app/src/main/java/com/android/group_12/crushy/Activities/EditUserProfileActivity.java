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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_WIFI_STATE;
import static android.widget.Toast.LENGTH_SHORT;
import static com.android.group_12.crushy.Constants.DatabaseConstant.USER_TABLE_COL_PROFILE_IMAGE;
import static com.android.group_12.crushy.Constants.ImageDIalog.CAPTURE_IMAGE_REQUEST;
import static com.android.group_12.crushy.Constants.StorageConstant.PROFILE_PICTURE;

public class EditUserProfileActivity extends AppCompatActivity {
    private CircleImageView UserProfileImage;
    private EditText EditUserName, UserDescription, UserEmail,
            UserGender, UserHeight, UserWeight, UserCity, UserBirthday, UserOccupation,
            UserHobbies, UserRelationshipStatus, UserBodyType;
    private TextView nameDisplayView;
    private RelativeLayout topCard;
    private ProgressBar progressBar;
    private LinearLayout SaveButton, PreviousButton;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String TAG = "EditUserProfileActivity";

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;

    private String currentUserID, imageUrl, userName, email;

    private File photoFile = null;
    private Uri mImageUri;
    private String mCurrentPhotoPath, profileImageUrl, userLocation;
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
        initializeFields();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        initializeFields();
        PreviousButton.setOnClickListener(view -> finish());
        SaveButton.setOnClickListener(view -> {
            saveButtonClickListener();
        });

//
//        UserProfileImage.setOnClickListener(view -> {
//            Intent EditImageIntent = new Intent(EditUserProfileActivity.this, EditUserImageActivity.class);
//            startActivity(EditImageIntent);
//        });

        this.topCard.setOnClickListener(v -> showBottomDialog());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getApplicationContext(),
                R.layout.dropdown_menu_popup_item,
                GenderConstant.OPTIONS);

        AutoCompleteTextView editTextFilledExposedDropdown = findViewById(R.id.EditUserGender);
        editTextFilledExposedDropdown.setAdapter(adapter);

        this.EditUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                System.out.println(s);
                nameDisplayView.setText(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void onStart() {
        super.onStart();
    }

    public void updateUserInfo(String EditUserName_, String UserDescription_, String UserEmail_, String UserGender_,
                               String UserHeight_, String UserWeight_, String UserCity_, String UserBirthday_,
                               String UserOccupation_, String UserProfileImage_, String UserHobbies_, String UserRelationshipStatus_, String UserBodyType_) {
        User post = new User(this.currentUserID, EditUserName_, UserBirthday_, UserEmail_, UserBodyType_, UserCity_, UserDescription_, UserGender_, UserHobbies_, UserOccupation_, UserProfileImage_, UserRelationshipStatus_, UserHeight_, UserWeight_);

        Map<String, Object> postValues = post.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Users/" + this.currentUserID + '/', postValues);

        mDatabase.updateChildren(childUpdates);
    }


    private void saveButtonClickListener() {
        String EditUserName_ = EditUserName.getText().toString();
        String UserDescription_ = UserDescription.getText().toString();
        String UserEmail_ = UserEmail.getText().toString();
        String UserGender_ = UserGender.getText().toString();
        String UserHeight_ = UserHeight.getText().toString();
        String UserWeight_ = UserWeight.getText().toString();
        String UserCity_ = UserCity.getText().toString();
        String UserBirthday_ = UserBirthday.getText().toString();
        String UserOccupation_ = UserOccupation.getText().toString();
        String UserHobbies_ = UserHobbies.getText().toString();
        String UserRelationshipStatus_ = UserRelationshipStatus.getText().toString();
        String UserBodyType_ = UserBodyType.getText().toString();
        String UserProfileImage_ = imageUrl;
        updateUserInfo(EditUserName_, UserDescription_, UserEmail_, UserGender_, UserHeight_, UserWeight_, UserCity_, UserBirthday_,
                UserOccupation_, UserProfileImage_, UserHobbies_, UserRelationshipStatus_, UserBodyType_);

        finish();
    }

    private void initializeFields() {
        this.UserProfileImage = findViewById(R.id.profile_image);
        this.EditUserName = findViewById(R.id.EditUserName);
        this.UserDescription = findViewById(R.id.EditUserDescription);
        this.UserEmail = findViewById(R.id.EditUserEmail);
        this.UserGender = findViewById(R.id.EditUserGender);
        this.UserHeight = findViewById(R.id.EditUserHeight);
        this.UserWeight = findViewById(R.id.EditUserWeight);
        this.UserCity = findViewById(R.id.EditUserCity);
        this.UserBirthday = findViewById(R.id.EditUserBirthday);
        this.UserOccupation = findViewById(R.id.EditUserOccupation);
        this.UserHobbies = findViewById(R.id.EditUserHobbies);
        this.UserRelationshipStatus = findViewById(R.id.EditUserRelationshipStatus);
        this.UserBodyType = findViewById(R.id.EditUserBodyType);
        this.topCard = findViewById(R.id.Card);
        this.progressBar = findViewById(R.id.edit_profile_progress_bar);
        this.nameDisplayView = findViewById(R.id.edit_profile_name_display);

        this.SaveButton = findViewById(R.id.SaveButton);
        this.PreviousButton = findViewById(R.id.PreviousButton);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            this.currentUserID = currentUser.getUid();
        }

        this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        this.mGeocoder = new Geocoder(getApplicationContext(), Locale.US);
        this.mStorageRef = FirebaseStorage.getInstance().getReference(PROFILE_PICTURE);
        this.rootRef = FirebaseDatabase.getInstance().getReference();

        retrieveUserInfo(currentUserID);
    }

    private void retrieveUserInfo(String uid) {
        System.out.println("User " + uid + " is 111111111");
        final String userId = uid;
        mDatabase.child(DatabaseConstant.USER_TABLE_NAME).child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);
                        String UserProfileImage_ = user.profileImageUrl;

                        if (UserProfileImage_ == null || UserProfileImage_.equals("") || UserProfileImage_.equals("N/A")) {
                            UserProfileImage.setImageResource(R.drawable.profile_image);
                        } else {
                            Glide.with(EditUserProfileActivity.this)
                                    .load(user.profileImageUrl)
                                    .into(UserProfileImage);
                            imageUrl = user.profileImageUrl;
                        }

                        EditUserName.setText(user.name);
                        UserBirthday.setText(user.birthday);
                        UserBodyType.setText(user.bodyType);
                        UserCity.setText(user.city);
                        UserDescription.setText(user.description);
                        UserEmail.setText(user.email);
                        UserGender.setText(user.gender);
                        UserHobbies.setText(user.hobbies);
                        UserOccupation.setText(user.occupation);
                        UserRelationshipStatus.setText(user.relationshipStatus);
                        UserHeight.setText(user.height);
                        UserWeight.setText(user.weight);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("getUser:onCancelled" + databaseError.toException());
                    }
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
                    .into(UserProfileImage);
            uploadProfileImage();
        } else if ((requestCode == CAPTURE_IMAGE_REQUEST) && resultCode == RESULT_OK) {
            Bitmap myBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
            UserProfileImage.setImageBitmap(myBitmap);

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
        UserCity.setText("Getting your location, please wait...");

        int permissionLocation = ContextCompat.checkSelfPermission(EditUserProfileActivity.this, ACCESS_FINE_LOCATION);
        int permissionWifi = ContextCompat.checkSelfPermission(EditUserProfileActivity.this, ACCESS_WIFI_STATE);

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

                    UserCity.setText(this.userLocation);
//                        System.out.println("last known place = " + lastKnownAddress);
                }
            }
        } else {
            if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(EditUserProfileActivity.this,
                        new String[]{ACCESS_FINE_LOCATION}, 0);
            }

            if (permissionWifi != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(EditUserProfileActivity.this,
                        new String[]{ACCESS_WIFI_STATE}, 0);
            }

            Toast.makeText(EditUserProfileActivity.this, "Oops, location and/or WiFi permission is not granted", Toast.LENGTH_SHORT).show();
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
//                    displayMessage(getBaseContext(), photoFile.getAbsolutePath());
//                    Log.i("Mayank", photoFile.getAbsolutePath());

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
                displayMessage(getBaseContext(), "Oops, somwthing goes wrong...");
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
//        System.out.println("line 513");
        if (this.mImageUri != null && this.currentUserID != null) {
//            System.out.println("line 515");
            final StorageReference fileReference = mStorageRef.child(this.currentUserID);
            this.progressBar.setVisibility(View.VISIBLE);
            this.SaveButton.setEnabled(false);
            this.PreviousButton.setEnabled(false);

            fileReference.putFile(this.mImageUri).addOnSuccessListener(taskSnapshot -> {
//                System.out.println("line 520");
                Toast.makeText(EditUserProfileActivity.this, "Succeed", LENGTH_SHORT).show();
                fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
//                    System.out.println("line 523");
                    Uri downloadUrl = uri;
//                    Toast.makeText(getBaseContext(), "Upload success! URL - " + downloadUrl.toString(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getBaseContext(), "Profile Image Uploaded Successfully!", Toast.LENGTH_SHORT).show();

                    this.profileImageUrl = downloadUrl.toString();
                    this.rootRef.child(DatabaseConstant.USER_TABLE_NAME).child(this.currentUserID).child(USER_TABLE_COL_PROFILE_IMAGE).setValue(downloadUrl.toString());
                    this.progressBar.setVisibility(View.INVISIBLE);
                    this.SaveButton.setEnabled(true);
                    this.PreviousButton.setEnabled(true);


                    initializeFields();
                });
            }).addOnFailureListener(e -> {
//                System.out.println("line 533");
                Toast.makeText(EditUserProfileActivity.this, "Failed", LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
                this.SaveButton.setEnabled(true);
                this.PreviousButton.setEnabled(true);
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
