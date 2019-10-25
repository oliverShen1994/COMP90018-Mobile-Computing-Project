package com.android.group_12.crushy.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.group_12.crushy.DatabaseWrappers.Chat;
import com.android.group_12.crushy.MessageActivity;
import com.android.group_12.crushy.OtherProfilePageActivity;
import com.android.group_12.crushy.R;
import com.bumptech.glide.Glide;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import static com.android.group_12.crushy.Constants.IntentExtraParameterName.UNIFORM_EXTRA_INFO_ACTIVITY_USER_ID;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;



    private Context mContext;
    private List<Chat> mChat;
    private String imageurl;
    private String userID;



    FirebaseUser fuser;

    public MessageAdapter(Context mContext, List<Chat> mChat, String imageurl, String userID) {
        this.mChat = mChat;
        this.mContext = mContext;
        this.imageurl = imageurl;
        this.userID = userID;
        System.out.println(userID);
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);

            return new MessageAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);

            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        Chat chat = mChat.get(position);
        holder.show_message.setText(chat.getMessage());
        if (imageurl.equals("")) {
            holder.profileImage.setImageResource(R.drawable.profile_image);
        } else {
            System.out.println("imageurl is :" + imageurl);
            Glide.with(mContext).load(imageurl).into(holder.profileImage);
        }
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView show_message;
        public ImageView profileImage;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            show_message = itemView.findViewById(R.id.show_message);
            profileImage = itemView.findViewById(R.id.profile_image_chat);
            profileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, OtherProfilePageActivity.class);
                    intent.putExtra(UNIFORM_EXTRA_INFO_ACTIVITY_USER_ID, userID);
                    mContext.startActivity(intent);
                }
            });
        }
    }

    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(fuser.getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}
