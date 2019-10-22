package com.android.group_12.crushy;

import android.widget.Toast;

import com.android.group_12.crushy.Constants.DatabaseConstant;
import com.android.group_12.crushy.DatabaseWrappers.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class LikeDislike {

    public static String LIKE = "0";
    public static String DISLIKE = "1";

    public static void LikeDislikeFunction(DatabaseReference rootRef, final String sender, String receiver, final String Flag){

        final ArrayList<String> senderFriendList = new ArrayList<String>();
        final ArrayList<String> senderLikeList = new ArrayList<String>();
        final ArrayList<String> senderDislikeList = new ArrayList<String>();
        final ArrayList<String> senderFansList = new ArrayList<String>();

        final ArrayList<String> receiverFriendList= new ArrayList<String>();
        final ArrayList<String> receiverFansList= new ArrayList<String>();
        final ArrayList<String> receiverLikeList= new ArrayList<String>();
        final ArrayList<String> receiverDislikeList= new ArrayList<String>();

        rootRef.child(DatabaseConstant.USER_TABLE_NAME).child(sender).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);

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
                        User user = dataSnapshot.getValue(User.class);
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
        rootRef.child(DatabaseConstant.USER_TABLE_NAME).child(sender).setValue(senderLists);

        Map<String, ArrayList<String>> receiverLists = new HashMap<>();
        receiverLists.put("fansList", receiverFansList);
        receiverLists.put("likeList", receiverLikeList);
        receiverLists.put("friendsList", receiverFriendList);
        receiverLists.put("dislikeList", receiverDislikeList);
        rootRef.child(DatabaseConstant.USER_TABLE_NAME).child(receiver).setValue(receiverLists);

    }
}
