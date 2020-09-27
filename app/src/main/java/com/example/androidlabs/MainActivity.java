package com.example.androidlabs;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //your program starts here
        super.onCreate(savedInstanceState);

        //nothing on screen yet
        //load your screen
        //setContentView(R.layout.activity_main);
        //setContentView(R.layout.activity_main_linear);
        setContentView(R.layout.activity_main_grid);
        //setContentView(R.layout.activity_main_relative);

        Button myButton = findViewById(R.id.myButton);
        myButton.setOnClickListener( btn ->
                Toast.makeText(MainActivity.this,getResources().getString(R.string.toast_message) , Toast.LENGTH_LONG).show());

        CheckBox checkBox = findViewById(R.id.CheckBox1);
        checkBox.setOnCheckedChangeListener((cb, b) ->{
            if (b){
                Snackbar.make(checkBox, getResources().getString(R.string.switch_message1),Snackbar.LENGTH_LONG).setAction( getResources().getString(R.string.undo1), click -> cb.setChecked(!b)).show();

            }else{
                Snackbar.make(checkBox,getResources().getString(R.string.switch_message2),Snackbar.LENGTH_LONG).show();
            }
        });

        Switch s= findViewById(R.id.Switch1);
        s.setOnCheckedChangeListener((cb,b)-> {
            if (b) {
                Snackbar.make(checkBox, getResources().getString(R.string.switch_message1), Snackbar.LENGTH_LONG).setAction(getResources().getString(R.string.undo1), click -> cb.setChecked(!b)).show();

            } else {
                Snackbar.make(checkBox,getResources().getString(R.string.switch_message2), Snackbar.LENGTH_LONG).show();
            }
        });


    }
}