package com.android.group_12.crushy.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.renderscript.Sampler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.group_12.crushy.Adapter.UserAdapter;
import com.android.group_12.crushy.DatabaseWrappers.Friends;
import com.android.group_12.crushy.DatabaseWrappers.UserFollow;
import com.android.group_12.crushy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ContactsFragment extends CrushyFragment {
    private RecyclerView recyclerView;
    private Toolbar toolbar;

    private UserAdapter userAdapter;
    private List<Friends> mUsers;

    private ArrayList<String> mFriends;

    public ContactsFragment(int fragmentHeight, int fragmentWidth) {
        super(R.id.fragment_contacts, fragmentHeight, fragmentWidth);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        Toolbar toolbar = view.findViewById(R.id.friend_list_toolbar);

        AppCompatActivity appCompatActivity = ((AppCompatActivity) getActivity());
        if (appCompatActivity != null) {
            appCompatActivity.setSupportActionBar(toolbar);
            appCompatActivity.getSupportActionBar().setTitle("Friend List");
        }

        recyclerView = view.findViewById(R.id.user_list);
        toolbar = view.findViewById(R.id.friend_list_toolbar);

        int toolbarHeight = toolbar.getLayoutParams().height;
        ViewGroup.LayoutParams recyclerViewLayout = recyclerView.getLayoutParams();
        recyclerViewLayout.height = this.fragmentHeight - toolbarHeight;
        recyclerView.setLayoutParams(recyclerViewLayout);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mUsers = new ArrayList<>();
        mFriends = new ArrayList<>();
        getFriends();
        System.out.println(Arrays.toString(mFriends.toArray()));
        readUsers();

        return view;
    }

    private void getFriends() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("UserFollow").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mFriends.clear();
                UserFollow user = dataSnapshot.getValue(UserFollow.class);
                mFriends = user.friendsList;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private  void readUsers() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Friends user = snapshot.getValue(Friends.class);
                    assert user != null;
                    assert firebaseUser != null;
                    if (mFriends.contains(user.getUserID())) {
                        mUsers.add(user);
                    }
//                    if (!user.getUserID().equals(firebaseUser.getUid())) {
//                        mUsers.add(user);
//                    }
                }

                userAdapter = new UserAdapter(getContext(), mUsers);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
