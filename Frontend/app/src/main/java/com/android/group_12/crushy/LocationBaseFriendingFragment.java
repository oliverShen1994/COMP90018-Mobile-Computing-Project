package com.android.group_12.crushy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;

import androidx.constraintlayout.widget.ConstraintLayout;


/**
 * A simple {@link CrushyFragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class LocationBaseFriendingFragment extends CrushyFragment {
    private ImageButton likeButton;
    private ImageButton dislikeButton;

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

        // Add event listener.
        this.likeButton = fragmentLayout.findViewById(R.id.like_button);
        this.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Like button clicked!");
//                getActivity().findViewById(R.id.dislike_button).setClickable(false);
//                getActivity().findViewById(R.id.like_button).setClickable(false);

                //Todo: send request to backend to mark user as liked.
            }
        });

        this.dislikeButton = fragmentLayout.findViewById(R.id.dislike_button);
        this.dislikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Dislike button clicked!");
//                getActivity().findViewById(R.id.dislike_button).setClickable(false);
//                getActivity().findViewById(R.id.like_button).setClickable(false);

                //Todo: send request to backend to mark user as disliked.
            }
        });

        return fragmentLayout;
    }
}
