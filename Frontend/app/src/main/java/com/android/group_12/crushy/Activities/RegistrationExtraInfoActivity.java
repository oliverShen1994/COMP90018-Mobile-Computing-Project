package com.android.group_12.crushy.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.group_12.crushy.Constants.APIKeys;
import com.android.group_12.crushy.Constants.DatabaseConstant;
import com.android.group_12.crushy.Constants.IntentExtraParameterName;
import com.android.group_12.crushy.DatabaseWrappers.User;
import com.android.group_12.crushy.DatabaseWrappers.UserFollow;
import com.android.group_12.crushy.R;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_WIFI_STATE;

public class RegistrationExtraInfoActivity extends AppCompatActivity {
    private TextView welcomeText;
    private TextInputEditText descriptionInput, occupationInput, locationInput, heightInput, weightInput, bodyTypeInput, dobInput, hobbiesInput, relationshipInput;
    private AutoCompleteTextView genderInput;
    private MaterialButton updateButton, skipButton;

    private String userName, email, currentUserID;

    private DatabaseReference rootRef;
    private LocationManager locationManager;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_extra_info);

        String[] GENDERS = new String[]{"Female", "Male", "Other", "I'd rather not say"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getApplicationContext(),
                R.layout.dropdown_menu_popup_item,
                GENDERS);

        AutoCompleteTextView editTextFilledExposedDropdown = findViewById(R.id.gender_dropdown);
        editTextFilledExposedDropdown.setAdapter(adapter);

        this.rootRef = FirebaseDatabase.getInstance().getReference();
        this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


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
            int permissionLocation = ContextCompat.checkSelfPermission(RegistrationExtraInfoActivity.this, ACCESS_FINE_LOCATION);
            int permissionWifi = ContextCompat.checkSelfPermission(RegistrationExtraInfoActivity.this, ACCESS_WIFI_STATE);

            if (permissionLocation == PackageManager.PERMISSION_GRANTED && permissionWifi == PackageManager.PERMISSION_GRANTED) {
                if (locationManager != null) {
                    System.out.println("location manager is not null");
//                    locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, mLocationListener);
//
//                    Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                    if (lastKnownLocation == null) {
//                        System.out.println("Last known location is null.");
//                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
//                    } else {
//                        System.out.println("lastKnownLocation = " + lastKnownLocation);
//
//
//                    }

                    // Initialize the SDK
                    Places.initialize(getApplicationContext(), APIKeys.PLACES_KEY);

                    // Create a new Places client instance
                    PlacesClient placesClient = Places.createClient(this);

                    // Use fields to define the data types to return.
                    List<Place.Field> placeFields = Collections.singletonList(Place.Field.NAME);

                    // Use the builder to create a FindCurrentPlaceRequest.
                    FindCurrentPlaceRequest request =
                            FindCurrentPlaceRequest.newInstance(placeFields);

                    // Call findCurrentPlace and handle the response (first check that the user has granted permission).
                    if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Task<FindCurrentPlaceResponse> placeResponse = placesClient.findCurrentPlace(request);
                        placeResponse.addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                FindCurrentPlaceResponse response = task.getResult();

                                List<PlaceLikelihood> placeLikelihoods = response.getPlaceLikelihoods();
                                if (placeLikelihoods != null) {
                                    List<Double> likelihoods = placeLikelihoods.stream().map(placeLikelihood -> placeLikelihood.getLikelihood()).collect(Collectors.toList());
                                    Double maxLikelihood = Collections.max(likelihoods);
                                    List<String> mostLikelyPlacesName = placeLikelihoods.stream()
                                            .filter(placeLikelihood -> placeLikelihood.getLikelihood() == maxLikelihood)
                                            .map(placeLikelihood -> placeLikelihood.getPlace().getName()).collect(Collectors.toList());

                                    System.out.println(mostLikelyPlacesName);
                                }

                                for (PlaceLikelihood placeLikelihood : placeLikelihoods) {
                                    System.out.println(String.format("Place '%s' has likelihood: %f",
                                            placeLikelihood.getPlace().getName(),
                                            placeLikelihood.getLikelihood()));
                                }
                            } else {
                                Exception exception = task.getException();
                                if (exception instanceof ApiException) {
                                    ApiException apiException = (ApiException) exception;
                                    System.out.println(("Place not found: " + apiException.getStatusCode()));
                                    System.out.println(apiException.toString());
                                }
                            }
                        });
                    } else {
                        // A local method to request required permissions;
                        // See https://developer.android.com/training/permissions/requesting
//                        getLocationPermission();
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
        });


    }

    private void skipButtonOnClickListener() {
        String userid = currentUserID;
        String name = userName;
        String birthday = "";
        String bodyType = "";
        String city = "";
        String description = "";
        String gender = "";
        String hobbies = "";
        String occupation = "";
        String profileImageUrl = "";
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
        String description = TextUtils.isEmpty(typedDescription) ? "N/A" : typedDescription;
        String gender = TextUtils.isEmpty(typedGender) ? "N/A" : typedGender;
        String hobbies = TextUtils.isEmpty(typedHobbies) ? "N/A" : typedHobbies;
        String occupation = TextUtils.isEmpty(typedOccupation) ? "N/A" : typedOccupation;
        String profileImageUrl = "";
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
}
