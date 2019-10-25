package com.android.group_12.crushy.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.android.group_12.crushy.About;
import com.android.group_12.crushy.Activities.FollowerListActivity;
import com.android.group_12.crushy.Activities.FollowingListActivity;
import com.android.group_12.crushy.Activities.LoginActivity;
import com.android.group_12.crushy.Activities.UserProfileActivity;
import com.android.group_12.crushy.Constants.DatabaseConstant;
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

//import com.google.android.material.bottomnavigation.BottomNavigationView;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PersonalAreaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PersonalAreaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonalAreaFragment extends Fragment {
    //    private OnFragmentInteractionListener mListener;
    private int fragmentHeight;
    private int fragmentWidth;
    private ImageView userImage;
    private TextView userDescription, userName, followerNum, followingNum;
    private LinearLayout myProfile, following, follower;
    private RelativeLayout blockList, about;
    private Button logoutButton;

    private static final String TAG = "PersonalAreaFragment";
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    public PersonalAreaFragment(int fragmentHeight, int fragmentWidth) {
        this.fragmentHeight = fragmentHeight;
        this.fragmentWidth = fragmentWidth;
    }

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * <p>
     * //     * @param param1 Parameter 1.
     * //     * @param param2 Parameter 2.
     *
     * @return A new instance of fragment PersonalAreaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PersonalAreaFragment newInstance(int fragmentHeight, int fragmentWidth) {
        PersonalAreaFragment fragment = new PersonalAreaFragment(fragmentHeight, fragmentWidth);
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_personal__area, container, false);
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
        this.userImage = view.findViewById(R.id.UserImageView);
        this.userDescription = view.findViewById(R.id.UserDescription);
        this.userName = view.findViewById(R.id.UserName);
        this.followerNum = view.findViewById(R.id.FollowersNum);
        this.followingNum = view.findViewById(R.id.FollowingNum);
        this.myProfile = view.findViewById(R.id.MyProfile);
        this.follower = view.findViewById(R.id.Follower);
        this.following = view.findViewById(R.id.Following);
        this.blockList = view.findViewById(R.id.Blockedlist);
        this.about = view.findViewById(R.id.About);
        this.logoutButton = view.findViewById(R.id.logout_button);

        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
        retrivePost(currentUser.getUid());

        this.follower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent followerIntent;
                followerIntent = new Intent(getActivity(), FollowerListActivity.class);
                startActivity(followerIntent);
            }
        });

        this.following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent followingIntent;
                followingIntent = new Intent(getActivity(), FollowingListActivity.class);
                startActivity(followingIntent);
            }
        });

        this.myProfile.setOnClickListener(view1 -> sendUserToProfileActivity());

        this.userImage.setOnClickListener(v -> sendUserToProfileActivity());

        this.about.setOnClickListener(view12 -> {
            Intent aboutPage;
            aboutPage = new Intent(getActivity(), About.class);
            startActivity(aboutPage);
//                getActivity().startActivityForResult(aboutPage, RequestCode.PersonalArea);
        });

        this.logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            sendUserToLoginActivity();
        });

        return view;
    }


    private void sendUserToLoginActivity() {
        Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
        startActivity(loginIntent);
    }

    private void sendUserToProfileActivity() {
        Intent personalProfileIntent;
        personalProfileIntent = new Intent(getActivity(), UserProfileActivity.class);
        startActivity(personalProfileIntent);
    }

    private void retrivePost(final String uid) {
        Log.i(TAG, "User " + uid + " is 111111111");

        // Disable button so there are no multi-posts
        //Toast.makeText(this, "Posting...", Toast.LENGTH_SHORT).show();

        // [START single_value_read]
        final String userId = uid;
        mDatabase.child(DatabaseConstant.USER_TABLE_NAME).child(uid).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);

                        String profileImageUrl = user.profileImageUrl;
                        String description = user.description;
                        String name = user.name;

                        System.out.println(profileImageUrl);
                        System.out.println(description);
                        System.out.println(name);

                        if (TextUtils.isEmpty(description) || description.equals("N/A")) {
                            userDescription.setText("The user has not said anything...");
                        } else {
                            userDescription.setText(description); //fixme:needed?
                        }
                        userName.setText(name);

                        FragmentActivity fragmentActivity = getActivity();
                        if (fragmentActivity != null) {
                            if (profileImageUrl == null || profileImageUrl.equals("") || profileImageUrl.equals("N/A")) {
                                userImage.setImageResource(R.drawable.profile_image);
                            } else {
                                Glide.with(fragmentActivity)
                                        .load(profileImageUrl)
                                        .into(userImage);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        // [START_EXCLUDE]
                        // [END_EXCLUDE]
                    }
                });

        mDatabase.child(DatabaseConstant.USER_FOLLOW_TABLE).child(uid).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        UserFollow user = dataSnapshot.getValue(UserFollow.class);

                        String followerNumValue = "0";
                        //System.out.println(FollowerNum_);
                        String followingNumValue = "0";

                        if (user != null) {
                            if (user.followerNum != null) {
                                followerNumValue = user.followerNum;
                                //System.out.println(FollowerNum_);
                            }
                            if (user.followingNum != null) {
                                followingNumValue = user.followingNum;
                                //System.out.println(FollowingNum_);
                            }

                            //UserProfileImarge = (CircleImageView) findViewById(R.id.profile_image);
                        }

                        followerNum.setText(followerNumValue);
                        followingNum.setText(followingNumValue);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );
        // [END single_value_read]
    }

//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        System.out.println("PA, line 73");
//        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
//        System.out.println("PA, line 75");
//        layoutParams.height = this.fragmentHeight;
//        view.setLayoutParams(layoutParams);
//        ImageView userImageView = getView().findViewById(R.id.potential_friend_image);
//
//        // Set the image view size, with height:width = 4:3.
//        ViewGroup.LayoutParams imageViewParams = userImageView.getLayoutParams();
//
//        // Calculate the remaining height, as the app navigation and button group height are fixed.
//        LinearLayout buttonGroup = getView().findViewById(R.id.button_group);
//        BottomNavigationView appNavigationBar = getView().findViewById(R.id.nav_view);
//
//        int expectedHeight = this.fragmentWidth * 4 / 3;
//        int remainingHeight = this.fragmentHeight - buttonGroup.getLayoutParams().height;
//
//        userImageView.getLayoutParams().width = this.fragmentWidth;
//        userImageView.getLayoutParams().height = remainingHeight > expectedHeight ? expectedHeight : remainingHeight;
//
//        if (expectedHeight > this.fragmentHeight) {
//            userImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
//        }
//
//        // Set the default image.
//        userImageView.setImageResource(R.drawable.ic_poker_2);
//
//
//        System.out.println("image view height = " + imageViewParams.height + ", width = " + imageViewParams.width);
//    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);

    }


}