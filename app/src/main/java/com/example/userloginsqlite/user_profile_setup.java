package com.example.userloginsqlite;

import static java.lang.Integer.parseInt;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class user_profile_setup extends AppCompatActivity {
    EditText edtname, edtgender, edtage, edtemail, edtbio;
    Button btnsave;
    dbConnect db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_profile_setup);

        edtname = findViewById(R.id.edtname);
        edtgender = findViewById(R.id.edtgender);
        edtage = findViewById(R.id.edtage);
        edtemail = findViewById(R.id.edtemail);
        edtbio = findViewById(R.id.edtbio);
        btnsave = findViewById(R.id.btnsave);

        db = new dbConnect();

        // Get the intent data
        Intent intent = getIntent();
        String googleName = intent.getStringExtra("googleName");
        String googleEmail = intent.getStringExtra("googleEmail");

        // Set the email and name in the EditText fields
        edtemail.setText(googleEmail);
        edtname.setText(googleName);

        // Save button logic
        btnsave.setOnClickListener(v -> {
            String name = edtname.getText().toString();
            String gender = edtgender.getText().toString();
            String ageStr = edtage.getText().toString();
            String bio = edtbio.getText().toString();

            // Validate inputs
            if (name.isEmpty() || gender.isEmpty() || ageStr.isEmpty()) {
                // Show error message if required fields are missing
                Toast.makeText(user_profile_setup.this, "Please fill all required fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            int age;
            try {
                age = parseInt(ageStr); // Convert age to integer
            } catch (NumberFormatException e) {
                Toast.makeText(user_profile_setup.this, "Invalid age format.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Fetch existing user data and update it
            String userId = db.getUserIdByEmail(googleEmail); // Fetch user ID using email
            if (userId.isEmpty()) {
                users user = new users(parseInt(userId), name, googleEmail, null, gender, age, bio);
                db.updateUser(user); // Update user profile
                Toast.makeText(user_profile_setup.this, "Profile updated successfully.", Toast.LENGTH_SHORT).show();

                // Pass gender back to Home_Page
                Intent resultIntent = new Intent();
                resultIntent.putExtra("gender", gender);
                setResult(RESULT_OK, resultIntent);

                // Navigate to Home_Page
                Intent homeIntent = new Intent(user_profile_setup.this, login.class);
                homeIntent.putExtra("gender", gender);
                startActivity(homeIntent);
                finish();
            } else {
                Toast.makeText(user_profile_setup.this, "User not found.", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle system insets for edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
