package com.example.userloginsqlite;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {

    private List<Item> items;

    public FeedAdapter(List<Item> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_feed_layout, parent, false);
        return new FeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder holder, int position) {

        Item currentItem = items.get(position);
        final boolean[] isLiked = {false};
        // Bind title
        TextView titleView = holder.itemView.findViewById(R.id.item_user);
        titleView.setText(currentItem.getTitle());

        // Bind image
        ImageView imageView = holder.itemView.findViewById(R.id.item_image);
        imageView.setImageResource(currentItem.getImageResourceId());

        // Bind icon (like button)
        ImageView likeIconView = holder.itemView.findViewById(R.id.likes);
        likeIconView.setImageResource(currentItem.getIconResourceId());

        TextView count_likes = holder.itemView.findViewById(R.id.countlikes);
        count_likes.setText(Integer.toString(currentItem.getLikes()));

        // Manage the color change for the like button
        holder.likes.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                isLiked[0] = !isLiked[0];

                if (isLiked[0]) {
                    holder.likes.setColorFilter(Color.RED);
                    currentItem.incLikes();// Change color on click
                } else {
                    holder.likes.setColorFilter(Color.BLACK);
                    currentItem.decLikes();// Revert to default color
                }
                count_likes.setText(Integer.toString(currentItem.getLikes()));
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {

            int countClicks = 0;
            @Override
            public void onClick(View v) {
                countClicks++;
                if(countClicks%2==0&&countClicks>0){
                    holder.likes.setColorFilter(Color.RED);
                    isLiked[0]=true;
                    currentItem.incLikes();// Change color on click
                }
                count_likes.setText(Integer.toString(currentItem.getLikes()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItems(List<Item> newItems) {
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    public static class FeedViewHolder extends RecyclerView.ViewHolder {
        ImageView likes;
        public FeedViewHolder(View itemView) {
            super(itemView);
            likes = itemView.findViewById(R.id.likes);
        }
    }
}


