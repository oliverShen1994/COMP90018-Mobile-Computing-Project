package com.android.group_12.crushy.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.group_12.crushy.DatabaseWrappers.Friends;
import com.android.group_12.crushy.DatabaseWrappers.User;
import com.android.group_12.crushy.EditUserImage;
import com.android.group_12.crushy.EditUserProfile;
import com.android.group_12.crushy.MessageActivity;
import com.android.group_12.crushy.OtherProfilePageActivity;
import com.android.group_12.crushy.PersonalInfo;
import com.android.group_12.crushy.R;
import com.bumptech.glide.Glide;

import java.util.List;

import static com.android.group_12.crushy.Constants.IntentExtraParameterName.UNIFORM_EXTRA_INFO_ACTIVITY_USER_ID;

public class FollowListAdapter extends RecyclerView.Adapter<FollowListAdapter.ViewHolder> {
    private Context context;
    // an array of PersonalInfo need to display at recyclerView
    private List<User> personalInfos;
    // the resource id of item layout
    private int resourceId;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView text;
        ViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.follow_image);
            text = view.findViewById(R.id.follow_text);
        }
    }

    // to initialize adapter with PersonalInfo array, and the resource id of layout
    public FollowListAdapter(List<User> listInfos, int resourceId, Context mContext) {
        this.personalInfos = listInfos;
        this.resourceId = resourceId;
        this.context = mContext;
    }

    @NonNull
    @Override
    //  Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent an item.
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(resourceId, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    // to bind the resources to viewHolder, including PersonalInfo image resource id and PersonalInfo name
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final User personalInfo = this.personalInfos.get(position);
        if (personalInfo.profileImageUrl.equals("")) {
            holder.image.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(this.context).load(personalInfo.profileImageUrl).into(holder.image);
        }
        holder.text.setText(personalInfo.name);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OtherProfilePageActivity.class);
                intent.putExtra(UNIFORM_EXTRA_INFO_ACTIVITY_USER_ID, personalInfo.userID);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.personalInfos.size();
    }

}