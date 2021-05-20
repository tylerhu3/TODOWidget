package com.hu.tyler.todowidget;
/*
Last Edit: 12/20/2017 for code cleaning and comments

*This is the activity that opens up when the application is ran,
*it's basic information about how to use this app and myself and
*checks to make sure that the app has permission to Read and Edit from Storage,
*as this app stores, edits and reads from the SDCard Root directory*/
import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    public static Activity mainActivty01; //Keep this activity open

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivty01 = this;
    }

}
