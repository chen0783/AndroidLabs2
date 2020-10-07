package com.example.androidlabs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    SharedPreferences prefs = null;
    EditText typeField;
    Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_lab3);

        // Load SharedPreferences with user email
        prefs = getSharedPreferences("FileName", Context.MODE_PRIVATE);
        String savedEmail = prefs.getString("ReserveName", "");

        //TextView tx1 = findViewById(R.id.textEmail);
        typeField = findViewById(R.id.editEmail);
        typeField.setText(savedEmail);

        //TextView tx2 = findViewById(R.id.textPassword);
        EditText typePassword = findViewById(R.id.editPassword);

        //click logButton to profile page
        Intent goToProfile = new Intent(MainActivity.this, ProfileActivity.class);
        Button saveBtn = findViewById(R.id.logBtn);
        saveBtn.setOnClickListener(bt -> {
            if((typeField.getText().toString().equals("")) || (typePassword.getText().toString().equals(""))){
                Toast.makeText(this, "Enter email or password", Toast.LENGTH_SHORT).show();
            }else {
                goToProfile.putExtra("EMAIL", typeField.getText().toString());
                startActivity(goToProfile);
            }
        });
    }

    private void saveSharedPrefs(String stringToSave)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("ReserveName", stringToSave);
        editor.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Use sharedPreferences to save input email
        saveSharedPrefs(typeField.getText().toString());
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}