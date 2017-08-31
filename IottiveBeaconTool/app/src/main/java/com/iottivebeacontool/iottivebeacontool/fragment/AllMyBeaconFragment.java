package com.iottivebeacontool.iottivebeacontool.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.iottivebeacontool.iottivebeacontool.GlobalApplication;
import com.iottivebeacontool.iottivebeacontool.R;
import com.iottivebeacontool.iottivebeacontool.SharedPrefsUtils;
import com.iottivebeacontool.iottivebeacontool.Utils;
import com.iottivebeacontool.iottivebeacontool.activity.MainActivity;
import com.iottivebeacontool.iottivebeacontool.activity.AttachmentListActivity;
import com.iottivebeacontool.iottivebeacontool.adapter.AllBeaconAdapter;
import com.iottivebeacontool.iottivebeacontool.api.BeaconsLoader;
import com.iottivebeacontool.iottivebeacontool.api.ProximityApi;
import com.iottivebeacontool.iottivebeacontool.model.AllBeaconModel;

import java.util.ArrayList;
import java.util.List;

public class AllMyBeaconFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<List<AllBeaconModel>> {

    private Activity all_bcn_activity;
    private ListView lv_all_bcn;
    private AllBeaconAdapter mAdapter=null;
    private ProgressDialog all_bcn_Dialog;
    private LinearLayout ll_progrssbar;
    private ProgressBar settings_progressbar;


    private void initUi(View view){
        lv_all_bcn =  (ListView) view.findViewById(R.id.lv_all_bcn);
        ll_progrssbar = (LinearLayout) view.findViewById(R.id.ll_progrssbar);
        settings_progressbar = (ProgressBar) view.findViewById(R.id.settings_progressbar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_all_my_beacon,
                container, false);
        all_bcn_activity =getActivity();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        all_bcn_activity=getActivity();
        initUi(view);
        mAdapter = new AllBeaconAdapter(getActivity());
        lv_all_bcn.setAdapter(mAdapter);
        getActivity().getSupportLoaderManager().initLoader(0, null, this);
        ll_progrssbar.setVisibility(View.VISIBLE);


    }

    @Override
    public void onResume() {
        super.onResume();
        all_bcn_activity = getActivity();
        if(all_bcn_activity instanceof MainActivity){
            MainActivity myactivity = (MainActivity) all_bcn_activity;
            myactivity.setToolbar(getString(R.string.all_my_beacon),View.GONE);

        }
        if (ProximityApi.getInstance(getActivity()).hasToken()) {

        /*    all_bcn_Dialog =new ProgressDialog(getActivity());
            all_bcn_Dialog.setMessage("Getting List.....");
            all_bcn_Dialog.show();*/
            refreshLoader();
        }else {
            Toast.makeText(all_bcn_activity, "Your token is null", Toast.LENGTH_SHORT).show();
        }


        lv_all_bcn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EditAlertDialod(position);
                /*SharedPrefsUtils.setStringPreference(getActivity(),GlobalApplication.BEACON_NAME_ATTACH,mAdapter.getItem(position).beacon_name);
                Intent aIntent = new Intent(getActivity(), AttachmentListActivity.class);
                startActivity(aIntent);*/
            }
        });

      /*  lv_all_bcn.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Are You Sure?")
                        .setMessage("Do you want to decommission " + mAdapter.getItem(position).bea_desc + " ?")
                        .setNegativeButton("Cancel", null)
                        .setPositiveButton("decommission", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ProximityApi.getInstance(getContext()).decommissionBeacon(mAdapter.getItem(position).beacon_name);
                            }
                        })
                        .show();
                return false;
            }
        });*/
    }

    @Override
    public Loader<List<AllBeaconModel>> onCreateLoader(int id, Bundle args) {
        return new BeaconsLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<AllBeaconModel>> loader, List<AllBeaconModel> data) {
        ll_progrssbar.setVisibility(View.GONE);

        /*if(all_bcn_Dialog.isShowing()){
            all_bcn_Dialog.dismiss();
        }*/
        if (lv_all_bcn.getEmptyView() == null) {
            lv_all_bcn.setEmptyView(getActivity().findViewById(R.id.tv_empty));
        }

        if (data == null) {
            Utils.showToast(getActivity(), "Unable to load beacons");
            return;
        }
        mAdapter.setNotifyOnChange(false);
        mAdapter.clear();
        mAdapter.addAll(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<AllBeaconModel>> loader) {
        mAdapter.clear();
    }

    private void refreshLoader() {
        getActivity().getSupportLoaderManager().restartLoader(0, null, this);
    }

    private void EditAlertDialod(final int position) {

        final Dialog dialog = new Dialog(all_bcn_activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_edit_beacon);


        TextView tv_edit_attachment = (TextView) dialog.findViewById(R.id.tv_edit_attachment);
        TextView tv_deactivate_beacon = (TextView) dialog.findViewById(R.id.tv_deactivate_beacon);
        TextView tv_decommission_beacon = (TextView) dialog.findViewById(R.id.tv_decommission_beacon);

        tv_edit_attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefsUtils.setStringPreference(getActivity(),GlobalApplication.BEACON_NAME_ATTACH,mAdapter.getItem(position).beacon_name);
                Intent aIntent = new Intent(getActivity(), AttachmentListActivity.class);
                startActivity(aIntent);
                dialog.dismiss();
            }
        });
        tv_deactivate_beacon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Are You Sure?")
                        .setMessage("Do you want to deactivate " + mAdapter.getItem(position).bea_desc + " beacon?")
                        .setNegativeButton("CANCEL", null)
                        .setPositiveButton("DEACTIVATE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ProximityApi.getInstance(getContext()).deActivateBeacon(mAdapter.getItem(position).beacon_name,all_bcn_activity);
                                refreshLoader();
                            }
                        })
                        .show();
                dialog.dismiss();


            }
        });
        tv_decommission_beacon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Are You Sure?")
                        .setMessage("Do you want to decommission " + mAdapter.getItem(position).bea_desc + " ?")
                        .setNegativeButton("CANCEL", null)
                        .setPositiveButton("DECOMMISSION", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ProximityApi.getInstance(getContext()).decommissionBeacon(mAdapter.getItem(position).beacon_name,all_bcn_activity);
                                refreshLoader();
                            }
                        })
                        .show();
                dialog.dismiss();


            }
        });

        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        dialog.show();
    }

}
