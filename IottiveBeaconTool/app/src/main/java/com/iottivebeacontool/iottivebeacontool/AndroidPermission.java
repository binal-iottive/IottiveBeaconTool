package com.iottivebeacontool.iottivebeacontool;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by YJ on 07-04-2017.
 */

public class AndroidPermission {
    public  int STORAGE_PERMISSION_CODE=0;
    public Context context;

    public AndroidPermission(Context context){
        this.context = context;
    }
    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermission(String permission, FragmentActivity activity) {
        new myPermission(permission, activity);
    }


    //RunTime requestPermission Handling

    public class myPermission extends AppCompatActivity {

        String permission;
        FragmentActivity activity;

        public myPermission(String permission, FragmentActivity activity) {
            this.permission = permission;
            this.activity = activity;
            if (isReadStorageAllowed()) {
//                Toast.makeText(context, "Permission is already granted", Toast.LENGTH_SHORT).show();
            } else
                requestStoragePermission();
        }

        @TargetApi(Build.VERSION_CODES.M)
        private boolean isReadStorageAllowed() {
            //Getting the permission status
            int result = ContextCompat.checkSelfPermission(activity, permission);

            //If permission is granted returning true
            if (result == PackageManager.PERMISSION_GRANTED)
                return true;

            //If permission is not granted returning false
            return false;
        }

        //Requesting permission
        @TargetApi(Build.VERSION_CODES.M)
        private void requestStoragePermission() {

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                //If the user has denied the permission previously your code will come to this block
                //Here you can explain why you need this permission
                //Explain here why you need this permission
            }

            //And finally ask for the permission
            ActivityCompat.requestPermissions(activity, new String[]{permission}, STORAGE_PERMISSION_CODE);
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            //Checking the request code of our request
            if (requestCode == STORAGE_PERMISSION_CODE) {

                //If permission is granted
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //Displaying a toast
                    Toast.makeText(context, "Permission granted.", Toast.LENGTH_LONG).show();
                } else {
                    //Displaying another toast if permission is not granted
                    Toast.makeText(context, "Oops, you just denied the permission", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
