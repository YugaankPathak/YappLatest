package com.example.userloginsqlite;
import java.util.Base64;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.annotation.RequiresApi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicReference;

public class AddItem extends AppCompatActivity {

    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private ImageButton imageButton;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private byte[] imageBytes; // Store the image as a byte array for saving
    private dbConnect database; // Use the dbConnect class for network operations
    public int imagecount = 0;
private Uri imageUri;
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        imageButton = findViewById(R.id.imageButton);
        final int RADIO_UPPER_ID = R.id.radio_upper;
        final int RADIO_LOWER_ID = R.id.radio_lower;
        final int RADIO_OTHER_ID = R.id.radio_other;

        AtomicReference<RadioGroup> radioGroup = new AtomicReference<>(findViewById(R.id.radio_group));
        Spinner spinner = findViewById(R.id.spinner_category);

        radioGroup.get().setOnCheckedChangeListener((group, checkedId) -> {
            int itemsArray;

            if (checkedId == RADIO_UPPER_ID) {
                itemsArray = R.array.upper_body_array;
            } else if (checkedId == RADIO_LOWER_ID) {
                itemsArray = R.array.lower_body_array;
            } else if (checkedId == RADIO_OTHER_ID) {
                itemsArray = R.array.accessory_array;
            } else {
                itemsArray = R.array.upper_body_array; // default option
            }

            updateSpinner(itemsArray, spinner);
        });

        // Spinner setup
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.base_showcase, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Image picker launcher
        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                imageUri = result.getData() != null ? result.getData().getData() : null;
                if (imageUri != null) {
                    imageButton.setImageURI(imageUri);  // Show selected image

                    // Convert the selected image to a byte[]
                    try {
                        imageBytes = UriToByteArrayConverter.getBytesFromUri(getContentResolver(), imageUri);
                       ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), imageUri);
                       Bitmap bitmap = ImageDecoder.decodeBitmap(source);
                        imageBytes = imageToByteArray(bitmap);  // Convert the image to byte array
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // Permission request launcher
        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                imagePickerLauncher.launch(intent);
            } else {
                Toast.makeText(AddItem.this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        });

        imageButton.setOnClickListener(view -> handleImageSelection());

        // Initialize dbConnect
        database = new dbConnect();

        Button submitButton = findViewById(R.id.button_submit);
        submitButton.setOnClickListener(view -> {
            int radioID = radioGroup.get().getCheckedRadioButtonId();
            RadioButton radioButton = findViewById(radioID);
            String upper = radioButton.getText().toString();
            String selectedCategory = spinner.getSelectedItem().toString();
            String color = ((EditText) findViewById(R.id.input_color)).getText().toString();
            String material = ((EditText) findViewById(R.id.input_material)).getText().toString();

            Apparel apparel = new Apparel();
            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            String userID = sharedPreferences.getString("userID", null);
            Log.e("ID", userID != null ? userID : "userID is null");

            apparel.setOwnership(userID);
            apparel.setColor(color);
            apparel.setMaterial(material);
            apparel.setUpperLower(upper);
            apparel.setOccasion(selectedCategory);

            // Assuming imageUri is available here
            String mimeType = null;
            if (imageUri != null) {
                ContentResolver contentResolver = getContentResolver();
                mimeType = contentResolver.getType(imageUri);
            }

            if (imageBytes != null && mimeType != null) {
                String base64Image = "data:" + mimeType + ";base64," + Base64.getEncoder().encodeToString(imageBytes);
                apparel.setImage(base64Image);
            }

            Log.e("app", apparel.getImage() != null ? apparel.getImage() : "Image is null");

            // Create the apparel record using dbConnect
            database.createApparel(apparel);

            // Clear form fields after submission
            radioGroup.get().clearCheck();
            spinner.setSelection(0);
            ((EditText) findViewById(R.id.input_color)).setText("");
            ((EditText) findViewById(R.id.input_material)).setText("");
            imageButton.setImageResource(android.R.color.transparent);

            Toast.makeText(AddItem.this, "Apparel added successfully!", Toast.LENGTH_SHORT).show();
            imagecount++;

            Intent intent = new Intent(AddItem.this, WardrobeActivity.class);
            intent.putExtra("IMAGE_COUNT", imagecount);
            startActivity(intent); // Clear selected image
        });


    }

    private void handleImageSelection() {
        String permission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                ? Manifest.permission.READ_MEDIA_IMAGES
                : Manifest.permission.READ_EXTERNAL_STORAGE;

        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(permission);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent);
        }
    }

    private void updateSpinner(int itemsArray, Spinner spinner) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                itemsArray,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    // Convert Bitmap to byte array
    private byte[] imageToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

}

