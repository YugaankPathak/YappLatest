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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class WardrobeActivity extends AppCompatActivity {

    private static final String TAG = "home_page";
    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;
    private List<byte[]> imageList;
    private database dbHelper;
    private Button btnAll, btnUpper, btnLower, btnOthers;
    private ImageView male_icon, female_icon, default_icon, more_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_wardrobe);

        // Initialize UI elements
        ConstraintLayout containerProfile = findViewById(R.id.container_profile);
        male_icon = findViewById(R.id.male_icon);
        female_icon = findViewById(R.id.female_icon);
        default_icon = findViewById(R.id.default_icon);
        more_icon = findViewById(R.id.more_icon);

        btnAll = findViewById(R.id.btnallapparels);
        btnUpper = findViewById(R.id.btnupperapparels);
        btnLower = findViewById(R.id.btnlowerapparels);
        btnOthers = findViewById(R.id.btnothers);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        imageList = new ArrayList<>();
        imageAdapter = new ImageAdapter(this, imageList);
        recyclerView.setAdapter(imageAdapter);

        dbHelper = new database(this);
        dbConnect db;

        // Initialize dbConnect instance
        db = new dbConnect(this);

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

        more_icon.setOnClickListener(v -> {
            Intent intent = new Intent(WardrobeActivity.this, AddItem.class);
            startActivity(intent);
        });

        btnAll.setOnClickListener(v -> loadImages("All"));
        btnUpper.setOnClickListener(v -> loadImages("Upper"));
        btnLower.setOnClickListener(v -> loadImages("Lower"));
        btnOthers.setOnClickListener(v -> loadImages("Other"));

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
        imageAdapter.notifyDataSetChanged(); // Notify adapter about data change

        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String selection = null;
        String[] selectionArgs = null;

        if (!category.equals("All")) {
            selection = "type = ?";
            selectionArgs = new String[]{category};
        }

        Cursor cursor = database.query(
                "apparel",
                new String[]{"image"},
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            do {
                byte[] imageBlob = cursor.getBlob(0);
                imageList.add(imageBlob);
            } while (cursor.moveToNext());
        }
        cursor.close();

        imageAdapter.notifyDataSetChanged(); // Notify adapter about new data
    }


}
