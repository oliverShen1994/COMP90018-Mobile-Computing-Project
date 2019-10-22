package com.android.group_12.crushy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

/**
 * An abstract simple {@link Fragment} class.
 */
public abstract class CrushyFragment extends Fragment {
    protected int fragmentHeight;
    protected int fragmentWidth;
    protected int fragmentID;

    public CrushyFragment(int fragmentID, int fragmentHeight, int fragmentWidth) {
        this.fragmentHeight = fragmentHeight;
        this.fragmentWidth = fragmentWidth;
        this.fragmentID = fragmentID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentLayout = inflater.inflate(this.fragmentID, container, false);
        ViewGroup.LayoutParams fragmentParams = fragmentLayout.getLayoutParams();
        fragmentParams.height = this.fragmentHeight;
        fragmentParams.width = this.fragmentWidth;
        fragmentLayout.setLayoutParams(fragmentParams);

        System.out.println("In CrushyFragment.onCreateView, fragmentHeight = " + this.fragmentHeight);
        System.out.println("In CrushyFragment.onCreateView, fragmentWidth = " + this.fragmentWidth);

        return fragmentLayout;
    }
}
