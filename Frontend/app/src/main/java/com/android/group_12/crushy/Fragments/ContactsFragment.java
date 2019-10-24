package com.android.group_12.crushy.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.group_12.crushy.Adapter.UserAdapter;
import com.android.group_12.crushy.DatabaseWrappers.Friends;
import com.android.group_12.crushy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ContactsFragment extends CrushyFragment {
    private RecyclerView recyclerView;
    private Toolbar toolbar;

    private UserAdapter userAdapter;
    private List<Friends> mUsers;

    public ContactsFragment(int fragmentHeight, int fragmentWidth) {
        super(R.id.fragment_contacts, fragmentHeight, fragmentWidth);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_contacts, container, false);
        Toolbar toolbar = view.findViewById(R.id.friend_list_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Friend List");

        recyclerView = view.findViewById(R.id.user_list);
        toolbar = view.findViewById(R.id.friend_list_toolbar);

        int toolbarHeight = toolbar.getLayoutParams().height;
        ViewGroup.LayoutParams recyclerViewLayout =  recyclerView.getLayoutParams();
        recyclerViewLayout.height = this.fragmentHeight - toolbarHeight;
        recyclerView.setLayoutParams(recyclerViewLayout);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mUsers = new ArrayList<>();
        readUsers();
        
        return view;
    }

    private  void readUsers() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Friends user = snapshot.getValue(Friends.class);
                    assert user != null;
                    assert firebaseUser != null;
                    if (!user.getUserID().equals(firebaseUser.getUid())) {
                        mUsers.add(user);
                    }
                }

                userAdapter = new UserAdapter(getContext(), mUsers);
                recyclerView.setAdapter(userAdapter);
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
