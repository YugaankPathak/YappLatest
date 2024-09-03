package com.example.userloginsqlite;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class login extends AppCompatActivity {

    TextView txtlogin, txtenteremail, txtenterpassword, txtforgotpassword, txtloginwithgoogle;
    EditText ptenteremail, ptenterpassword;
    Button btnlogin;
    LinearLayout btnloginwithgoogle;
    dbConnect db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtlogin = findViewById(R.id.txtlogin);
        txtenteremail = findViewById(R.id.txtenteremail);
        txtenterpassword = findViewById(R.id.txtenterpassword);
        txtforgotpassword = findViewById(R.id.txtforgotpassword);
        ptenteremail = findViewById(R.id.ptenteremail);
        ptenterpassword = findViewById(R.id.ptenterpassword);
        btnlogin = findViewById(R.id.btnlogin);

        db = new dbConnect(this);

        txtforgotpassword.setOnClickListener(view -> {
            Intent intent = new Intent(login.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });



        btnlogin.setOnClickListener(view -> {
            String email = ptenteremail.getText().toString().trim();
            String password = ptenterpassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(login.this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            if (db.checkEmailExists(email)) {
                String storedPassword = db.getPasswordByEmail(email);
                if (storedPassword != null && storedPassword.equals(password)) {
                    // Store email in SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("email", email);
                    editor.apply();

                    // Login successful
                    Toast.makeText(login.this, "Login successful", Toast.LENGTH_SHORT).show();
                    try {
                        Intent intent = new Intent(login.this, home_page.class);
                        startActivity(intent);
                        finish(); // Ensure the login activity is closed
                    } catch (Exception e) {
                        Log.e("LoginError", "Error while starting Home_Page activity", e);
                        Toast.makeText(login.this, "An error occurred while navigating", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Invalid password
                    Toast.makeText(login.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Email does not exist
                Toast.makeText(login.this, "Email does not exist", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
