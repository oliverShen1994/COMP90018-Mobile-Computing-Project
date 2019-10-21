package com.android.group_12.crushy;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


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
        ScrollView userScrollView = getView().findViewById(R.id.potential_friend_view);

        // Set the image view size, with height:width = 4:3.
        ViewGroup.LayoutParams imageViewParams = userImageView.getLayoutParams();
        ViewGroup.LayoutParams scrollViewParams = userScrollView.getLayoutParams();


        // Calculate the remaining height, as the app navigation and button group height are fixed.
        LinearLayout buttonGroup = getView().findViewById(R.id.button_group);

        int expectedHeight = this.fragmentWidth * 4 / 3;
        int remainingHeight = this.fragmentHeight - buttonGroup.getLayoutParams().height;
        int scrollViewHeight = remainingHeight > expectedHeight ? expectedHeight : remainingHeight;

        imageViewParams.width = this.fragmentWidth;
        imageViewParams.height = scrollViewHeight;

        scrollViewParams.width = this.fragmentWidth;
        scrollViewParams.height = scrollViewHeight;


        if (expectedHeight > this.fragmentHeight) {
            userImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }

        // Set the default image.
        userImageView.setImageResource(R.drawable.ic_poker_2);


        System.out.println("image view height = " + imageViewParams.height + ", width = " + imageViewParams.width);
        System.out.println("scroll view height = " + scrollViewParams.height + ", width = " + scrollViewParams.width);
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
