package com.example.userloginsqlite;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class user_profile extends AppCompatActivity {

    private static final String TAG = "UserProfileActivity";

    Button edit_profile_button;
    ImageView profile_image, backbutton, logout_button;
    TextView txtuserprofilegender, txtuserprofileage, user_name, user_email, user_bio;
    dbConnect db;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_profile);

        // Initialize views
        edit_profile_button = findViewById(R.id.edit_profile_button);
        txtuserprofilegender = findViewById(R.id.txtuserprofilegender);
        txtuserprofileage = findViewById(R.id.txtuserprofileage);
        user_name = findViewById(R.id.user_name);
        user_email = findViewById(R.id.user_email);
        user_bio = findViewById(R.id.user_bio);
        profile_image = findViewById(R.id.profile_image);
        backbutton = findViewById(R.id.backbutton);
        logout_button = findViewById(R.id.logout_button);

        // Initialize database and shared preferences
        db = new dbConnect();
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        // Get email from shared preferences
        String email = sharedPreferences.getString("email", null);

        if (email != null) {
            // Fetch user data from database using email
            try {
                users user = db.getUserByEmail(email);
                if (user != null) {
                    Log.d(TAG, "User found: " + user.getName());

                    user_name.setText(user.getName() != null ? user.getName() : "");
                    user_email.setText(user.getEmail() != null ? user.getEmail() : "");
                    txtuserprofilegender.setText(user.getGender() != null ? user.getGender() : "");
                    txtuserprofileage.setText(user.getAge() != 0 ? String.valueOf(user.getAge()) : "");
                    user_bio.setText(user.getBio() != null ? user.getBio() : "");

                    // Set profile image based on gender
                    if ("male".equalsIgnoreCase(user.getGender())) {
                        profile_image.setImageResource(R.drawable.ic_male);
                    } else if ("female".equalsIgnoreCase(user.getGender())) {
                        profile_image.setImageResource(R.drawable.ic_female);
                    } else {
                        profile_image.setImageResource(R.drawable.ic_defaultprofile);
                    }
                } else {
                    Log.d(TAG, "User not found for email: " + email);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error fetching user data", e);
            }
        } else {
            Log.d(TAG, "Email not found in SharedPreferences");
        }

        // Back button action
        backbutton.setOnClickListener(view -> {
            Intent intent = new Intent(user_profile.this, home_page.class);
            startActivity(intent);
            finish();
        });

        // Logout button action
        logout_button.setOnClickListener(view -> {
            new AlertDialog.Builder(user_profile.this)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.apply();

                        Intent intent = new Intent(user_profile.this, login.class);
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        // Edit profile button action
        edit_profile_button.setOnClickListener(view -> {
            Intent intent = new Intent(user_profile.this, edit_profile.class);
            startActivity(intent);
        });
    }
}
