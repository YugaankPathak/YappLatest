package com.example.userloginsqlite;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.ArrayAdapter;

import java.util.List;
import java.util.Map;

public class recommendation extends AppCompatActivity {

    private ImageView hom, style, wardrobe, imageUpper, imageLower;
    private Button getRecommendationButton, allCombinationsButton;
    private Spinner occasionSpinner;

    private static final String TAG = "RecommendActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);

        // Spinner setup
        occasionSpinner = findViewById(R.id.spinnerOccasion);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.occasion_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        occasionSpinner.setAdapter(adapter);

        // Button and image view initialization
        hom = findViewById(R.id.icon_home);
        style = findViewById(R.id.icon_style);
        wardrobe = findViewById(R.id.icon_wardrobe);
        getRecommendationButton = findViewById(R.id.button_get_recommendation);
        allCombinationsButton = findViewById(R.id.text1); // This is the "All Combinations" button
        imageUpper = findViewById(R.id.image_upper);
        imageLower = findViewById(R.id.image_lower);

        // Set click listeners for navigation
        hom.setOnClickListener(view -> startActivity(new Intent(recommendation.this, home_page.class)));
        style.setOnClickListener(view -> startActivity(new Intent(recommendation.this, style_hub.class)));
        wardrobe.setOnClickListener(view -> startActivity(new Intent(recommendation.this, WardrobeActivity.class)));

        // Fetch single recommendation
        getRecommendationButton.setOnClickListener(view -> fetchRecommendations());

        // Fetch all combinations
        allCombinationsButton.setOnClickListener(view -> fetchAllCombinations());
    }

    // Method to fetch individual recommendations
    private void fetchRecommendations() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userID = sharedPreferences.getString("userID", null);
        Log.e(TAG, userID != null ? userID : "userID is null");

        if (userID == null) {
            Toast.makeText(this, "User ID is not available", Toast.LENGTH_SHORT).show();
            return;
        }

        String selectedOccasion = occasionSpinner.getSelectedItem().toString();
        dbConnect dbConnect = new dbConnect();

        dbConnect.recommendOutfit(userID, selectedOccasion, new dbConnect.OutfitCallback<Map<String, String>>() {
            @Override
            public void onSuccess(Map<String, String> result) {
                displayRecommendedOutfit(result);
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(recommendation.this, "Failed to get recommendations: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error fetching recommendations", t);
            }
        });
    }

    // Method to fetch all combinations
    private void fetchAllCombinations() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userID = sharedPreferences.getString("userID", null);
        Log.e(TAG, userID != null ? userID : "userID is null");

        if (userID == null) {
            Toast.makeText(this, "User ID is not available", Toast.LENGTH_SHORT).show();
            return;
        }

        dbConnect dbConnect = new dbConnect();

        dbConnect.get_all_combinations(userID, new dbConnect.OutfitCallback <List<Map<String, String>>>() {

            public void onSuccess(List<Map<String, String>> result) {
                displayAllCombinations(result);
            }



            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(recommendation.this, "Failed to fetch all combinations: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error fetching all combinations", t);
            }
        });
    }

    // Method to display individual outfit recommendation
    private void displayRecommendedOutfit(Map<String, String> outfit) {
        String upperImageBase64 = outfit.get("upper_image");
        String lowerImageBase64 = outfit.get("lower_image");

        Log.e(TAG, upperImageBase64 != null ? "Upper image fetched" : "No upper image found");
        Log.e(TAG, lowerImageBase64 != null ? "Lower image fetched" : "No lower image found");

        displayImage(imageUpper, upperImageBase64);
        displayImage(imageLower, lowerImageBase64);
    }

    // Method to display all combinations of outfits
    private void displayAllCombinations(List<Map<String, String>> combinations) {
        // Assuming the result contains multiple combinations (e.g., "upper_image1", "lower_image1", etc.)
        for(Map<String,String> com:combinations){
        for (Map.Entry<String, String> entry : com.entrySet()) {
            String key = entry.getKey();
            String imageBase64 = entry.getValue();

            Log.e(TAG, key + " image fetched");
            if (key.contains("image")) {
                displayImage(imageUpper, imageBase64);
            }
        }
    }}

    // Helper method to decode and display images
    private void displayImage(ImageView imageView, String imageBase64) {
        if (imageBase64 != null && !imageBase64.isEmpty()) {
            if (imageBase64.startsWith("data:image")) {
                imageBase64 = imageBase64.substring(imageBase64.indexOf(",") + 1);
            }
            try {
                byte[] decodedString = Base64.decode(imageBase64, Base64.DEFAULT);
                imageView.setImageBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "Error decoding image: " + e.getMessage());
                Toast.makeText(this, "Failed to decode image", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No image found!", Toast.LENGTH_SHORT).show();
        }
    }
}
