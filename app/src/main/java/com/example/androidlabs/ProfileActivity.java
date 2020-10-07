package com.example.androidlabs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {
    //step 7: create a class variable call mImageButton
    private ImageButton mImageButton;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    //step 9: create a static String varialbe
    public static final String ACTIVITY_NAME = "PROFILE_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //get the object nextpage from FirstActivity.java
        Intent fromMain = getIntent();
        String emailStr = fromMain.getStringExtra("EMAIL");
        EditText emailField = findViewById(R.id.inputEmail);
        emailField.setText(emailStr);
        EditText nameField = findViewById(R.id.inputName);

        mImageButton= findViewById(R.id.imageButton);
        if (mImageButton != null) {
            mImageButton.setOnClickListener(click -> dispatchTakePictureIntent());
        }
        Log.e(ACTIVITY_NAME, "In Function:onCreate()");
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
        Log.e(ACTIVITY_NAME, "In Function:dispatchTakePictureIntent()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(ACTIVITY_NAME, "In function:"+"onStart()");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.e(ACTIVITY_NAME, "In function:"+"onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(ACTIVITY_NAME, "In function:"+"onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(ACTIVITY_NAME, "In function:"+"onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(ACTIVITY_NAME, "In function:"+"onDestroy()");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageButton.setImageBitmap(imageBitmap);
        }
        Log.e(ACTIVITY_NAME, "In function:"+"onActivityResult()");
    }


}
