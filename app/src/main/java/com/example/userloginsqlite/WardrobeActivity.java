package com.example.userloginsqlite;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.util.Map;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WardrobeActivity extends AppCompatActivity {

    private static final String TAG = "WardrobeActivity";
    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;
    private List<Bitmap> imageList; // Change to List<Bitmap>
    private ImageView image1, image2, image3;
    private Button btnAll, btnUpper, btnLower, btnOthers;
    private ImageView male_icon, female_icon, default_icon, more_icon, icon_home;
    private dbConnect dbConnectInstance; // Instance of dbConnect class

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

        // RecyclerView setup
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Initialize image list and adapter
        imageList = new ArrayList<>();
        imageAdapter = new ImageAdapter(this, imageList);
        recyclerView.setAdapter(imageAdapter);

        // Initialize dbConnect instance
        dbConnectInstance = new dbConnect();

        // Retrieve email from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String email = sharedPreferences.getString("userEmail", null);
        Log.e("User Email: ", email != null ? email : "No email found");

        // Navigation setup
        setupNavigation();

        // Button click listeners
        btnAll.setOnClickListener(v -> loadImages("All", email));
        btnUpper.setOnClickListener(v -> loadImages("Upper", email));
        btnLower.setOnClickListener(v -> loadImages("Lower", email));
        btnOthers.setOnClickListener(v -> loadImages("Accessory", email));

        // Load all images by default
        loadImages("All", email);

        // Adjust padding for system bars
        adjustSystemBars();
    }

    private void setupNavigation() {
        ImageView hom = findViewById(R.id.icon_home);
        hom.setOnClickListener(view -> navigateTo(home_page.class));

        ImageView ward = findViewById(R.id.icon_style);
        ward.setOnClickListener(view -> navigateTo(style_hub.class));

        ConstraintLayout container_profile = findViewById(R.id.container_profile);
        container_profile.setOnClickListener(view -> navigateTo(user_profile.class));

        LinearLayout container_work = findViewById(R.id.container_work);
        container_work.setOnClickListener(view -> navigateTo(work_space.class));

        more_icon.setOnClickListener(v -> navigateTo(AddItem.class));
    }

    private void navigateTo(Class<?> targetClass) {
        try {
            Intent intent = new Intent(WardrobeActivity.this, targetClass);
            startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "Error while starting " + targetClass.getSimpleName() + " activity", e);
        }
    }

    private void adjustSystemBars() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.container_profile), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loadImages(String category, String email) {
        imageList.clear(); // Clear existing images

        Call<List<Map<String, String>>> call;
        if (category.equals("All")) {
            call = dbConnectInstance.apiService.getAllApparelsByEmail(email);
        } else {
            call = dbConnectInstance.apiService.getApparelsByTypeAndEmail(email, category.toLowerCase());
        }

        call.enqueue(new Callback<List<Map<String, String>>>() {
            @Override
            public void onResponse(Call<List<Map<String, String>>> call, Response<List<Map<String, String>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Map<String, String>> base64Images = response.body();

                    // Process the body content
                    for (Map<String, String> base64ImageFinder : base64Images) {
                        String base64Image = base64ImageFinder.get("image"); // Ensure the key is correct
                        Log.e("base64: ", base64Image);
                        if (base64Image != null) {
                            try {
                                byte[] imageBytes = Base64.decode( base64Image.substring(base64Image.indexOf(",") + 1), Base64.DEFAULT);
                                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                                imageList.add(bitmap); // Add Bitmap directly to the list
                            } catch (Exception e) {
                                Log.e(TAG, "Failed to decode image: " + e.getMessage());
                            }
                        }
                    }

                    imageAdapter.notifyDataSetChanged();
                    updateImageViewsVisibility(imageList.size());
                } else {
                    Log.e(TAG, "Response was not successful or body is null");
                }
            }

            @Override
            public void onFailure(Call<List<Map<String, String>>> call, Throwable t) {
                Log.e(TAG, "API call failed: " + t.getMessage());
            }
        });
    }

    private void updateImageViewsVisibility(int imageCount) {
        // If there are images, hide the default placeholders
        if (imageCount > 0) {
            image1.setVisibility(View.GONE);
            image2.setVisibility(View.GONE);
            image3.setVisibility(View.GONE);
        } else {
            // If no images, show default placeholders
            image1.setVisibility(View.VISIBLE);
            image2.setVisibility(View.VISIBLE);
            image3.setVisibility(View.VISIBLE);
        }
    }
}
