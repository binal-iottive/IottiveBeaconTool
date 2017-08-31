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

/**
 * Created by iottive on 7/20/17.
 */

public class RegisteredFragment extends Fragment implements ApiDataCallback {
    private Activity mActivity;
    private static ListView lv_registeredList;
    static NearByBeaconAdapter nAdapter;
    private static ArrayList<NearByBeaconModel> nArraylist;
    NearByBeaconModel MBean;


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
        View view = inflater.inflate(R.layout.register_list,
                container, false);
        initUi(view);
        //  mActivity.startService(new Intent(mActivity, BeaconScanService.class));
        lv_registeredList = (ListView) view.findViewById(R.id.lv_registeredList);
        nArraylist = new ArrayList<>();

        /* MBean = new NearByBeaconModel();
        MBean.bea_type = "ibeacon";
        MBean.bea_namespace ="hgfb";
        MBean.bea_instance = "ydtfg";
        MBean.bea_tx = "dgv";
        MBean.bea_rssi = "hgfvh";
        nArraylist.add(MBean);*/

        nAdapter = new NearByBeaconAdapter(mActivity/*, nArraylist*/,1);
        lv_registeredList.setAdapter(nAdapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onResume() {
        super.onResume();
        ProximityApi.getInstance(getActivity()).registerDataCallback(this);
        ((MainActivity) getActivity()).StartBeacon();
        //resetRegisterFragment();
        //  nAdapter.ListUpdate(MainActivity.bcnListnew);
        // nAdapter.notifyDataSetChanged();

    }
    @Override
    public void onPause() {
        super.onPause();
        ((MainActivity) getActivity()).StopBeacon();
        ProximityApi.getInstance(getActivity()).registerDataCallback(this);
    }


    private void initUi(View view) {
        lv_registeredList = (ListView) view.findViewById(R.id.lv_unregisterdList);
    }
    private void resetRegisterFragment() {
        SharedPrefsUtils.setBooleanPreference(getActivity(), GlobalApplication.boolean_description, false);
        SharedPrefsUtils.setBooleanPreference(getActivity(), GlobalApplication.boolean_floor_lvl, false);
        SharedPrefsUtils.setBooleanPreference(getActivity(), GlobalApplication.boolean_place_id, false);
        SharedPrefsUtils.setBooleanPreference(getActivity(), GlobalApplication.boolean_stable, false);
        SharedPrefsUtils.setBooleanPreference(getActivity(), GlobalApplication.boolean_property, false);
        SharedPrefsUtils.setBooleanPreference(getActivity(), GlobalApplication.boolean_attechment, false);
        SharedPrefsUtils.setStringPreference(getActivity(), GlobalApplication.description_text,null);
        SharedPrefsUtils.setStringPreference(getActivity(), GlobalApplication.stable_text,null);
        SharedPrefsUtils.setStringPreference(getActivity(), GlobalApplication.floor_lvl_text,null);
        SharedPrefsUtils.setStringPreference(getActivity(), GlobalApplication.place_id_text,null);
        SharedPrefsUtils.setArraylist(getActivity(), GlobalApplication.property_list,null);
        SharedPrefsUtils.setArraylist(getActivity(), GlobalApplication.attach_list,null);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public static void onRefreshData(final ArrayList<NearByBeaconModel> bcnList) {
        Log.d("onRefreshDatam Size :", bcnList.size() + "");
        ListAdapter listAdapter = lv_registeredList.getAdapter();
        if (listAdapter == null){
            nArraylist = bcnList;

            lv_registeredList.setAdapter(nAdapter);
        }
        else {
            nArraylist =bcnList;

            nAdapter.ListUpdate(bcnList);
            nAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBeaconResponse(NearByBeaconModel beacon) {
        nAdapter.addDiscoveredBeacon(beacon);
    }

    @Override
    public void onAttachmentResponse() {

    }
}
