package com.iottivebeacontool.iottivebeacontool.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.iottivebeacontool.iottivebeacontool.GlobalApplication;
import com.iottivebeacontool.iottivebeacontool.R;
import com.iottivebeacontool.iottivebeacontool.SharedPrefsUtils;

import static com.iottivebeacontool.iottivebeacontool.activity.BeaconRegisterActivity.place_id;

public class SelectLocation extends AppCompatActivity {
    private GoogleApiClient mGoogleApiClient;
    private int PLACE_PICKER_REQUEST = 0;
    private TextView tv_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);

        tv_location = (TextView) findViewById(R.id.tv_location);

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();

       /* var builder = new PlacePicker.IntentBuilder();
        StartActivityForResult(builder.Build(this), PLACE_PICKER_REQUEST);*/
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(SelectLocation.this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }



    }
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                StringBuilder stBuilder = new StringBuilder();
                String placename = String.format("%s", place.getName());
               BeaconRegisterActivity.place_latitude = place.getLatLng().latitude;
                BeaconRegisterActivity.place_longitude  = place.getLatLng().longitude;
                place_id = String.format("%s", place.getId());
                SharedPrefsUtils.setBooleanPreference(SelectLocation.this, GlobalApplication.boolean_place_id,true);
                SharedPrefsUtils.setStringPreference(SelectLocation.this, GlobalApplication.PLACE_ID,place_id);
                SharedPrefsUtils.setStringPreference(SelectLocation.this, GlobalApplication.PLACE_LAT, String.valueOf(BeaconRegisterActivity.place_latitude));
                SharedPrefsUtils.setStringPreference(SelectLocation.this, GlobalApplication.PLACE_LONG, String.valueOf(BeaconRegisterActivity.place_longitude));
                finish();
              /*  stBuilder.append("Name: ");
                stBuilder.append(placename);
                stBuilder.append("\n");
                stBuilder.append("Latitude: ");
                stBuilder.append(latitude);
                stBuilder.append("\n");
                stBuilder.append("Logitude: ");
                stBuilder.append(longitude);
                stBuilder.append("\n");
                stBuilder.append("Address: ");
                stBuilder.append(place_id_address);

                tv_location.setText(stBuilder.toString());*/
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
