package com.example.userloginsqlite;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class combinations extends ComponentActivity {

    private ImageView imageView1, imageView2, imageView3, imageView4, imageView5, imageView6, imageView7, imageView8;
    private ImageView imageView21, imageView22, imageView23, imageView24, imageView25, imageView26, imageView27, imageView28;
    private ImageView imageView31, imageView32, imageView33, imageView34, imageView35, imageView36, imageView37, imageView38;
    private ImageView imageView41, imageView42, imageView43, imageView44, imageView45, imageView46, imageView47, imageView48;

    private final Map<ImageView, ActivityResultLauncher<Intent>> imageViewToLauncherMap = new HashMap<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.combination);

        Button backbtn = findViewById(R.id.back_button);
        backbtn.setOnClickListener(v -> {
            // Create an intent to start the HomeActivity
            Intent intent = new Intent(combinations.this, work_space.class);
            startActivity(intent);
        });

        Button screenshotButton = findViewById(R.id.save_button);
        screenshotButton.setOnClickListener(v -> takeScreenshot());

        // Request permission for saving to external storage
        registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                Log.d("SetLayout", "Permission granted");
            } else {
                Log.d("SetLayout", "Permission denied");
            }
        });

        // Initialize image views and their launchers
        initializeImageViews();
        initializeImageViewLaunchers();
    }

    private void initializeImageViews() {
        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        imageView4 = findViewById(R.id.imageView4);
        imageView5 = findViewById(R.id.imageView5);
        imageView6 = findViewById(R.id.imageView6);
        imageView7 = findViewById(R.id.imageView7);
        imageView8 = findViewById(R.id.imageView8);

        imageView21 = findViewById(R.id.imageView21);
        imageView22 = findViewById(R.id.imageView22);
        imageView23 = findViewById(R.id.imageView23);
        imageView24 = findViewById(R.id.imageView24);
        imageView25 = findViewById(R.id.imageView25);
        imageView26 = findViewById(R.id.imageView26);
        imageView27 = findViewById(R.id.imageView27);
        imageView28 = findViewById(R.id.imageView28);

        imageView31 = findViewById(R.id.imageView31);
        imageView32 = findViewById(R.id.imageView32);
        imageView33 = findViewById(R.id.imageView33);
        imageView34 = findViewById(R.id.imageView34);
        imageView35 = findViewById(R.id.imageView35);
        imageView36 = findViewById(R.id.imageView36);
        imageView37 = findViewById(R.id.imageView37);
        imageView38 = findViewById(R.id.imageView38);

        imageView41 = findViewById(R.id.imageView41);
        imageView42 = findViewById(R.id.imageView42);
        imageView43 = findViewById(R.id.imageView43);
        imageView44 = findViewById(R.id.imageView44);
        imageView45 = findViewById(R.id.imageView45);
        imageView46 = findViewById(R.id.imageView46);
        imageView47 = findViewById(R.id.imageView47);
        imageView48 = findViewById(R.id.imageView48);


    }

    private void initializeImageViewLaunchers() {
        initializeImageViewLauncher(imageView1);
        initializeImageViewLauncher(imageView2);
        initializeImageViewLauncher(imageView3);
        initializeImageViewLauncher(imageView4);
        initializeImageViewLauncher(imageView5);
        initializeImageViewLauncher(imageView6);
        initializeImageViewLauncher(imageView7);
        initializeImageViewLauncher(imageView8);

        initializeImageViewLauncher(imageView21);
        initializeImageViewLauncher(imageView22);
        initializeImageViewLauncher(imageView23);
        initializeImageViewLauncher(imageView24);
        initializeImageViewLauncher(imageView25);
        initializeImageViewLauncher(imageView26);
        initializeImageViewLauncher(imageView27);

        initializeImageViewLauncher(imageView31);
        initializeImageViewLauncher(imageView32);
        initializeImageViewLauncher(imageView33);
        initializeImageViewLauncher(imageView34);
        initializeImageViewLauncher(imageView35);
        initializeImageViewLauncher(imageView36);
        initializeImageViewLauncher(imageView37);
        initializeImageViewLauncher(imageView38);

        initializeImageViewLauncher(imageView41);
        initializeImageViewLauncher(imageView42);
        initializeImageViewLauncher(imageView43);
        initializeImageViewLauncher(imageView44);
        initializeImageViewLauncher(imageView45);
        initializeImageViewLauncher(imageView46);
        initializeImageViewLauncher(imageView47);
        initializeImageViewLauncher(imageView48);



    }

    private void takeScreenshot() {
        View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        rootView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(rootView.getDrawingCache());
        rootView.setDrawingCacheEnabled(false);

        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "screenshot_" + System.currentTimeMillis() + ".png");
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            Log.d("SetLayout", "Screenshot saved to " + file.getAbsolutePath());
            Toast.makeText(this, "Screenshot saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("SetLayout", "Failed to save screenshot: " + e.getMessage());
        }
    }

    private void initializeImageViewLauncher(ImageView imageView) {
        ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    Uri uri = data.getData();
                    if (uri != null) {
                        try (InputStream inputStream = getContentResolver().openInputStream(uri)) {
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            imageView.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        imageViewToLauncherMap.put(imageView, launcher);

        imageView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imageViewToLauncherMap.get(imageView).launch(intent);
        });
    }
}

