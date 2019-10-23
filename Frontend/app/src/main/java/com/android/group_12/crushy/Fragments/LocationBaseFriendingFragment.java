package com.android.group_12.crushy.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.group_12.crushy.R;


/**
 * A simple {@link CrushyFragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class LocationBaseFriendingFragment extends CrushyFragment {
    public LocationBaseFriendingFragment(int fragmentHeight, int fragmentWidth) {
        super(R.layout.fragment_location_base_friending, fragmentHeight, fragmentWidth);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment by calling parent's method.
        View fragmentLayout = super.onCreateView(inflater, container, savedInstanceState);

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

        return fragmentLayout;
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
