package com.example.userloginsqlite;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class recommendation extends AppCompatActivity {

    ImageView hom, style ,wardrobe;
    ConstraintLayout container_profile;

    private static final String TAG = "RecommendActivity";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);

        hom = findViewById(R.id.icon_home);
        style= findViewById(R.id.icon_style);
        container_profile = findViewById(R.id.container_profile);
        wardrobe = findViewById(R.id.icon_wardrobe);


        hom.setOnClickListener(view -> {
            try {
                Intent h = new Intent(recommendation.this, home_page.class);
                startActivity(h);
            } catch (Exception e) {
                Log.e(TAG, "Error while starting home_page activity", e);
            }
        });

        style.setOnClickListener( view -> {
            try {
                Intent w = new Intent(recommendation.this, style_hub.class);
                startActivity(w);
            } catch (Exception e) {
                Log.e(TAG, "Error while starting Wardrobe activity", e);
            }
        });

        container_profile.setOnClickListener(view -> {
            try {
                Intent i = new Intent(recommendation.this, user_profile.class);
                startActivity(i);
            } catch (Exception e) {
                Log.e(TAG, "Error while starting user_profile activity", e);
            }
        });

        wardrobe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent w1 = new Intent(recommendation.this, WardrobeActivity.class);
                startActivity(w1);
            }
        });

    }

}
