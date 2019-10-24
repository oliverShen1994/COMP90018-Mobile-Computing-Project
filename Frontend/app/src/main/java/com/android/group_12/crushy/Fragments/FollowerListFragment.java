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

import com.android.group_12.crushy.Adapter.FollowListAdapter;
import com.android.group_12.crushy.PersonalInfo;
import com.android.group_12.crushy.R;

import java.util.ArrayList;

public class FollowerListFragment extends Fragment {
    static String LAYOUT_TYPE = "type";
    private int layout = R.layout.fragment_follow_list_gridview;
    private RecyclerView gridView;
    private RecyclerView listView;
    private SearchView mSearchView;
    private Switch mSwitch;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (this.getArguments() != null)
            this.layout = getArguments().getInt(LAYOUT_TYPE);
        View view = inflater.inflate(layout, container, false);
        initializeWidget(view);
        initializeList(view);

        return view;
    }

    public static Fragment newInstance(int layout) {
        Fragment fragment = new FollowerListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(LAYOUT_TYPE, layout);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void initializeWidget(View view) {
        mSearchView = (SearchView) view.findViewById(R.id.searchView);
        mSearchView.setSubmitButtonEnabled(true);
        mSwitch = (Switch) view.findViewById(R.id.viewSwitch);
        if(this.layout == R.layout.fragment_follow_list_listview){
            mSwitch.setChecked(false);
        }
        else if(this.layout == R.layout.fragment_follow_list_gridview){
            mSwitch.setChecked(true);
        }

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
    private void initializeList(View view) {
        //To bind GridView adapter to View
        if(this.layout == R.layout.fragment_follow_list_listview){
            //TODO: reimplement method getPersons()
            FollowListAdapter adapter = new FollowListAdapter(getPersons(), R.layout.follow_list_list_token);
            listView = view.findViewById(R.id.follow_recycler);
            listView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL,false));
            listView.setAdapter(adapter);
        }
        else if (this.layout == R.layout.fragment_follow_list_gridview) {
            //TODO: reimplement method getPersons()
            FollowListAdapter adapter = new FollowListAdapter(getPersons(), R.layout.follow_list_grid_token);
            gridView = view.findViewById(R.id.follow_recycler);
            gridView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
//            gridView.setLayoutManager(new GridLayoutManager(this.getContext(), 2, GridLayoutManager.VERTICAL, false));
            gridView.setAdapter(adapter);
        }
    }

    private void updateFragment(int selectedFragmentID) {
        Fragment fragment = FollowerListFragment.newInstance(selectedFragmentID);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.personal_info_fragment, fragment)
                .commit();
//                .addToBackStack(null)
    }

    // TODO: switch to database information
    private ArrayList<PersonalInfo> getPersons() {
        ArrayList<PersonalInfo> persons = new ArrayList<>();
        persons.add(new PersonalInfo(R.drawable.apple, "Apple"));
        persons.add(new PersonalInfo(R.drawable.bananas, "Bananas"));
        persons.add(new PersonalInfo(R.drawable.cherry, "Cherry"));
        persons.add(new PersonalInfo(R.drawable.grapes, "Grapes"));
        persons.add(new PersonalInfo(R.drawable.lemon, "Lemon"));
        persons.add(new PersonalInfo(R.drawable.orange, "Orange"));
        persons.add(new PersonalInfo(R.drawable.melon, "Melon"));
        persons.add(new PersonalInfo(R.drawable.peach, "Peach"));
        persons.add(new PersonalInfo(R.drawable.pear, "Pear"));
        persons.add(new PersonalInfo(R.drawable.pear, "Pear2"));

        return persons;
    }

}
