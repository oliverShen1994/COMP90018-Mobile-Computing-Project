package com.android.group_12.crushy;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LocationBaseFriendingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LocationBaseFriendingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationBaseFriendingFragment extends Fragment {
    //    private OnFragmentInteractionListener mListener;
    private int fragmentHeight;
    private int fragmentWidth;
    
    public static String LIKE = "LIKE";
    public static String DISLIKE = "DISLIKE";

    public LocationBaseFriendingFragment(int fragmentHeight, int fragmentWidth) {
        this.fragmentHeight = fragmentHeight;
        this.fragmentWidth = fragmentWidth;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LocationBaseFriendingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LocationBaseFriendingFragment newInstance(int fragmentHeight, int fragmentWidth) {
        LocationBaseFriendingFragment fragment = new LocationBaseFriendingFragment(fragmentHeight, fragmentWidth);
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location_base_friending, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = this.fragmentHeight;
        view.setLayoutParams(layoutParams);
        ImageView userImageView = getView().findViewById(R.id.potential_friend_image);

        // Set the image view size, with height:width = 4:3.
        ViewGroup.LayoutParams imageViewParams = userImageView.getLayoutParams();

        // Calculate the remaining height, as the app navigation and button group height are fixed.
        LinearLayout buttonGroup = getView().findViewById(R.id.button_group);
        BottomNavigationView appNavigationBar = getView().findViewById(R.id.nav_view);

        int expectedHeight = this.fragmentWidth * 4 / 3;
        int remainingHeight = this.fragmentHeight - buttonGroup.getLayoutParams().height;

        userImageView.getLayoutParams().width = this.fragmentWidth;
        userImageView.getLayoutParams().height = remainingHeight > expectedHeight ? expectedHeight : remainingHeight;

        if (expectedHeight > this.fragmentHeight) {
            userImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }

        // Set the default image.
        userImageView.setImageResource(R.drawable.ic_poker_2);


        System.out.println("image view height = " + imageViewParams.height + ", width = " + imageViewParams.width);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
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
    
    private static void LikeDislikeFunction(DatabaseReference rootRef, final String sender, String receiver, final String Flag){
        
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


//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
