package com.example.userloginsqlite;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class WardrobeActivity extends AppCompatActivity {

    private static final String TAG = "WardrobeActivity";
    RecyclerView recyclerView;
    ImageAdapter imageAdapter;
    List<byte[]> imageList;
    ImageView image1,image2,image3;
    database dbHelper; // This should be the database class for apparels
    dbConnect userDbHelper; // This should be the database class for users
    Button btnAll, btnUpper, btnLower, btnOthers;
    ImageView male_icon, female_icon, default_icon, more_icon, icon_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_wardrobe);

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

        dbHelper = new database(this); // Apparels database helper
        userDbHelper = new dbConnect(this); // Users database helper

        // Retrieve email from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", null);

        more_icon.setOnClickListener(v -> {
            Intent intent = new Intent(WardrobeActivity.this, AddItem.class);
            startActivity(intent);
        });

        btnAll.setOnClickListener(v -> loadImages("All"));
        btnUpper.setOnClickListener(v -> loadImages("Upper"));
        btnLower.setOnClickListener(v -> loadImages("Lower"));
        btnOthers.setOnClickListener(v -> loadImages("Accessory"));

        // Load all images by default
        loadImages("All");

        // Adjust padding for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.container_profile), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loadImages(String category) {
        imageList.clear(); // Clear existing images

        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String selection = null;
        String[] selectionArgs = null;

        if (!category.equals("All")) {
            selection = "type = ?";
            selectionArgs = new String[]{category};
        }

        Cursor cursor = database.query(
                "apparel", // Table name
                new String[]{"image"},
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        // Variable to keep track of image count
        int imageCount = 0;

        if (cursor.moveToLast()) {
            do {
                byte[] imageBlob = cursor.getBlob(0);
                imageList.add(0, imageBlob);  // Add images to the start of the list (most recent first)
                imageCount++;
            } while (cursor.moveToPrevious());
        }
        cursor.close();

        // Update the visibility of the image views based on the number of images
        updateImageViewsVisibility(imageCount);

        // Notify adapter about new data
        imageAdapter.notifyDataSetChanged();
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
