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
    private int STORAGE_PERMISSION_CODE = 23; // permission code to ask for storage permission

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivty01 = this;
        if (!isReadStorageAllowed()) {
            showToastMessage("Need Read Storage permission");
            requestStoragePermission();
        }
    }

    //We are calling this method to check the permission status
    private boolean isReadStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }


    //Requesting permission
    public void requestStoragePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            showToastMessage("Need Permission to store TODOList in SDCARD. :)");
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    void showToastMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT)
                .show();
    }
}
