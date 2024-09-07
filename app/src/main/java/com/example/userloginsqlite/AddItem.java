package com.example.userloginsqlite;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
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

public class AddItem extends AppCompatActivity {

    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private ImageButton imageButton;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private byte[] imageBlob; // Store the image as a byte array for saving
    public int imagecount=0;
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        RadioGroup radioGroup = findViewById(R.id.radio_group);
        Spinner spinner = findViewById(R.id.spinner_category);
        imageButton = findViewById(R.id.imageButton);

        // Spinner setup
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Image picker launcher
        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Uri imageUri = result.getData() != null ? result.getData().getData() : null;
                if (imageUri != null) {
                    imageButton.setImageURI(imageUri);  // Show selected image

                    // Convert the selected image to a BLOB
                    try {
                        ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), imageUri);
                        Bitmap bitmap = ImageDecoder.decodeBitmap(source);
                        imageBlob = imageToByteArray(bitmap);  // Convert the image to byte array
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

        database db = new database(this);

        Button submitButton = findViewById(R.id.button_submit);
        submitButton.setOnClickListener(view -> {
            int radioID = radioGroup.getCheckedRadioButtonId();
            RadioButton radioButton = findViewById(radioID);
            String upper = radioButton.getText().toString();
            String selectedCategory = spinner.getSelectedItem().toString();
            String color = ((EditText) findViewById(R.id.input_color)).getText().toString();
            String material = ((EditText) findViewById(R.id.input_material)).getText().toString();
            boolean isPersonal = ((Switch) findViewById(R.id.switch2)).isChecked();

            ContentValues values = new ContentValues();
            values.put("ownership", isPersonal ? "Personal" : "Not Owned");
            values.put("color", color);
            values.put("material", material);
            values.put("upper_lower", upper);
            values.put("type", selectedCategory);

            if (imageBlob != null) {
                values.put("image", imageBlob);  // Save the imageBlob in the database
            }

            long newRowId = db.getWritableDatabase().insert("Apparel", null, values);

            if (newRowId != -1) {
                Toast.makeText(AddItem.this, "Apparel added successfully!", Toast.LENGTH_SHORT).show();
                imagecount++;
                // Clear form fields after submission
                radioGroup.clearCheck();
                spinner.setSelection(0);
                ((EditText) findViewById(R.id.input_color)).setText("");
                ((EditText) findViewById(R.id.input_material)).setText("");
                ((Switch) findViewById(R.id.switch2)).setChecked(false);
                imageButton.setImageResource(android.R.color.transparent);

                Intent intent = new Intent(AddItem.this, WardrobeActivity.class);
                intent.putExtra("IMAGE_COUNT", imagecount);
                startActivity(intent);// Clear selected image
            } else {
                Log.e("DB_INSERT_ERROR", "Error inserting row with values: " + values);
                Toast.makeText(AddItem.this, "Error adding apparel", Toast.LENGTH_SHORT).show();
            }
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

    // Convert Bitmap to byte array
    private byte[] imageToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}
