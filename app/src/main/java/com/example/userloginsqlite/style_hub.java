package com.example.userloginsqlite;

import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class style_hub extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FeedAdapter feedAdapter;
    private LinearLayoutManager linearLayoutManager;
    private boolean isLoading = false;
    private final int visibleThreshold = 5;
    private List<Item> items = new ArrayList<>();

    private static final String TAG = "style_hub";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_style_hub);

        ImageView male_icon, female_icon, default_icon,icon_wardrobe;
        dbConnect db;
        ConstraintLayout container_profile;
        LinearLayout container_home, container_work, container_style;

        male_icon = findViewById(R.id.male_icon);
        female_icon = findViewById(R.id.female_icon);
        default_icon = findViewById(R.id.default_icon);
        container_profile = findViewById(R.id.container_profile);
        container_home = findViewById(R.id.container_home);
        icon_wardrobe = findViewById(R.id.icon_wardrobe);
        container_work = findViewById(R.id.container_work);
        container_style = findViewById(R.id.container_style);


        // Initialize dbConnect instance
        db = new dbConnect();

        // Set default visibility
        male_icon.setVisibility(View.GONE);
        female_icon.setVisibility(View.GONE);
        default_icon.setVisibility(View.VISIBLE);

        // Retrieve email from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", null);

        if (email != null) {
            try {
                // Fetch gender from database
                String gender = db.getGenderByEmail(email);

                // Set visibility based on gender
                if (gender != null) {
                    switch (gender.toLowerCase()) {
                        case "male":
                            male_icon.setVisibility(View.VISIBLE);
                            female_icon.setVisibility(View.GONE);
                            default_icon.setVisibility(View.GONE);
                            break;
                        case "female":
                            male_icon.setVisibility(View.GONE);
                            female_icon.setVisibility(View.VISIBLE);
                            default_icon.setVisibility(View.GONE);
                            break;
                        default:
                            male_icon.setVisibility(View.GONE);
                            female_icon.setVisibility(View.GONE);
                            default_icon.setVisibility(View.VISIBLE);
                            break;
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Error fetching gender from database", e);
            }
        } else {
            Log.d(TAG, "No email found in SharedPreferences");
        }

        container_profile.setOnClickListener(view -> {
            try {
                Intent i = new Intent(style_hub.this, user_profile.class);
                startActivity(i);
            } catch (Exception e) {
                Log.e(TAG, "Error while starting user_profile activity", e);
            }
        });
        icon_wardrobe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(style_hub.this, WardrobeActivity.class);
                startActivity(i);
            }
        });

        ImageView hom = findViewById(R.id.icon_home);
        hom.setOnClickListener(view -> {
            try {
                Intent h = new Intent(style_hub.this, home_page.class);
                startActivity(h);
            } catch (Exception e) {
                Log.e(TAG, "Error while starting home_page activity", e);
            }
        });

        container_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent w1 = new Intent(style_hub.this, work_space.class);
                startActivity(w1);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Yugaank
        //will req DB commands when we Integrate, rest all should work fine
        items.add(new Item("Yugaank", R.drawable.ic_image1, R.drawable.ic_like,0));
        items.add(new Item("Vikas", R.drawable.ic_image2, R.drawable.ic_like,0));
        items.add(new Item("Amar", R.drawable.ic_image3, R.drawable.ic_like,0));
        items.add(new Item("Yugaank", R.drawable.ic_image1, R.drawable.ic_like,0));
        items.add(new Item("Vikas", R.drawable.ic_image2, R.drawable.ic_like,0));
        items.add(new Item("Amar", R.drawable.ic_image3, R.drawable.ic_like,0));


        recyclerView = findViewById(R.id.recyclerView);
        feedAdapter = new FeedAdapter(items);
        linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(feedAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = recyclerView.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

                if (!isLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount - visibleThreshold) {
                    isLoading = true;
                    loadMoreData();
                }
            }
        });

        // Initial data load
        loadMoreData();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        FeedAdapter adapter = new FeedAdapter(items);
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter.notifyDataSetChanged();

    }

    private void loadMoreData() {
        // Simulate network or database call
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            List<Item> newItems = fetchData();
            feedAdapter.addItems(newItems);
            isLoading = false;
        }, 2000);
    }

    private List<Item> fetchData() {
        // Generate or fetch new items
        return new ArrayList<>(); // Replace with actual data
    }
}
