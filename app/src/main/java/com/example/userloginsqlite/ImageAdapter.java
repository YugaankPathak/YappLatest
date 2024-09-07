package com.example.userloginsqlite;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private Context context;
    private List<byte[]> imageList;

    public ImageAdapter(Context context, List<byte[]> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        byte[] imageBlob = imageList.get(position);
        if (imageBlob != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBlob, 0, imageBlob.length);

            // No need for dynamic resizing anymore if using GridLayoutManager
            holder.imageView.setImageBitmap(bitmap);

            // Ensure the ImageView size is 300dp by 300dp
            ViewGroup.LayoutParams params = holder.imageView.getLayoutParams();
            params.width = dpToPx(200);
            params.height = dpToPx(200);
            holder.imageView.setLayoutParams(params);
        }
    }


    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view); // Make sure to use the correct ID
        }
    }

    private int dpToPx(int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}
