package com.android.group_12.crushy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FollowListAdapter extends RecyclerView.Adapter<FollowListAdapter.ViewHolder> {
    // an array of PersonalInfo need to display at recyclerView
    private List<PersonalInfo> personalInfos;
    // the resource id of item layout
    private int resourceId;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView text;
        ViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.follow_image);
            text = view.findViewById(R.id.follow_text);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                //fixme: change to activity skip
                Toast.makeText(view.getContext(), text.getText(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    // to initialize adapter with PersonalInfo array, and the resource id of layout
    public FollowListAdapter(List<PersonalInfo> listInfos, int resourceId) {
        this.personalInfos = listInfos;
        this.resourceId = resourceId;
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
        holder.image.setImageURI(this.personalInfos.get(position).getImageId());
        holder.text.setText(this.personalInfos.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return this.personalInfos.size();
    }

}