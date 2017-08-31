package com.iottivebeacontool.iottivebeacontool.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iottivebeacontool.iottivebeacontool.GlobalApplication;
import com.iottivebeacontool.iottivebeacontool.R;
import com.iottivebeacontool.iottivebeacontool.SharedPrefsUtils;
import com.iottivebeacontool.iottivebeacontool.Utils;
import com.iottivebeacontool.iottivebeacontool.adapter.AttachmentAdapter;
import com.iottivebeacontool.iottivebeacontool.api.ApiDataCallback;
import com.iottivebeacontool.iottivebeacontool.api.AttachmentsLoader;
import com.iottivebeacontool.iottivebeacontool.api.ProximityApi;
import com.iottivebeacontool.iottivebeacontool.fragment.AttachmentFragment;
import com.iottivebeacontool.iottivebeacontool.model.Attachmentmodel;
import com.iottivebeacontool.iottivebeacontool.model.NearByBeaconModel;

import java.util.List;

public class AttachmentListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Attachmentmodel>>,
        AdapterView.OnItemClickListener,
        ApiDataCallback {

    private ListView lv_beacon_AttachmentList;
    private String BraconName;
    private AttachmentAdapter attachmentAdapter;
    private ProximityApi mProximityApi;
    private TextView tv_beacon_name;
    private ImageView iv_addAttachment;
    private RelativeLayout rl_fragment;
    private ProgressDialog pDialog;
    private LinearLayout ll_progrssbar;
    private ProgressBar settings_progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attachment_list);

        initAttachUi();
        pDialog =new ProgressDialog(this);
        pDialog.setMessage("Getting List.....");
        pDialog.show();
    }

    private void initAttachUi() {
        lv_beacon_AttachmentList = (ListView) findViewById(R.id.lv_beacon_AttachmentList);
        tv_beacon_name = (TextView) findViewById(R.id.tv_beacon_name);
        iv_addAttachment = (ImageView) findViewById(R.id.iv_addAttachment);
        rl_fragment = (RelativeLayout) findViewById(R.id.rl_fragment);
        ll_progrssbar = (LinearLayout) findViewById(R.id.ll_progrssbar);
        settings_progressbar = (ProgressBar) findViewById(R.id.settings_progressbar);

        BraconName = SharedPrefsUtils.getStringPreference(this, GlobalApplication.BEACON_NAME_ATTACH);
        tv_beacon_name.setText(BraconName);
        attachmentAdapter = new AttachmentAdapter(this);
        lv_beacon_AttachmentList.setAdapter(attachmentAdapter);
        lv_beacon_AttachmentList.setOnItemClickListener(this);

        mProximityApi = ProximityApi.getInstance(this);

        getSupportLoaderManager().initLoader(0, getIntent().getExtras(), this);

        iv_addAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_fragment.setVisibility(View.VISIBLE);
                GlobalApplication.ATTACHMENT_LOG = "addEditAttach";
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                AttachmentFragment edit_beacon = new AttachmentFragment();
                fragmentTransaction.replace(R.id.rl_fragment, edit_beacon);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshLoader();
        mProximityApi.registerDataCallback(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mProximityApi.unregisterDataCallback(this);
    }

    @Override
    public Loader<List<Attachmentmodel>> onCreateLoader(int id, Bundle args) {
        if (ProximityApi.getInstance(AttachmentListActivity.this).hasToken()) {
            return new AttachmentsLoader(this, BraconName);
        }else {
            Toast.makeText(AttachmentListActivity.this, "Your token is null", Toast.LENGTH_SHORT).show();
            return null;
        }

    }

    @Override
    public void onLoadFinished(Loader<List<Attachmentmodel>> loader, List<Attachmentmodel> data) {
        ll_progrssbar.setVisibility(View.GONE);

        if (lv_beacon_AttachmentList.getEmptyView() == null) {
            lv_beacon_AttachmentList.setEmptyView(this.findViewById(R.id.empty));
        }
        if (data == null) {
            Utils.showToast(this, "Unable to load attachments");
            return;
        }
        if(pDialog.isShowing()){
            pDialog.dismiss();
        }
        attachmentAdapter.setNotifyOnChange(false);
        attachmentAdapter.clear();
        attachmentAdapter.addAll(data);
        attachmentAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<Attachmentmodel>> loader) {
        attachmentAdapter.clear();
    }

    private void refreshLoader() {
        getSupportLoaderManager().restartLoader(0, getIntent().getExtras(), this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onBeaconResponse(NearByBeaconModel beacon) {
        refreshLoader();
    }

    @Override
    public void onAttachmentResponse() {
        refreshLoader();
    }

    public void SetRlVisibility() {
        ll_progrssbar.setVisibility(View.VISIBLE);
        rl_fragment.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        rl_fragment.setVisibility(View.GONE);
    }
}
