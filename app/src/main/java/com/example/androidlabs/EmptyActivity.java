package com.example.androidlabs;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class EmptyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
        Bundle dataToPass = getIntent().getExtras(); // important: get data from previous activity
        DetailsFragment dFragment = new DetailsFragment(); //add a DetailFragment
        dFragment.setArguments( dataToPass ); //pass it a bundle for information

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentLocation, dFragment) //Add the fragment in FrameLayout
                .commit(); //actually load the fragment.
    }
}
