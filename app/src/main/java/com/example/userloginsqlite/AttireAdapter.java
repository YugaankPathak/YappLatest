package com.example.userloginsqlite;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AttireAdapter extends RecyclerView.Adapter<AttireAdapter.ViewHolder> {
    private List<Attire_DateView> attireList;

    public AttireAdapter(List<Attire_DateView> attireList) {
        this.attireList = attireList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Attire_DateView attire = attireList.get(position);
        holder.dateTextView.setText(attire.getDate());
        holder.imageView.setImageURI(attire.getImageUri());
    }

    @Override
    public int getItemCount() {
        return attireList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.item_date); // Change ID as per your layout
            imageView = itemView.findViewById(R.id.item_image); // Change ID as per your layout
        }
    }
    public void addItems(List<Attire_DateView> newItems) {
        attireList.addAll(newItems);
        notifyDataSetChanged();
    }
}
