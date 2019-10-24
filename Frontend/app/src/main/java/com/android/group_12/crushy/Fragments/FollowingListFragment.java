package com.android.group_12.crushy.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SearchView;
import android.widget.Switch;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.group_12.crushy.Constants.DatabaseConstant;
import com.android.group_12.crushy.DatabaseWrappers.User;
import com.android.group_12.crushy.DatabaseWrappers.UserFollow;
import com.android.group_12.crushy.FollowListAdapter;
import com.android.group_12.crushy.Adapter.FollowListAdapter;
import com.android.group_12.crushy.PersonalInfo;
import com.android.group_12.crushy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FollowingListFragment extends Fragment {
    static String LAYOUT_TYPE = "type";
    private int layout = R.layout.fragment_follow_list_gridview;
    private RecyclerView gridView;
    private RecyclerView listView;
    private SearchView mSearchView;
    private Switch mSwitch;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String currentUserId;
    private ArrayList<String> followingList;
    private ArrayList<PersonalInfo> followingInfo;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mAuth = FirebaseAuth.getInstance();
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        this.currentUserId = currentUser.getUid();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (this.getArguments() != null)
            this.layout = getArguments().getInt(LAYOUT_TYPE);
        View view = inflater.inflate(this.layout, container, false);
        initializeWidget(view);
        fetchFollowingList(this.currentUserId);
        initializeList(view, this.followingList);

        return view;
    }


    public static Fragment newInstance(int layout) {
        Fragment fragment = new FollowingListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(LAYOUT_TYPE, layout);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void initializeWidget(View view) {
        this.mSearchView = (SearchView) view.findViewById(R.id.searchView);
        this.mSearchView.setSubmitButtonEnabled(true);
        this.mSwitch = (Switch) view.findViewById(R.id.viewSwitch);
        if(this.layout == R.layout.fragment_follow_list_listview){
            this.mSwitch.setChecked(false);
        }
        else if(this.layout == R.layout.fragment_follow_list_gridview){
            this.mSwitch.setChecked(true);
        }

        this.mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    updateFragment(R.layout.fragment_follow_list_gridview);
                }else {
                    updateFragment(R.layout.fragment_follow_list_listview);
                }
            }
        });

    }

    private void initializeList(View view, ArrayList<String> followList) {
        //To bind GridView adapter to View
        if(this.layout == R.layout.fragment_follow_list_listview){
            //TODO: reimplement method getPersons()
            FollowListAdapter adapter = new FollowListAdapter(this.followingInfo, R.layout.follow_list_list_token);
//            FollowListAdapter adapter = new FollowListAdapter(getPersons(), R.layout.follow_list_list_token);
            this.listView = view.findViewById(R.id.follow_recycler);
            this.listView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL,false));
            this.listView.setAdapter(adapter);
        }
        else if(this.layout == R.layout.fragment_follow_list_gridview) {
            //TODO: reimplement method getPersons()
            FollowListAdapter adapter = new FollowListAdapter(this.followingInfo, R.layout.follow_list_grid_token);
//            FollowListAdapter adapter = new FollowListAdapter(getPersons(), R.layout.follow_list_grid_token);
            this.gridView = view.findViewById(R.id.follow_recycler);
            this.gridView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
//            this.gridView.setLayoutManager(new GridLayoutManager(this.getContext(), 2, GridLayoutManager.VERTICAL, false));
            this.gridView.setAdapter(adapter);
        }
    }

    private void fetchFollowingList(String userId) {
        mDatabase.child(DatabaseConstant.USER_FOLLOW_TABLE).child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        UserFollow userFollow = dataSnapshot.getValue(UserFollow.class);
                        FollowingListFragment.this.followingList = userFollow.fansList;
                        FollowingListFragment.this.followingInfo = new ArrayList<>();
                        for(String userId: FollowingListFragment.this.followingList){
                            mDatabase.child(DatabaseConstant.USER_TABLE_NAME).child(userId).addListenerForSingleValueEvent(
                                    new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            // Get user value
                                            User user = dataSnapshot.getValue(User.class);
                                            String imageUrl = user.profileImageUrl;
                                            String name = user.name;
                                            FollowingListFragment.this.followingInfo.add(new PersonalInfo(imageUrl, name));
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    private void updateFragment(int selectedFragmentID) {
        Fragment fragment = FollowingListFragment.newInstance(selectedFragmentID);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.personal_info_fragment, fragment)
                .commit();
//                .addToBackStack(null)
    }

//    // TODO: switch to database information
//    private ArrayList<PersonalInfo> getPersons() {
//        ArrayList<PersonalInfo> persons = new ArrayList<>();
//        persons.add(new PersonalInfo(R.drawable.apple, "Apple"));
//        persons.add(new PersonalInfo(R.drawable.bananas, "Bananas"));
//        persons.add(new PersonalInfo(R.drawable.cherry, "Cherry"));
//        persons.add(new PersonalInfo(R.drawable.grapes, "Grapes"));
//        persons.add(new PersonalInfo(R.drawable.lemon, "Lemon"));
//        persons.add(new PersonalInfo(R.drawable.orange, "Orange"));
//        persons.add(new PersonalInfo(R.drawable.melon, "Melon"));
//        persons.add(new PersonalInfo(R.drawable.peach, "Peach"));
//        persons.add(new PersonalInfo(R.drawable.pear, "Pear"));
//        persons.add(new PersonalInfo(R.drawable.pear, "Pear2"));
//
//        return persons;
//    }

}
