package com.android.group_12.crushy.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.group_12.crushy.DatabaseWrappers.Friends;
import com.android.group_12.crushy.Activities.MessageActivity;
import com.bumptech.glide.Glide;
import com.android.group_12.crushy.R;


import java.util.Comparator;
import java.util.List;

import static com.android.group_12.crushy.Constants.IntentExtraParameterName.UNIFORM_EXTRA_INFO_ACTIVITY_USER_ID;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context mContext;
    private List<Friends> mUsers;

    public UserAdapter(Context mContext, List<Friends> mUsers) {
        this.mUsers = mUsers;
        this.mContext = mContext;
        mUsers.sort(new Comparator<Friends>() {
            @Override
            public int compare(Friends friends, Friends t1) {
                return friends.getName().compareTo(t1.getName());
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Friends user = mUsers.get(position);
        holder.username.setText(user.getName());
        String profileUrl = user.getProfileImageUrl();

        if (profileUrl == null || profileUrl.equals("") || profileUrl.equals("N/A")) {
            holder.profileImage.setImageResource(R.drawable.profile_image);
        } else {
            Glide.with(mContext).load(user.getProfileImageUrl()).into(holder.profileImage);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra(UNIFORM_EXTRA_INFO_ACTIVITY_USER_ID, user.getUserID());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView username;
        public ImageView profileImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.user_username);
            profileImage = itemView.findViewById(R.id.user_profile_image);
        }
    }
}
