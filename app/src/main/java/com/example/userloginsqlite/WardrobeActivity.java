package com.example.userloginsqlite;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WardrobeActivity extends AppCompatActivity {

    private static final String TAG = "WardrobeActivity";
    RecyclerView recyclerView;
    ImageAdapter imageAdapter;
    List<byte[]> imageList;
    ImageView image1, image2, image3;
    Button btnAll, btnUpper, btnLower, btnOthers;
    ImageView male_icon, female_icon, default_icon, more_icon, icon_home;
    dbConnect.ApiService apiService; // Retrofit API service

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.dmm);

        // Initialize UI elements
        male_icon = findViewById(R.id.male_icon);
        female_icon = findViewById(R.id.female_icon);
        default_icon = findViewById(R.id.default_icon);
        more_icon = findViewById(R.id.more_icon);
        icon_home = findViewById(R.id.icon_home);
        btnAll = findViewById(R.id.btnallapparels);
        btnUpper = findViewById(R.id.btnupperapparels);
        btnLower = findViewById(R.id.btnlowerapparels);
        btnOthers = findViewById(R.id.btnothers);
        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        image3 = findViewById(R.id.image3);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        imageList = new ArrayList<>();
        imageAdapter = new ImageAdapter(this, imageList);
        recyclerView.setAdapter(imageAdapter);

        apiService = dbConnect.getClient().create(dbConnect.ApiService.class); // Initialize API service

        // Retrieve email from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", null);

        ImageView hom = findViewById(R.id.icon_home);
        hom.setOnClickListener(view -> {
            try {
                Intent h = new Intent(WardrobeActivity.this, home_page.class);
                startActivity(h);
            } catch (Exception e) {
                Log.e(TAG, "Error while starting home_page activity", e);
            }
        });

        ImageView ward = findViewById(R.id.icon_style);
        ward.setOnClickListener(view -> {
            try {
                Intent w = new Intent(WardrobeActivity.this, style_hub.class);
                startActivity(w);
            } catch (Exception e) {
                Log.e(TAG, "Error while starting Wardrobe activity", e);
            }
        });

        ConstraintLayout container_profile = findViewById(R.id.container_profile);
        container_profile.setOnClickListener(view -> {
            try {
                Intent u = new Intent(WardrobeActivity.this, user_profile.class);
                startActivity(u);
            } catch (Exception e) {
                Log.e(TAG, "Error while starting user_profile activity", e);
            }
        });

        LinearLayout container_work = findViewById(R.id.container_work);
        container_work.setOnClickListener(view -> {
            Intent w1 = new Intent(WardrobeActivity.this, work_space.class);
            startActivity(w1);
        });

        more_icon.setOnClickListener(v -> {
            Intent intent = new Intent(WardrobeActivity.this, AddItem.class);
            startActivity(intent);
        });

      //  btnAll.setOnClickListener(v -> loadImages("All", email));
       // btnUpper.setOnClickListener(v -> loadImages("Upper", email));
     //   btnLower.setOnClickListener(v -> loadImages("Lower", email));
      //  btnOthers.setOnClickListener(v -> loadImages("Accessory", email));

        // Load all images by default
       // loadImages("All", email);

        // Adjust padding for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.container_profile), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loadImages(String category, String email) {
        imageList.clear(); // Clear existing images

        Call<List<byte[]>> call;
        if (category.equals("All")) {
            call = apiService.getAllApparelsByEmail(email);
        } else {
            call = apiService.getApparelsByTypeAndEmail(email, category);
        }

        call.enqueue(new Callback<List<byte[]>>() {
            @Override
            public void onResponse(Call<List<byte[]>> call, Response<List<byte[]>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<byte[]> base64Images = response.body();

                    // Decode each Base64 string to byte[]
                    for (byte[] base64Image : base64Images) {
                        byte[] imageBytes = Base64.decode(base64Image, Base64.DEFAULT);
                        imageList.add(imageBytes); // Add to your image list
                    }

                    imageAdapter.notifyDataSetChanged();
                    updateImageViewsVisibility(imageList.size());
                }
            }

            @Override
            public void onFailure(Call<List<byte[]>> call, Throwable t) {
                Log.e(TAG, "Error fetching images", t);
            }
        });
    }

    private void updateImageViewsVisibility(int imageCount) {
        if (imageCount > 0) {
            // If there are images in the RecyclerView, hide the default images
            image1.setVisibility(View.GONE);
            image2.setVisibility(View.GONE);
            image3.setVisibility(View.GONE);
        } else {
            // If there are no images, show the default images
            image1.setVisibility(View.VISIBLE);
            image2.setVisibility(View.VISIBLE);
            image3.setVisibility(View.VISIBLE);
        }
    }
}
