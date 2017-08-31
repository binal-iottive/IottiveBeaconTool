package com.iottivebeacontool.iottivebeacontool.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;


import com.iottivebeacontool.iottivebeacontool.GlobalApplication;
import com.iottivebeacontool.iottivebeacontool.R;
import com.iottivebeacontool.iottivebeacontool.SharedPrefsUtils;
import com.iottivebeacontool.iottivebeacontool.activity.BeaconRegisterActivity;
import com.iottivebeacontool.iottivebeacontool.activity.MainActivity;
import com.iottivebeacontool.iottivebeacontool.adapter.NearByBeaconAdapter;
import com.iottivebeacontool.iottivebeacontool.api.ApiDataCallback;
import com.iottivebeacontool.iottivebeacontool.api.ProximityApi;
import com.iottivebeacontool.iottivebeacontool.model.NearByBeaconModel;

import java.util.ArrayList;

import static com.iottivebeacontool.iottivebeacontool.activity.BeaconRegisterActivity.place_latitude;

/**
 * Created by iottive on 7/20/17.
 */

public class UnregisteredFragment extends Fragment implements ApiDataCallback {
    private Activity mActivity;
    private ListView lv_unregisterdList;
    NearByBeaconAdapter nAdapter;
    private ArrayList<NearByBeaconModel> nArraylist;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (AppCompatActivity) activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.unregisterd_list, container, false);
        initUi(view);
        lv_unregisterdList = (ListView) view.findViewById(R.id.lv_unregisterdList);
        nArraylist = new ArrayList<>();
        nAdapter = new NearByBeaconAdapter(mActivity, 0);
        lv_unregisterdList.setAdapter(nAdapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lv_unregisterdList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                NearByBeaconModel selected = (NearByBeaconModel) nAdapter.getItem(position);
                SharedPrefsUtils.setStringPreference(getActivity(), GlobalApplication.BEACON_NAMESPACE, selected.bea_namespace);
                SharedPrefsUtils.setStringPreference(getActivity(), GlobalApplication.BEACON_INSTANCE, selected.bea_instance);
                SharedPrefsUtils.setStringPreference(getActivity(), GlobalApplication.BEACON_NAME, selected.bea_name);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                BeaconRegisterActivity fragment = new BeaconRegisterActivity();
                fragmentTransaction.replace(R.id.view_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        resetRegisterFragment();

        ProximityApi.getInstance(getActivity()).registerDataCallback(this);
        ((MainActivity) getActivity()).StartBeacon();
    }

    public void startScanningBeacon(){
        ProximityApi.getInstance(getActivity()).registerDataCallback(this);
    }
    private void initUi(View view) {
        lv_unregisterdList = (ListView) view.findViewById(R.id.lv_unregisterdList);
    }

    private void resetRegisterFragment() {
        place_latitude=0;
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
    }

    @Override
    public void onPause() {
        super.onPause();
        ((MainActivity) getActivity()).StopBeacon();
        ProximityApi.getInstance(getActivity()).unregisterDataCallback(this);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onBeaconResponse(NearByBeaconModel beacon) {
        nAdapter.addDiscoveredBeacon(beacon);
    }

    @Override
    public void onAttachmentResponse() {

    }
}
