package com.example.androidlabs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    //create a static String variable as the activity name
    public static final String ACTIVITY_NAME = "activity_profile";
    private ImageButton mImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //TextView profileTX = findViewById(R.id.profile);
        //TextView nameTX = findViewById(R.id.name);
        //TextView emailTX = findViewById(R.id.email);
        //TextView pictureTX = findViewById(R.id.picture);
        //TextView clickTx = findViewById(R.id.clickMsg);

        Intent fromMain = getIntent();
        String emailStr = fromMain.getStringExtra("EMAIL");
        EditText emailField = findViewById(R.id.inputEmail);
        emailField.setText(emailStr);

        EditText nameField = findViewById(R.id.inputName);

        mImageButton = (ImageButton)findViewById(R.id.imageButton);
        if (mImageButton != null) {
            mImageButton.setOnClickListener(click -> dispatchTakePictureIntent());
        }

        Button goToChat = (Button)findViewById(R.id.chatBtn);
        goToChat.setOnClickListener( click->{
            Intent gotoChat=new Intent(ProfileActivity.this,ChatRoomActivity.class);
            startActivity(gotoChat);
        } );

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageButton.setImageBitmap(imageBitmap);
        }
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(ACTIVITY_NAME, "In Function:onActivityResult()");
    }


    @Override
    protected void onStart() {
        Log.e(ACTIVITY_NAME, "In Function:onStart()");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.e(ACTIVITY_NAME, "In Function:onResume()");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.e(ACTIVITY_NAME, "In Function:onPause()");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.e(ACTIVITY_NAME, "In Function:onStop()");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.e(ACTIVITY_NAME, "In Function:onDestroy()");
        super.onDestroy();
    }

    private void onClick(View click) {
        Intent goToChatPage = new Intent( this, ChatRoomActivity.class );
        startActivityForResult( goToChatPage, 30 );
    }
}