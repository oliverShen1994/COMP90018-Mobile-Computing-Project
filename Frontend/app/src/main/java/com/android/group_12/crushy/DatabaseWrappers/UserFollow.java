package com.android.group_12.crushy.DatabaseWrappers;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserFollow {

    public String followerNum;
    public String followingNum;
    public ArrayList<String> fansList = new ArrayList<String>();
    public ArrayList<String> likeList = new ArrayList<String>();
    public ArrayList<String> friendsList = new ArrayList<String>();
    public ArrayList<String> blockList = new ArrayList<String>();
    public ArrayList<String> dislikeList = new ArrayList<String>();

    public UserFollow(ArrayList<String> fansList, ArrayList<String> likeList, ArrayList<String> friendsList,
                ArrayList<String> blockList, ArrayList<String> dislikeList, String followerNum, String followingNum) {

        this.fansList = fansList;
        this.likeList = likeList;
        this.friendsList = friendsList;
        this.blockList = blockList;
        this.dislikeList = dislikeList;
        this.followerNum = followerNum;
        this.followingNum = followingNum;

    }

    public UserFollow() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {

        HashMap<String, Object> result = new HashMap<>();

        result.put("fansList", fansList);
        result.put("likeList", likeList);
        result.put("blockList", blockList);
        result.put("dislikeList", dislikeList);
        result.put("friendsList", friendsList);

        result.put("followerNum", followerNum);
        result.put("followingNum", followingNum);

        return result;

    }

}
