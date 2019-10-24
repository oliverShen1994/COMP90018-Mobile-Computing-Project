package com.android.group_12.crushy.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.group_12.crushy.Activities.FollowerListActivity;
import com.android.group_12.crushy.Activities.FollowingListActivity;
import com.android.group_12.crushy.Constants.DatabaseConstant;
import com.android.group_12.crushy.DatabaseWrappers.User;
import com.android.group_12.crushy.DatabaseWrappers.UserFollow;
import com.android.group_12.crushy.R;
import com.android.group_12.crushy.*;
import com.android.group_12.crushy.Constants.*;
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
    private TextView userID,userName,followerNum,followingNum;
    private LinearLayout myProfile,following,follower;
    private RelativeLayout calendar,blockList,setting,about;
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
     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
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
        mDatabase = FirebaseDatabase.getInstance().getReference();
        userImage = (ImageView) view.findViewById(R.id.UserImageView);
        userID = (TextView) view.findViewById(R.id.UserID);
        userName = (TextView) view.findViewById(R.id.UserName);
        followerNum = (TextView) view.findViewById(R.id.FollowersNum);
        followingNum = (TextView) view.findViewById(R.id.FollowingNum);
        myProfile = (LinearLayout) view.findViewById(R.id.MyProfile);
        follower = (LinearLayout) view.findViewById(R.id.Follower);
        following = (LinearLayout) view.findViewById(R.id.Following);
        calendar = (RelativeLayout) view.findViewById(R.id.Calendar);
        blockList = (RelativeLayout) view.findViewById(R.id.Blockedlist);
        setting = (RelativeLayout) view.findViewById(R.id.Setting);
        about = (RelativeLayout) view.findViewById(R.id.About);
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
        retrivePost(currentUser.getUid());

        follower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent followerIntent;
                followerIntent = new Intent(getActivity(), FollowerListActivity.class);
                startActivity(followerIntent);
            }
        });

        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent followingIntent;
                followingIntent = new Intent(getActivity(), FollowingListActivity.class);
                startActivity(followingIntent);
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent settingIntent;
                settingIntent = new Intent(getActivity(), Settings.class);
                startActivity(settingIntent);
            }
        });

        myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent personalProfileIntent;
                personalProfileIntent = new Intent(getActivity(), UserProfile.class);
                startActivity(personalProfileIntent);
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent aboutPage;
                aboutPage = new Intent(getActivity(), About.class);
                startActivity(aboutPage);
//                getActivity().startActivityForResult(aboutPage, RequestCode.PersonalArea);
            }
        });
        return view;
    }

    private void retrivePost(String uid) {
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

                        String UserProfileImage_ = user.profileImageUrl;
                        Log.i(TAG, UserProfileImage_);
                        String UserID_ = user.userID;
                        Log.i(TAG, UserID_);
                        String UserName_ = user.name;
                        Log.i(TAG, UserName_);

                        userID.setText(UserID_); // fixme:needed?
                        userName.setText(UserName_);
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

                        String followerNum = "0";
                        //Log.i(TAG, FollowerNum_);
                        String followingNum = "0";

                        if (user != null) {
                            followerNum = user.followerNum;
                            //Log.i(TAG, FollowerNum_);
                            followingNum = user.followingNum;
                            //Log.i(TAG, FollowingNum_);

                            //UserProfileImage = (CircleImageView) findViewById(R.id.profile_image);
                        }

                        PersonalAreaFragment.this.followerNum.setText(followerNum);
                        PersonalAreaFragment.this.followingNum.setText(followingNum);
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