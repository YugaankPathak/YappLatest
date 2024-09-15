package com.example.userloginsqlite;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class edit_profile extends AppCompatActivity {
    private EditText edtname, edtemail, edtgender, edtage, edtbio;
    private Button btnsave;
    private ImageView backimage;
    private dbConnect db;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize views
        edtname = findViewById(R.id.edtname);
        edtemail = findViewById(R.id.edtemail);
        edtgender = findViewById(R.id.edtgender);
        edtage = findViewById(R.id.edtage);
        edtbio = findViewById(R.id.edtbio);
        btnsave = findViewById(R.id.btnsave);
        backimage = findViewById(R.id.backimage);

        // Initialize database connection and SharedPreferences
        db = new dbConnect();
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String email = sharedPreferences.getString("user_email", null);

        // Load existing profile data
        if (email != null) {
            users user = db.getUserByEmail(email);
            if (user != null) {
                edtname.setText(user.getName());
                edtemail.setText(user.getEmail());
                edtgender.setText(user.getGender());
                edtage.setText(String.valueOf(user.getAge()));
                edtbio.setText(user.getBio());
            } else {
                Toast.makeText(this, "User not found.", Toast.LENGTH_SHORT).show();
            }
        }

        // Save updated profile data
        btnsave.setOnClickListener(view -> {
            String name = edtname.getText().toString().trim();
            String emailInput = edtemail.getText().toString().trim();
            String gender = edtgender.getText().toString().trim();
            String ageStr = edtage.getText().toString().trim();
            String bio = edtbio.getText().toString().trim();

            // Validate required fields
            if (name.isEmpty() || gender.isEmpty() || ageStr.isEmpty()) {
                Toast.makeText(this, "Please fill all required fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            int age;
            try {
                age = Integer.parseInt(ageStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid age format.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create a user object with updated data
            users updatedUser = new users(name, emailInput, gender, age, bio);

            // Update user profile in the database
            boolean isUpdated = db.updateUserProfile(updatedUser);
            if (isUpdated) {
                // Update SharedPreferences if email is changed
                if (!email.equals(emailInput)) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("user_email", emailInput);
                    editor.apply();
                }

                Toast.makeText(this, "Profile updated successfully.", Toast.LENGTH_SHORT).show();

                // Navigate back to user_profile activity
                Intent intent = new Intent(edit_profile.this, user_profile.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Failed to update profile.", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "You can't Update E-mail", Toast.LENGTH_SHORT).show();

            }
        });

        // Back button functionality
        backimage.setOnClickListener(view -> {
            Intent intent = new Intent(edit_profile.this, user_profile.class);
            startActivity(intent);
        });
    }
}
