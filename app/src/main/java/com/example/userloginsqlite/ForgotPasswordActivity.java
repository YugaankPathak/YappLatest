package com.example.userloginsqlite;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText ptForgotName, ptForgotEmail;
    Button btnSubmitForgot;
    TextView txtForgotPasswordMessage;
    dbConnect db;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        ptForgotName = findViewById(R.id.ptForgotName);
        ptForgotEmail = findViewById(R.id.ptForgotEmail);
        btnSubmitForgot = findViewById(R.id.btnSubmitForgot);
        txtForgotPasswordMessage = findViewById(R.id.txtForgotPasswordMessage);

        db = new dbConnect();
        handler = new Handler();

        btnSubmitForgot.setOnClickListener(view -> {
            String name = ptForgotName.getText().toString();
            String email = ptForgotEmail.getText().toString();

            if (name.isEmpty() || email.isEmpty()) {
                txtForgotPasswordMessage.setText("All fields are required");
                txtForgotPasswordMessage.setVisibility(View.VISIBLE);
                hideMessageAfterDelay(2000); // Hide after 2 seconds
            } else if (!db.checkEmailExists(email) || !db.checkNameExists(name)) {
                txtForgotPasswordMessage.setText("Email or Name not found");
                txtForgotPasswordMessage.setVisibility(View.VISIBLE);
                hideMessageAfterDelay(2000); // Hide after 2 seconds
            } else {
                // Logic to retrieve and display the password
                String password = db.getPasswordByNameAndEmail(name, email);

                if (password != null) {
                    Toast.makeText(ForgotPasswordActivity.this, "Password: " + password, Toast.LENGTH_LONG).show();
                } else {
                    txtForgotPasswordMessage.setText("Unable to retrieve password");
                    txtForgotPasswordMessage.setVisibility(View.VISIBLE);
                    hideMessageAfterDelay(2000); // Hide after 2 seconds
                }
            }
        });
    }

    private void hideMessageAfterDelay(long delayMillis) {
        handler.postDelayed(() -> txtForgotPasswordMessage.setVisibility(View.GONE), delayMillis);
    }
}
