package com.example.userloginsqlite;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class work_space extends AppCompatActivity {
    ImageView clothes,hom, style ,wardrobe;
    ConstraintLayout container_profile;
    ImageView male_icon, female_icon, default_icon;

    private static final String TAG = "WorkActivity";

    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work);


        clothes = findViewById(R.id.clothes_icon);
        hom = findViewById(R.id.icon_home);
        style= findViewById(R.id.icon_style);
        container_profile = findViewById(R.id.container_profile);
        wardrobe = findViewById(R.id.icon_wardrobe);

        clothes.setOnClickListener(view -> {
            Intent i = new Intent(work_space.this, combinations.class);
            startActivity(i);
        });


        // Initialize dbConnect instance
        dbConnect db = new dbConnect(this);
        male_icon = findViewById(R.id.male_icon);
        female_icon = findViewById(R.id.female_icon);
        default_icon = findViewById(R.id.default_icon);
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


        hom.setOnClickListener(view -> {
            try {
                Intent h = new Intent(work_space.this, home_page.class);
                startActivity(h);
            } catch (Exception e) {
                Log.e(TAG, "Error while starting home_page activity", e);
            }
        });

        style.setOnClickListener( view -> {
            try {
                Intent w = new Intent(work_space.this, style_hub.class);
                startActivity(w);
            } catch (Exception e) {
                Log.e(TAG, "Error while starting Wardrobe activity", e);
            }
        });

        container_profile.setOnClickListener(view -> {
            try {
                Intent i = new Intent(work_space.this, user_profile.class);
                startActivity(i);
            } catch (Exception e) {
                Log.e(TAG, "Error while starting user_profile activity", e);
            }
        });

        wardrobe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent w1 = new Intent(work_space.this, WardrobeActivity.class);
                startActivity(w1);
            }
        });

    }
}
