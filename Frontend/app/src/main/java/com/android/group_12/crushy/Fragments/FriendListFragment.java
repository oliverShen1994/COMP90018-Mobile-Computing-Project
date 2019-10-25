package com.android.group_12.crushy.Fragments;

import android.os.Bundle;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.android.group_12.crushy.DatabaseWrappers.Friends;
import com.android.group_12.crushy.R;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class FriendListFragment extends CrushyFragment {
    ViewPager viewPager;
    AppBarLayout appBarLayout;

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    public FriendListFragment(int fragmentHeight, int fragmentWidth) {
        super(R.layout.fragment_friend_list, fragmentHeight, fragmentWidth);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friend_list, container, false);

        Toolbar toolbar = view.findViewById(R.id.friend_list_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        appBarLayout = view.findViewById(R.id.friend_list_app_bar_layout);
        ViewGroup.LayoutParams appBarLayoutParam = appBarLayout.getLayoutParams();
        System.out.println("appBarLayoutParam.height");
        System.out.println(appBarLayoutParam.height);

        int tabHeight = view.findViewById(R.id.tab_layout).getLayoutParams().height;
        System.out.println("tabHeight: " + tabHeight);

        viewPager = view.findViewById(R.id.view_pager);
        ViewGroup.LayoutParams recyclerViewLayout = viewPager.getLayoutParams();
        recyclerViewLayout.height = this.fragmentHeight - appBarLayout.getLayoutParams().height;

        viewPager.setLayoutParams(recyclerViewLayout);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Friends user = dataSnapshot.getValue(Friends.class);
//                username.setText(user.getName());
                String userProfileImageUrl = user.getProfileImageUrl();
                if (userProfileImageUrl == null || userProfileImageUrl.equals("") || userProfileImageUrl.equals("N/A")) {
//                    profile_image.setImageResource(R.mipmap.ic_launcher);
                } else {
//                    Glide.with(FriendListFragment.this).load(user.getProfileImageUrl()).into(profile_image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        ViewPager viewPager = view.findViewById(R.id.view_pager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragment(ChatsFragment.getInstance(), "Chats");
        viewPagerAdapter.addFragment(new ContactsFragment(this.fragmentHeight, this.fragmentWidth), "Friends");
        viewPager.setAdapter(viewPagerAdapter);
        System.out.println("add adapter");
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            titles.add(title);
        }


        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

}
