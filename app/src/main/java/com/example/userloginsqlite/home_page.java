package com.example.userloginsqlite;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class home_page extends AppCompatActivity {

    private static final String TAG = "home_page";
    private final Map<ImageView, ActivityResultLauncher<Intent>> imageViewToLauncherMap = new HashMap<>();
    private ImageView selectedImageView;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    AtomicReference<String> selectedDate = new AtomicReference<>("");

    private RecyclerView recyclerView;
    private AttireAdapter feedAdapter;
    private LinearLayoutManager linearLayoutManager;
    private boolean isLoading = false;
    private final int visibleThreshold = 5;
    private List<Attire_DateView> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

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

        ImageView stylehub= findViewById(R.id.icon_style);
        stylehub.setOnClickListener(view -> {
            try {
                Intent s = new Intent(home_page.this, style_hub.class);
                startActivity(s);
            } catch (Exception e) {
                Log.e(TAG, "Error while starting style hub activity", e);
            }
        });

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
                Intent i = new Intent(home_page.this, user_profile.class);
                startActivity(i);
            } catch (Exception e) {
                Log.e(TAG, "Error while starting user_profile activity", e);
            }
        });
        icon_wardrobe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(home_page.this, WardrobeActivity.class);
                startActivity(i);
            }
        });

        container_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent w1 = new Intent(home_page.this, work_space.class);
                startActivity(w1);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //permission to access external storage
        registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                Log.d("SetLayout", "Permission granted");
            } else {
                Log.d("SetLayout", "Permission denied");
            }
        });


        CalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDate.set(dayOfMonth + "/" + (month + 1) + "/" + year);
            Toast.makeText(home_page.this, "Selected Date: " + selectedDate, Toast.LENGTH_SHORT).show();
        });

        initializeImageViewLauncher();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Find the overlay container (FrameLayout) in the main layout
                FrameLayout overlayContainer = findViewById(R.id.overlay_container);

                // Inflate the new layout and add it to the overlay container
                LayoutInflater inflater = LayoutInflater.from(home_page.this);
                View inflatedView = inflater.inflate(R.layout.dialogue_new_layout, overlayContainer, false);

                // Add the inflated view to the overlay container
                overlayContainer.addView(inflatedView);

                // Make the overlay visible
                overlayContainer.setVisibility(View.VISIBLE);

                // Handle the button inside the inflated layout
                ImageView attButton = inflatedView.findViewById(R.id.dialog_choose);
                attButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedImageView = findViewById(R.id.dialog_choose);
                        pickImage();
                    }
                });

                Button dialogButton = inflatedView.findViewById(R.id.dialog_submit);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(home_page.this, "Saved!", Toast.LENGTH_SHORT).show();

                        // Remove the overlay after button click (or perform another action)
                        overlayContainer.removeAllViews();
                        overlayContainer.setVisibility(View.GONE);
                    }
                });
            }
        });

        //Yugaank
        //will req DB commands when we Integrate, rest all should work fine
     //   items.add(new Attire_DateView(selectedDate.get(),R.drawable.ic_image1));
       // items.add(new Attire_DateView(selectedDate.get(),R.drawable.ic_image1));



        recyclerView = findViewById(R.id.recyclerView);
        feedAdapter = new AttireAdapter(items);
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

        AttireAdapter adapter = new AttireAdapter(items);
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter.notifyDataSetChanged();


    }
    private void initializeImageViewLauncher() {
        // Register for the activity result here
        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri selectedImage = result.getData().getData();
                        // Display the selected image in the ImageView
                        selectedImageView.setImageURI(selectedImage);
                    }
                });
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent); // Launch the image picker
    }

    private void loadMoreData() {
        // Simulate network or database call
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            List<Attire_DateView> newItems = fetchData();
            feedAdapter.addItems(newItems);
            isLoading = false;
        }, 2000);
    }

    private List<Attire_DateView> fetchData() {
        // Generate or fetch new items
        return new ArrayList<>(); // Replace with actual data
    }
}
