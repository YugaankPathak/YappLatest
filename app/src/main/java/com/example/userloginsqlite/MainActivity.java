package com.example.userloginsqlite;

import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.ComponentActivity;
import androidx.annotation.Nullable;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;

import java.util.Calendar;

public class MainActivity extends ComponentActivity {

    TextView txtgreeting;
    Button btnLoginLog;
    Button btnRegisterLog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLoginLog = findViewById(R.id.btnLoginLog);
        btnRegisterLog = findViewById(R.id.btnRegisterLog);
        txtgreeting = findViewById(R.id.txtgreeting);

        setGreetingMessage();

        btnRegisterLog.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, register.class);
            startActivity(i);
        });

        btnLoginLog.setOnClickListener(view -> {

            Intent i = new Intent(MainActivity.this, login.class);
            startActivity(i);
        });
    }

    private void setGreetingMessage() {
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        String greeting;

        if (hourOfDay >= 5 && hourOfDay < 12) {
            greeting = "Good Morning!";
        } else if (hourOfDay >= 12 && hourOfDay < 17) {
            greeting = "Good Afternoon!";
        } else if (hourOfDay >= 17 && hourOfDay < 21) {
            greeting = "Good Evening!";
        } else {
            greeting = "Good Night!";
        }

        txtgreeting.setText(greeting);
    }
}
