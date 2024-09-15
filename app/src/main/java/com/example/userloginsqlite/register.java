package com.example.userloginsqlite;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class register extends AppCompatActivity {

    EditText edtName, edtEmail, edtPassword, edtCnfPassword, edtGender, edtAge, edtBio;
    Button btnRegister, btnLogin;
    dbConnect db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize views
        edtName = findViewById(R.id.ptname);
        edtEmail = findViewById(R.id.ptemail);
        edtPassword = findViewById(R.id.ptpassword);
        edtCnfPassword = findViewById(R.id.ptcnfpassword); // Confirm password field

        btnRegister = findViewById(R.id.btnRegisterLog);
        btnLogin = findViewById(R.id.btnLoginLog);

        // Initialize dbConnect instance
        db = new dbConnect();
        // users newUsermm = new users("nnnn", "t@g.com", "12345678", "male", 34, "ji");
        // db.createUser(newUsermm);
        // Register button click listener
        btnRegister.setOnClickListener(view -> {
            String name = edtName.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            String confirmPassword = edtCnfPassword.getText().toString().trim();

            // Placeholder values for gender, age, and bio
            String gender = "Not Specified"; // Default gender value
            int age = 25;                    // Default age value
            String bio = "No bio available";  // Default bio value

            // Validate inputs
            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(register.this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(register.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // Run registration process in a background thread
            new Thread(() -> {
                boolean emailExists = db.checkEmailExists(email);
                boolean nameExists = db.checkNameExists(name);

                runOnUiThread(() -> {
                    if (!emailExists && !nameExists) {
                        // Email and name are unique, proceed with registration
                        users newUser = new users(name, email, password, gender, age, bio);
                        db.createUser(newUser);
                        Toast.makeText(register.this, "Registration successful", Toast.LENGTH_SHORT).show();

                        // Navigate to login screen after successful registration
                        Intent intent = new Intent(register.this, login.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Show error messages for existing email or name
                        if (emailExists) {
                            Toast.makeText(register.this, "Email already exists", Toast.LENGTH_SHORT).show();
                        }
                        if (nameExists) {
                            Toast.makeText(register.this, "Name already exists", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }).start();
        });

        // Login button click listener
        btnLogin.setOnClickListener(view -> {
            // Navigate to login activity
            Intent intent = new Intent(register.this, login.class);
            startActivity(intent);
            finish();
        });
    }

}
