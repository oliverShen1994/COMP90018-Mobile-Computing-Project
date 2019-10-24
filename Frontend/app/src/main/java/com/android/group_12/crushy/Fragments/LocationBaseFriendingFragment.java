package com.android.group_12.crushy.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.group_12.crushy.Activities.MainActivity;
import com.android.group_12.crushy.Constants.DatabaseConstant;
import com.android.group_12.crushy.DatabaseWrappers.User;
import com.android.group_12.crushy.DatabaseWrappers.UserFollow;
import com.android.group_12.crushy.EditUserProfile;
import com.android.group_12.crushy.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


/**
 * A simple {@link CrushyFragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class LocationBaseFriendingFragment extends CrushyFragment {
    private ImageButton likeButton;
    private ImageButton dislikeButton;

    public static String LIKE = "0";
    public static String DISLIKE = "1";
    public ImageView userImage;
    public TextView userName;
    public TextView gender;
    public TextView city;
    public TextView hobby;
    public ImageButton like;
    public ImageButton dislike;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String currentUserId;
    public String nextUserId;
    private String TAG = "LocationBaseFriendingFragment";
    private ArrayList<String> userIDs = new ArrayList<>();
    private ArrayList<String> disLikeList_ = new ArrayList<>();

    public LocationBaseFriendingFragment(int fragmentHeight, int fragmentWidth) {
        super(R.layout.fragment_location_base_friending, fragmentHeight, fragmentWidth);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment by calling parent's method.
        View fragmentLayout = super.onCreateView(inflater, container, savedInstanceState);

        userImage = fragmentLayout.findViewById(R.id.potential_friend_image);
        userName = fragmentLayout.findViewById(R.id.nick_name);
        gender=fragmentLayout.findViewById(R.id.gender);
        city= fragmentLayout.findViewById(R.id.city);
        hobby= fragmentLayout.findViewById(R.id.hobbies);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        currentUserId = currentUser.getUid();


        // Adjust the image view.
        ImageView userImageView = fragmentLayout.findViewById(R.id.potential_friend_image);
        ViewGroup.MarginLayoutParams imageViewParams = (ViewGroup.MarginLayoutParams) userImageView.getLayoutParams();

        int expectedHeight = this.fragmentWidth * 4 / 3;
        int remainingHeight = (int) Math.round(this.fragmentHeight * 0.8);
        int scrollViewHeight = remainingHeight > expectedHeight ? remainingHeight : expectedHeight;

        imageViewParams.width = this.fragmentWidth;
        imageViewParams.height = scrollViewHeight;
        imageViewParams.setMargins(0, 0, 0, 0);
        userImageView.setLayoutParams(imageViewParams);

        // Calculate the remaining height, as the app navigation and button group height are fixed.
        ConstraintLayout buttonGroup = fragmentLayout.findViewById(R.id.button_group);
        ViewGroup.LayoutParams buttonGroupParam = buttonGroup.getLayoutParams();
        buttonGroupParam.width = this.fragmentWidth;
        buttonGroupParam.height = (int) Math.round(this.fragmentHeight * 0.1);
        buttonGroup.setLayoutParams(buttonGroupParam);

        System.out.println("Button group height = " + buttonGroup.getLayoutParams().height);

        // Set the image view size, with height:width = 4:3.
        ScrollView userScrollView = fragmentLayout.findViewById(R.id.potential_friend_view);
        ViewGroup.MarginLayoutParams scrollViewParams = (ViewGroup.MarginLayoutParams) userScrollView.getLayoutParams();

        scrollViewParams.width = this.fragmentWidth;
        scrollViewParams.height = scrollViewHeight;
        scrollViewParams.setMargins(0, 0, 0, 0);
        userScrollView.setLayoutParams(scrollViewParams);

        // Set the default image.
        userImageView.setImageResource(R.drawable.sample_portrait_photo);
        if (expectedHeight > this.fragmentHeight) {
            userImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            userImageView.setAdjustViewBounds(true);
        }

        System.out.println("image view height = " + imageViewParams.height + ", width = " + imageViewParams.width);
        System.out.println("scroll view height = " + scrollViewParams.height + ", width = " + scrollViewParams.width);

        // Add event listener.
        this.likeButton = fragmentLayout.findViewById(R.id.like_button);
        this.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo: send request to backend to mark user as liked.
                System.out.println("Like button clicked!");
                LikeDislikeFunction(mDatabase, currentUserId, nextUserId, LIKE);
                pickNextUser();
            }
        });

        this.dislikeButton = fragmentLayout.findViewById(R.id.dislike_button);
        this.dislikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo: send request to backend to mark user as disliked.
                System.out.println("Dislike button clicked!");
                LikeDislikeFunction(mDatabase, currentUserId, nextUserId, DISLIKE);
                pickNextUser();
            }
        });
        retriveUserIDs();
        //pickNextUser();

        return fragmentLayout;
    }

    //Like or Dislike
    public static void LikeDislikeFunction(DatabaseReference rootRef, final String sender, String receiver, final String Flag){

        final ArrayList<String> senderFriendList = new ArrayList<String>();
        final ArrayList<String> senderLikeList = new ArrayList<String>();
        final ArrayList<String> senderDislikeList = new ArrayList<String>();
        final ArrayList<String> senderFansList = new ArrayList<String>();

        final ArrayList<String> receiverFriendList= new ArrayList<String>();
        final ArrayList<String> receiverFansList= new ArrayList<String>();
        final ArrayList<String> receiverLikeList= new ArrayList<String>();
        final ArrayList<String> receiverDislikeList= new ArrayList<String>();

        rootRef.child(DatabaseConstant.USER_FOLLOW_TABLE).child(sender).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        UserFollow user = dataSnapshot.getValue(UserFollow.class);

                        for(String liked : user.likeList){
                            senderLikeList.add(liked);
                        }
                        for(String friend : user.friendsList){
                            senderFriendList.add(friend);
                        }
                        for(String fans : user.fansList){
                            senderFansList.add(fans);
                        }
                        for(String disLike : user.dislikeList){
                            senderDislikeList.add(disLike);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        rootRef.child(DatabaseConstant.USER_TABLE_NAME).child(receiver).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        UserFollow user = dataSnapshot.getValue(UserFollow.class);
                        for(String liked : user.likeList){
                            receiverLikeList.add(liked);
                        }
                        for(String friend : user.friendsList){
                            receiverFriendList.add(friend);
                        }
                        for(String fans : user.fansList){
                            receiverFansList.add(fans);
                        }
                        for(String disLike : user.dislikeList){
                            receiverDislikeList.add(disLike);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        // if I like her, add her to my likeList, add me to her fansList, if her likeList has me, add me
        // to her friendList, add her to my friendList
        if(Flag == LIKE){

            senderLikeList.add(receiver);
            receiverFansList.add(sender);

            // if her likeList has me,
            // congratulation! we matched !!!
            if(receiverLikeList.contains(sender)){
                senderFriendList.add(receiver);
                receiverFriendList.add(sender);
            }

        }

        if(Flag == DISLIKE){
            senderDislikeList.add(receiver);
        }

        //update the firebase with the new values
        Map<String, ArrayList<String>> senderLists = new HashMap<>();
        senderLists.put("fansList", senderFansList);
        senderLists.put("likeList", senderLikeList);
        senderLists.put("friendsList", senderFriendList);
        senderLists.put("dislikeList", senderDislikeList);
        rootRef.child(DatabaseConstant.USER_FOLLOW_TABLE).child(sender).setValue(senderLists);

        Map<String, ArrayList<String>> receiverLists = new HashMap<>();
        receiverLists.put("fansList", receiverFansList);
        receiverLists.put("likeList", receiverLikeList);
        receiverLists.put("friendsList", receiverFriendList);
        receiverLists.put("dislikeList", receiverDislikeList);
        rootRef.child(DatabaseConstant.USER_FOLLOW_TABLE).child(receiver).setValue(receiverLists);

    }


    private void retriveUserIDs() {

        mDatabase.child(DatabaseConstant.USER_TABLE_NAME).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (final DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            User user = dataSnapshot1.getValue(User.class);
                            userIDs.add(user.userID);
                            //Toast.makeText(getActivity(), userIDs.toString(), Toast.LENGTH_SHORT).show();
                            pickNextUser();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });

        mDatabase.child(DatabaseConstant.USER_TABLE_NAME).child(currentUserId).addListenerForSingleValueEvent(
                new ValueEventListener(){
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UserFollow userFollow = dataSnapshot.getValue(UserFollow.class);
                        for(String disliked : userFollow.dislikeList){
                            disLikeList_.add(disliked);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );
    }

    private void pickNextUser(){
        Integer length =  userIDs.size();
        Random r = new Random();
        Integer userIndex = r.nextInt(length);
        String nextUserId_ = userIDs.get(userIndex);
        if(!disLikeList_.contains(nextUserId_)){
            retrieveUser(userIDs.get(userIndex));
        }
        else{
            pickNextUser();
        }
    }

    private void retrieveUser(String userId){
        mDatabase.child(DatabaseConstant.USER_TABLE_NAME).child(userId).addListenerForSingleValueEvent(
                new ValueEventListener(){
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        final User user = dataSnapshot.getValue(User.class);
                        if(!user.profileImageUrl.equals("")){
                            Glide.with(getActivity())
                                    .load(user.profileImageUrl)
                                    .into(userImage);
                        }
                        userName.setText(user.name);
                        gender.setText(user.gender);
                        city.setText(user.city);
                        hobby.setText(user.hobbies);
                        //update the next user ID
                        nextUserId = user.userID;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );
    }
}
