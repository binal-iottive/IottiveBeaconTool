package com.iottivebeacontool.iottivebeacontool.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.iottivebeacontool.iottivebeacontool.GlobalApplication;
import com.iottivebeacontool.iottivebeacontool.NonScrollableListView;
import com.iottivebeacontool.iottivebeacontool.R;
import com.iottivebeacontool.iottivebeacontool.SharedPrefsUtils;
import com.iottivebeacontool.iottivebeacontool.adapter.AddPropertyAdapter;
import com.iottivebeacontool.iottivebeacontool.api.ProximityApi;
import com.iottivebeacontool.iottivebeacontool.fragment.AttachmentFragment;
import com.iottivebeacontool.iottivebeacontool.fragment.DescriptionFragment;
import com.iottivebeacontool.iottivebeacontool.fragment.FloorLevelFragment;
import com.iottivebeacontool.iottivebeacontool.fragment.PropertyFragment;
import com.iottivebeacontool.iottivebeacontool.fragment.StabilityFragment;
import com.iottivebeacontool.iottivebeacontool.model.AddPropertyModel;

import java.util.ArrayList;

public class BeaconRegisterActivity extends Fragment implements View.OnClickListener,OnMapReadyCallback {

    private Activity bcn_reg_activity;
    private TextView tv_tap_location, tv_tap_stability, tv_tap_desc, tv_tap_floorlvl, tv_register_beacon;
    private ToggleButton tb_location, tb_stability, tb_desc, tb_floorlvl;
    private ImageView iv_properties, iv_attechments;
    private NonScrollableListView lv_attachmentList, lv_propretyList;
    private ArrayList<AddPropertyModel> attach_list, prorety_list;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private AddPropertyAdapter addPropertyAdapter = null;
    private ProximityApi mProximityApi;
    public static double place_latitude,place_longitude=0;
    public static String place_id ="";
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private RelativeLayout rl_map;
    private Fragment f;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bcn_reg_activity = getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_beacon_register, container, false);
        bcn_reg_activity = getActivity();
        initUiRegister(view);
        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        iniiUiOnResume();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bcn_reg_activity = getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
        bcn_reg_activity = getActivity();
        mProximityApi = ProximityApi.getInstance(bcn_reg_activity);
        iniiUiOnResume();

    }

    private void initUiRegister(View view) {
        tv_tap_location = (TextView) view.findViewById(R.id.tv_tap_location);
        tv_tap_stability = (TextView) view.findViewById(R.id.tv_tap_stability);
        tv_tap_desc = (TextView) view.findViewById(R.id.tv_tap_desc);
        tv_tap_floorlvl = (TextView) view.findViewById(R.id.tv_tap_floorlvl);
        tv_register_beacon = (TextView) view.findViewById(R.id.tv_register_beacon);
        tb_location = (ToggleButton) view.findViewById(R.id.tb_location);
        tb_stability = (ToggleButton) view.findViewById(R.id.tb_stability);
        tb_desc = (ToggleButton) view.findViewById(R.id.tb_desc);
        tb_floorlvl = (ToggleButton) view.findViewById(R.id.tb_floorlvl);
        iv_attechments = (ImageView) view.findViewById(R.id.iv_attechments);
        iv_properties = (ImageView) view.findViewById(R.id.iv_properties);
        lv_propretyList = (NonScrollableListView) view.findViewById(R.id.lv_propretyList);
        lv_attachmentList = (NonScrollableListView) view.findViewById(R.id.lv_attachmentList);
        rl_map = (RelativeLayout) view.findViewById(R.id.rl_map);

        attach_list = new ArrayList<>();
        prorety_list = new ArrayList<>();

        tv_tap_location.setOnClickListener(this);
        tv_tap_stability.setOnClickListener(this);
        tv_tap_desc.setOnClickListener(this);
        tv_tap_floorlvl.setOnClickListener(this);
        tv_register_beacon.setOnClickListener(this);
        tb_location.setOnClickListener(this);
        tb_stability.setOnClickListener(this);
        tb_desc.setOnClickListener(this);
        tb_floorlvl.setOnClickListener(this);
        iv_attechments.setOnClickListener(this);
        iv_properties.setOnClickListener(this);
    }

    private void iniiUiOnResume() {
        if (bcn_reg_activity instanceof MainActivity) {
            MainActivity myactivity = (MainActivity) bcn_reg_activity;
            myactivity.setToolbar(getString(R.string.registering_bcn), View.GONE);
        }

        setTextVisibility(tb_location, tv_tap_location);
        setTextVisibility(tb_stability, tv_tap_stability);
        setTextVisibility(tb_desc, tv_tap_desc);
        setTextVisibility(tb_floorlvl, tv_tap_floorlvl);

        if (checkPreferenceStringValue(GlobalApplication.boolean_description)) {
            tv_tap_desc.setText(SharedPrefsUtils.getStringPreference(getActivity(), GlobalApplication.description_text));
        }

        if (checkPreferenceStringValue(GlobalApplication.boolean_stable)) {
            tv_tap_stability.setText(SharedPrefsUtils.getStringPreference(getActivity(), GlobalApplication.stable_text));
        }
        if (checkPreferenceStringValue(GlobalApplication.boolean_floor_lvl)) {
            tv_tap_floorlvl.setText(SharedPrefsUtils.getStringPreference(getActivity(), GlobalApplication.floor_lvl_text));
        }
        if (checkPreferenceStringValue(GlobalApplication.boolean_property)) {
            prorety_list = SharedPrefsUtils.getArraylist(getActivity(), GlobalApplication.property_list);
            Log.d("prorety_list======>", String.valueOf(prorety_list.size()));
            addPropertyAdapter = new AddPropertyAdapter(getActivity(), prorety_list, 0);
            lv_propretyList.setAdapter(addPropertyAdapter);
        }
        if (checkPreferenceStringValue(GlobalApplication.boolean_attechment)) {
            attach_list = SharedPrefsUtils.getArraylist(getActivity(), GlobalApplication.attach_list);
            Log.d("prorety_list======>", String.valueOf(attach_list.size()));
            addPropertyAdapter = new AddPropertyAdapter(getActivity(), attach_list, 1);
            lv_attachmentList.setAdapter(addPropertyAdapter);
        }

        if(place_latitude!=0){
            rl_map.setVisibility(View.VISIBLE);
            LatLng sydney = new LatLng(place_latitude, place_longitude);
            mMap.addMarker(new MarkerOptions().position(sydney).title("Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(place_latitude, place_longitude), 12));
            tv_tap_location.setText("Coordinates: "+place_latitude+", "+place_longitude);
        }


        if (checkPreferenceStringValue(GlobalApplication.boolean_place_id)) {
            String place_lat=SharedPrefsUtils.getStringPreference(getActivity(), GlobalApplication.PLACE_LAT);
            String place_long=SharedPrefsUtils.getStringPreference(getActivity(), GlobalApplication.PLACE_LONG);
            String place_id=SharedPrefsUtils.getStringPreference(getActivity(), GlobalApplication.PLACE_ID);
            tv_tap_location.setText("Coordinates: "+place_lat+", "+place_long);

        }


    }

    private boolean checkPreferenceStringValue(String b) {
        boolean nullOrNot = false;
        if (SharedPrefsUtils.getBooleanPreference(bcn_reg_activity, b) == true) {
            nullOrNot = true;
        }
        return nullOrNot;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_tap_desc:

                // I use mapFragment inside Fragment so I remove mapFragment when leave current fragment
                f = getChildFragmentManager().findFragmentById(R.id.map);
                if (f != null) {
                    getChildFragmentManager()
                            .beginTransaction().remove(f).commit();
                }
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                DescriptionFragment des_fragment = new DescriptionFragment();
                fragmentTransaction.replace(R.id.view_container, des_fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;

            case R.id.tv_tap_stability:
                 f = getChildFragmentManager().findFragmentById(R.id.map);
                if (f != null) {
                    getChildFragmentManager()
                            .beginTransaction().remove(f).commit();
                }
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                StabilityFragment sta_fragment = new StabilityFragment();
                fragmentTransaction.replace(R.id.view_container, sta_fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;

            case R.id.tv_tap_floorlvl:
                 f = getChildFragmentManager().findFragmentById(R.id.map);
                if (f != null) {
                    getChildFragmentManager()
                            .beginTransaction().remove(f).commit();
                }
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                FloorLevelFragment floor_fragment = new FloorLevelFragment();
                fragmentTransaction.replace(R.id.view_container, floor_fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;

            case R.id.tv_tap_location:
                Intent intent  = new Intent(getActivity(),SelectLocation.class);
                startActivity(intent);
                break;

            case R.id.iv_properties:
                f = getChildFragmentManager().findFragmentById(R.id.map);
                if (f != null) {
                    getChildFragmentManager()
                            .beginTransaction().remove(f).commit();
                }
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                PropertyFragment property_fragment = new PropertyFragment();
                fragmentTransaction.replace(R.id.view_container, property_fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;

            case R.id.iv_attechments:
                f = getChildFragmentManager().findFragmentById(R.id.map);
                if (f != null) {
                    getChildFragmentManager()
                            .beginTransaction().remove(f).commit();
                }
                GlobalApplication.ATTACHMENT_LOG="addRegisterAttach";
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                AttachmentFragment attechment_fragment = new AttachmentFragment();
                fragmentTransaction.replace(R.id.view_container, attechment_fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;

            case R.id.tv_register_beacon:
                registerBeaconData();
                break;

            case R.id.tb_location:
                setTextVisibility(tb_location, tv_tap_location);
                break;

            case R.id.tb_stability:
                setTextVisibility(tb_stability, tv_tap_stability);
                break;

            case R.id.tb_desc:
                setTextVisibility(tb_desc, tv_tap_desc);
                break;

            case R.id.tb_floorlvl:
                setTextVisibility(tb_floorlvl, tv_tap_floorlvl);
                break;
        }

    }

    private void registerBeaconData() {

        String namespaceId, instanceId,bea_name;
        namespaceId = SharedPrefsUtils.getStringPreference(getActivity(), GlobalApplication.BEACON_NAMESPACE);
        instanceId = SharedPrefsUtils.getStringPreference(getActivity(), GlobalApplication.BEACON_INSTANCE);
        bea_name = SharedPrefsUtils.getStringPreference(getActivity(), GlobalApplication.BEACON_NAME);
        namespaceId = namespaceId.substring(2, namespaceId.length());
        instanceId = instanceId.substring(2, instanceId.length());
        final String uid = namespaceId + instanceId;
        base64Encode(uid);


        if(tv_tap_desc.getText().toString().equals(getString(R.string.tap_description))){
            tv_tap_desc.setText("");
        }
        if(tv_tap_stability.getText().toString().equals(getString(R.string.tap_stability))){
            tv_tap_stability.setText("");
        }
        if(tv_tap_floorlvl.getText().toString().equals(getString(R.string.tap_floorelevel))){
            tv_tap_floorlvl.setText("");
        }
        if(tv_tap_location.getText().toString().equals(getString(R.string.tap_floorelevel))){
            tv_tap_floorlvl.setText("");
        }
        tv_tap_location.setText("Coordinates: "+place_latitude+", "+place_longitude);

        if (ProximityApi.getInstance(getActivity()).hasToken()) {
            mProximityApi.registerBeacon(base64Encode(uid), tv_tap_desc.getText().toString(), tv_tap_stability.getText().toString(),
                    tv_tap_floorlvl.getText().toString(), prorety_list,String.valueOf(place_latitude),String.valueOf(place_longitude),place_id);

            if(attach_list.size()>=1) {
                for (int i = 0; i < attach_list.size(); i++) {
                    mProximityApi.createAttachment(bea_name, attach_list.get(0).attch_data, attach_list.get(0).attach_type);

                }
            }
        }else {
            Toast.makeText(bcn_reg_activity, "Your token is null", Toast.LENGTH_SHORT).show();
        }

        getActivity().onBackPressed();
    }

    public static String base64Encode(String b) {
        return base64Encode(setByteArrayValue(b));
    }

    private static String base64Encode(byte[] bytes) {
        return Base64.encodeToString(bytes, Base64.DEFAULT).trim();
    }

    private static byte[] setByteArrayValue(String hexString) {
        int len = hexString.length();
        byte[] bytes = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            bytes[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return bytes;
    }

    private void setTextVisibility(ToggleButton toggleButton, TextView textView) {
        if (toggleButton.isChecked()) {
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("=======>onPause","onPause");

       /* Fragment f = getChildFragmentManager().findFragmentById(R.id.map);
        if (f != null) {
            getChildFragmentManager()
                    .beginTransaction().remove(f).commit();
        }*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d("=======>onDestroy","onDestroy");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("=======>onDestroyView","onDestroyView");
       /*// Fragment f = getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            getChildFragmentManager()
                    .beginTransaction().remove(mapFragment).commit();
        }*/
       /* Fragment f = getActivity()
                .getSupportFragmentManager().findFragmentById(R.id.map);
        if (f != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction().remove(f).commit();
        }*/

        //resetBoolean();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(place_latitude!=0){
            LatLng sydney = new LatLng(place_latitude,place_longitude);
            mMap.addMarker(new MarkerOptions().position(sydney).title("Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(place_latitude, place_longitude), 12));
        }



        // Add a marker in Sydney and move the camera
      /*  LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
    }

  /*  private void resetRegisterFragment() {
        SharedPrefsUtils.setBooleanPreference(getActivity(), GlobalApplication.boolean_description, false);
        SharedPrefsUtils.setBooleanPreference(getActivity(), GlobalApplication.boolean_floor_lvl, false);
        SharedPrefsUtils.setBooleanPreference(getActivity(), GlobalApplication.boolean_place_id, false);
        SharedPrefsUtils.setBooleanPreference(getActivity(), GlobalApplication.boolean_stable, false);
        SharedPrefsUtils.setBooleanPreference(getActivity(), GlobalApplication.boolean_property, false);
        SharedPrefsUtils.setBooleanPreference(getActivity(), GlobalApplication.boolean_attechment, false);
        SharedPrefsUtils.setStringPreference(getActivity(), GlobalApplication.description_text, null);
        SharedPrefsUtils.setStringPreference(getActivity(), GlobalApplication.stable_text, null);
        SharedPrefsUtils.setStringPreference(getActivity(), GlobalApplication.floor_lvl_text, null);
        SharedPrefsUtils.setStringPreference(getActivity(), GlobalApplication.place_id_text, null);
        SharedPrefsUtils.setArraylist(getActivity(), GlobalApplication.property_list, null);
        SharedPrefsUtils.setArraylist(getActivity(), GlobalApplication.attach_list, null);
    }*/


}
