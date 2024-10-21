package com.example.userloginsqlite;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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

        db = new dbConnect();

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

            new LoginTask().execute(email, password);
        });
    }

    private class LoginTask extends AsyncTask<String, Void, Boolean> {
        private String email;
        private String password;

        @Override
        protected Boolean doInBackground(String... params) {
            email = params[0];
            password = params[1];

            // Check if email exists
            boolean emailExists = db.checkEmailExists(email);

            if (!emailExists) {
                return false;
            }

            // Get password by email
            String storedPassword = db.getPasswordByEmail(email);
            // Store email in SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("userEmail", email);
            String idd = db.getUserIdByEmail(email);
           // Log.e("mmm", db.getAllApparelsByEmail("tanmayzitshi0@gmail.com").get(0).get("image"));
            editor.putString("userID", idd);
            Log.e("xxxx", email);
            Log.e("xxxx", idd);

            // editor.putString("userID",idd);
            editor.apply();
            return storedPassword != null && storedPassword.equals(password);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Log.e("xxxx", email);

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
                // Invalid email or password
                Toast.makeText(login.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
