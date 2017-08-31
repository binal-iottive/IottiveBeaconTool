package com.iottivebeacontool.iottivebeacontool.activity;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.location.places.Places;
import com.iottivebeacontool.iottivebeacontool.AndroidPermission;
import com.iottivebeacontool.iottivebeacontool.GlobalApplication;
import com.iottivebeacontool.iottivebeacontool.ListProjectsTask;
import com.iottivebeacontool.iottivebeacontool.R;
import com.iottivebeacontool.iottivebeacontool.SharedPrefsUtils;
import com.iottivebeacontool.iottivebeacontool.Utils;
import com.iottivebeacontool.iottivebeacontool.api.ApiDataCallback;
import com.iottivebeacontool.iottivebeacontool.api.ProximityApi;
import com.iottivebeacontool.iottivebeacontool.data.MyStaticBeacon;
import com.iottivebeacontool.iottivebeacontool.data.RequestAccessTokenTask;
import com.iottivebeacontool.iottivebeacontool.fragment.AllMyBeaconFragment;
import com.iottivebeacontool.iottivebeacontool.fragment.NearByBeaconFragment;
import com.iottivebeacontool.iottivebeacontool.fragment.ProjectListFragment;
import com.iottivebeacontool.iottivebeacontool.fragment.RegisteredFragment;
import com.iottivebeacontool.iottivebeacontool.fragment.UnregisteredFragment;
import com.iottivebeacontool.iottivebeacontool.model.NearByBeaconModel;
import com.iottivebeacontool.iottivebeacontool.model.ProjectModel;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.utils.UrlBeaconUrlCompressor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, BeaconConsumer, RangeNotifier {

    public Toolbar actionbar_toolbar;
    public  TextView tv_title;
    public ImageView iv_menu,iv_save;
    public DrawerLayout dLayout;
    public TextView tv_all_beacon,tv_nearby,tv_pic_account,tv_switch_pro,tv_projectId,tv_projectNum;

    private FrameLayout view_container;
    private  Fragment fragment;
    private android.support.v4.app.FragmentManager fragmentManager;
    private android.support.v4.app.FragmentTransaction fragmentTransaction;

    private static final int REQUEST_PICK_ACCOUNT = 42;
    private static final int REQUEST_ERROR_RECOVER = 43;
    private static final String SCOPE_PROXIMITY =
            "oauth2:https://www.googleapis.com/auth/userlocation.beacon.registry";
    private static final String TAG =
            MainActivity.class.getSimpleName();

    private BeaconManager mBeaconManager;
    public NearByBeaconModel MBean;
    public  ArrayList<NearByBeaconModel> BeaconList;
    public static ArrayList<NearByBeaconModel> bcnListnew= new ArrayList<>();
    public UnregisteredFragment rangingActivity = null;
    ProximityApi mproximityApi;

    public static final int SIGN_IN = 121;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInOptions mGso;
    private ProgressDialog progressDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        rangingActivity = new UnregisteredFragment();
        mproximityApi = ProximityApi.getInstance(MainActivity.this);
        getToolbar();
        initUi();
        getDrawer();
        getDeviceAccount();

    }

    @Override
    protected void onResume() {
        super.onResume();
        new AndroidPermission(this).requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, this);

    }
    private void getDeviceAccount(){
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("processing.....");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if(SharedPrefsUtils.getBooleanPreference(MainActivity.this,GlobalApplication.BOOLEAN_ACCOUNT_PIC)==false){
            tv_pic_account.setText(SharedPrefsUtils.getStringPreference(MainActivity.this,GlobalApplication.ACCOUNT_NAME));
            startGoogleSignin(SharedPrefsUtils.getStringPreference(MainActivity.this,GlobalApplication.ACCOUNT_NAME));
        }else {
            Pattern gmailPattern = Patterns.EMAIL_ADDRESS;
            Account[] accounts = AccountManager.get(this).getAccounts();
            for (Account account : accounts) {
                if (gmailPattern.matcher(account.name).matches()) {

                    Log.d("device_account======>",account.name);
                    tv_pic_account.setText(account.name);
                    startGoogleSignin(account.name);
                }
            }
        }

    }

    public void getToolbar() {
        actionbar_toolbar = (Toolbar) findViewById(R.id.actionbar_toolbar);
        tv_title = (TextView) actionbar_toolbar.findViewById(R.id.tv_title);
        iv_menu = (ImageView) actionbar_toolbar.findViewById(R.id.iv_menu);
        iv_save = (ImageView) actionbar_toolbar.findViewById(R.id.iv_save);
        iv_save.setVisibility(View.GONE);
        iv_menu.setOnClickListener(this);
    }

    public void setToolbar(String title, int save_visibility){
        tv_title.setText(title);
        iv_save.setVisibility(save_visibility);
    }
    public void setCurrentProject(String pro_id, String pro_num){
        tv_projectNum.setText(pro_num);
        tv_projectId.setText(pro_id);
        GlobalApplication.SELECTED_PROJECT=pro_id;
    }
    private void initUi() {
        tv_title.setText(getString(R.string.beacon_near_me));
        view_container = (FrameLayout) findViewById(R.id.view_container);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        NearByBeaconFragment fragment = new NearByBeaconFragment();
        fragmentTransaction.replace(R.id.view_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();


    }

    public void getDrawer() {
        dLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        tv_all_beacon = (TextView) findViewById(R.id.tv_all_beacon);
        tv_nearby = (TextView) findViewById(R.id.tv_nearby);
        tv_pic_account = (TextView) findViewById(R.id.tv_pic_account);
        tv_switch_pro = (TextView) findViewById(R.id.tv_switch_pro);
        tv_projectId = (TextView) findViewById(R.id.tv_projectId);
        tv_projectNum = (TextView) findViewById(R.id.tv_projectNum);

        tv_all_beacon.setOnClickListener(this);
        tv_nearby.setOnClickListener(this);
        tv_pic_account.setOnClickListener(this);
        tv_switch_pro.setOnClickListener(this);

        if(SharedPrefsUtils.getBooleanPreference(MainActivity.this, GlobalApplication.PROJECT_SELECTED)==false){
            setCurrentProject( SharedPrefsUtils.getStringPreference(MainActivity.this, GlobalApplication.CURRENT_PROJECT_ID),
            SharedPrefsUtils.getStringPreference(MainActivity.this, GlobalApplication.CURRENT_PROJECT_NUM));
        }else {
            setCurrentProject("Select a project","");
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.iv_menu:
                dLayout.openDrawer(GravityCompat.START);
                break;

            case R.id.tv_all_beacon:
                tv_title.setText(getString(R.string.all_my_beacon));
                fragment = getSupportFragmentManager().findFragmentById(R.id.view_container);
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.remove(fragment);
                fragmentTransaction.commit();

                fragmentTransaction = fragmentManager.beginTransaction();
                AllMyBeaconFragment afragment = new AllMyBeaconFragment();
                fragmentTransaction.add(R.id.view_container, afragment);
               // fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                dLayout.closeDrawer(Gravity.START);
                break;

            case R.id.tv_nearby:
                tv_title.setText(getString(R.string.beacon_near_me));
                fragment = getSupportFragmentManager().findFragmentById(R.id.view_container);
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.remove(fragment);
                fragmentTransaction.commit();

                fragmentTransaction = fragmentManager.beginTransaction();
                NearByBeaconFragment nfragment = new NearByBeaconFragment();
                fragmentTransaction.replace(R.id.view_container, nfragment);
               // fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                dLayout.closeDrawer(Gravity.START);
                break;

            case R.id.tv_switch_pro:
                tv_title.setText(getString(R.string.beacon_near_me));
          /*      fragment = getSupportFragmentManager().findFragmentById(R.id.view_container);
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.remove(fragment);
                fragmentTransaction.commit();*/

                fragmentTransaction = fragmentManager.beginTransaction();
                ProjectListFragment projectListFragment = new ProjectListFragment();
                fragmentTransaction.replace(R.id.view_container, projectListFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                dLayout.closeDrawer(Gravity.START);
                break;

            case R.id.tv_pic_account:
                pickUserAccount();
        }
    }

    private void pickUserAccount() {
        String[] accountTypes = new String[]{"com.google"};
        Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                accountTypes, true, null, null, null, null);
        startActivityForResult(intent, REQUEST_PICK_ACCOUNT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PICK_ACCOUNT) {
            // Receiving a result from the AccountPicker
            if (resultCode == RESULT_OK) {
                String email = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                tv_pic_account.setText(email);
                SharedPrefsUtils.setStringPreference(MainActivity.this,GlobalApplication.ACCOUNT_NAME,email);
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("processing.....");
                progressDialog.show();
                SharedPrefsUtils.setBooleanPreference(MainActivity.this,GlobalApplication.BOOLEAN_ACCOUNT_PIC,false);
                startGoogleSignin(email);
              /*  GetTokenTask task = new GetTokenTask();
                task.execute(email);*/
            } else if (resultCode == RESULT_CANCELED) {

                Utils.showToast(this, "You have to select an account");
            }
        }
        if (requestCode==SIGN_IN){
            Log.d("data=======>", String.valueOf(data));
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        if (requestCode == REQUEST_ERROR_RECOVER && resultCode == RESULT_OK) {

        }
    }

  @Override
    public void onBeaconServiceConnect() {
        Region region = new Region("all-beacons-region", null, null, null);

        try {
            mBeaconManager.startRangingBeaconsInRegion(region);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        mBeaconManager.addRangeNotifier((RangeNotifier) this);
    }

    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> collection, Region region) {
        for (Beacon beacon : collection) {
            Log.d("beacon======>", String.valueOf(beacon));
            Log.d("beacon======>regi", String.valueOf(collection));

            if (beacon.getServiceUuid() == 0xfeaa) {
                if (beacon.getServiceUuid() == 0xfeaa && beacon.getBeaconTypeCode() == 0x00) {

                   /* MBean = new NearByBeaconModel();
                    MBean.bea_type = "Eddystone-UID";
                    MBean.bea_namespace = String.valueOf(beacon.getId1()).substring(2);
                    MBean.bea_instance = String.valueOf(beacon.getId2()).substring(2);
                    MBean.bea_tx = String.valueOf(beacon.getTxPower());
                    MBean.bea_rssi = String.valueOf(beacon.getRssi());
                    MBean.bea_name =  Utils.toBeaconName(3,MBean.bea_namespace+MBean.bea_instance);
                    MBean.beacon_registord_or_not=null;
*/
                   String type = "Eddystone-UID";
                    String mac_address=beacon.getBluetoothAddress();
                    String namespace_main = String.valueOf(beacon.getId1()).substring(2);
                    String namespace = String.valueOf(beacon.getId1());
                    String instance_main = String.valueOf(beacon.getId2()).substring(2);
                    String instance = String.valueOf(beacon.getId2());
                    String tx = String.valueOf(beacon.getTxPower());
                    String rssi = String.valueOf(beacon.getRssi());
                    String beaconName =  Utils.toBeaconName(3,namespace_main+instance_main);


                    if(SharedPrefsUtils.getStringPreference(MainActivity.this,GlobalApplication.ACCOUNT_TOKEN)!=null) {
                        mproximityApi.getBeacon(type,namespace,instance,tx,rssi,beaconName,mac_address);
                    }

                  /*  int index = isContainBeacon(MBean, BeaconList);
                    if (index == -1) {
                        //New Record
                        BeaconList.add(MBean);
                    } else {

                        BeaconList.remove(index);
                        BeaconList.add(index, MBean);
                    }*/
                }
                if (beacon.getServiceUuid() == 0xfeaa && beacon.getBeaconTypeCode() == 0x10) {
                    // This is a Eddystone-URL frame
/*
                    MBean.bea_type = "Eddystone-URL";
                    MBean.bea_namespace = "";
                    MBean.bea_tx = String.valueOf(beacon.getTxPower());
                    MBean.bea_instance = UrlBeaconUrlCompressor.uncompress(beacon.getId1().toByteArray());
                    MBean.bea_rssi = String.valueOf(beacon.getRssi());
                    MBean.beacon_registord_or_not=null;*/

                  String type = "Eddystone-URL";
                    String mac_address=beacon.getBluetoothAddress();
                    String namespace = "";
                    String tx = String.valueOf(beacon.getTxPower());
                    String instance = UrlBeaconUrlCompressor.uncompress(beacon.getId1().toByteArray());
                    String rssi = String.valueOf(beacon.getRssi());

                    if(SharedPrefsUtils.getStringPreference(MainActivity.this,GlobalApplication.ACCOUNT_TOKEN)!=null) {
                       mproximityApi.getBeacon(type,namespace,instance,tx,rssi,"",mac_address);
                    }

                   /* int index = isContainBeacon(MBean, BeaconList);
                    if (index == -1) {
                        //New Record
                        BeaconList.add(MBean);
                    } else {

                       // BeaconList.remove(index);
                       // BeaconList.add(index, MBean);
                    }*/
                }
            } else {

               /* MBean = new NearByBeaconModel();
                MBean.bea_type = "ibeacon";
                MBean.bea_namespace =beacon.getId1().toString();
                MBean.bea_instance = (beacon.getId2().toString())+"  "+(beacon.getId3().toString());
                MBean.bea_tx = String.valueOf(beacon.getTxPower());
                MBean.bea_rssi = " " + beacon.getRssi();
                String minor =Utils.toHex((beacon.getId2().toString()));
                String major =Utils.toHex((beacon.getId3().toString()));
                String namespace = Utils.getUUid(MBean.bea_namespace);
                String beaconName=namespace+minor+major;
                MBean.bea_name =  Utils.toBeaconName(1,beaconName);*/




                String type= "ibeacon";
                String mac_address=beacon.getBluetoothAddress();
                String namespace =beacon.getId1().toString();
                String instance = (beacon.getId2().toString())+"  "+(beacon.getId3().toString());
                String tx = String.valueOf(beacon.getTxPower());
                String rssi = " " + beacon.getRssi();
                String minor =Utils.toHex((beacon.getId2().toString()));
                String major =Utils.toHex((beacon.getId3().toString()));
                String namespace_ = Utils.getUUid(beacon.getId1().toString());
                String beaconName_=namespace_+minor+major;
                String beaconName =  Utils.toBeaconName(1,beaconName_);

                if(SharedPrefsUtils.getStringPreference(MainActivity.this,GlobalApplication.ACCOUNT_TOKEN)!=null) {
                    ProximityApi.getInstance(MainActivity.this)
                            .getBeacon(type, namespace, instance, tx, rssi, beaconName,mac_address);
                }
               // int index = isContainBeacon(MBean, BeaconList);
              /*  if (index == -1) {
                    //New Record
                    BeaconList.add(MBean);
                } else {

                    BeaconList.remove(index);
                     BeaconList.add(index, MBean);
                }*/



            }

        }

        runOnUiThread(new Runnable() {
            public void run() {
                bcnListnew =BeaconList;
                Log.d("mArraylist..size====>", BeaconList.size() + "");
             //   UnregisteredFragment.onRefreshData(BeaconList);
              //  RegisteredFragment.onRefreshData(BeaconList);

            }
        });

    }
    private int isContainBeacon(NearByBeaconModel modelRanging, ArrayList<NearByBeaconModel> arrayListBeacn) {
        int index = -1;
        for (int x = 0; x < arrayListBeacn.size(); x++) {
            if (modelRanging.bea_instance.equals(arrayListBeacn.get(x).bea_instance)) {
                index = x;
                return index;
            }
        }
        return index;
    }
    private class GetTokenTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                Log.d("token==========>",fetchToken(params[0]));
                return fetchToken(params[0]);
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                ProximityApi.getInstance(MainActivity.this)
                        .setAuthToken(result);
                SharedPrefsUtils.setStringPreference(MainActivity.this,GlobalApplication.ACCOUNT_TOKEN,result);
                SharedPrefsUtils.setBooleanPreference(MainActivity.this,GlobalApplication.BOOLEAN_ACCOUNT_TOKEN,false);
               // new ListProjectsTask(MainActivity.this, GlobalApplication.GET_PROJECT, mListProjectsCallBack, result).execute();

                setResult(RESULT_OK);
               // finish();
            }
        }
    }

    private String fetchToken(String email) throws IOException {
        try {
            return GoogleAuthUtil.getToken(this, email, GlobalApplication.SCOPE_PROXIMITY_TOKEN);
        } catch (UserRecoverableAuthException e) {
            handleException(e);
        } catch (GoogleAuthException e) {
            Log.w(TAG, "Fatal Exception", e);
        }

        return null;
    }

    public void handleException(final Exception e) {
        // Because this call comes from the AsyncTask, we must ensure that the following
        // code instead executes on the UI thread.
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (e instanceof GooglePlayServicesAvailabilityException) {
                    // The Google Play services APK is old, disabled, or not present.
                    // Show a dialog created by Google Play services that allows
                    // the user to update the APK
                    int statusCode = ((GooglePlayServicesAvailabilityException) e)
                            .getConnectionStatusCode();
                    Dialog dialog = GooglePlayServicesUtil.getErrorDialog(statusCode,
                            MainActivity.this,
                            REQUEST_ERROR_RECOVER);
                    dialog.show();
                } else if (e instanceof UserRecoverableAuthException) {
                    // Unable to authenticate, such as when the user has not yet granted
                    // the app access to the account, but the user can fix this.
                    // Forward the user to an activity in Google Play services.
                    Intent intent = ((UserRecoverableAuthException) e).getIntent();
                    startActivityForResult(intent,
                            REQUEST_ERROR_RECOVER);
                }
            }
        });
    }

    public void StartBeacon() {
        BeaconList = new ArrayList<>();

        mBeaconManager = BeaconManager.getInstanceForApplication(getApplicationContext());
        // Detect the main Eddystone-UID frame:
        mBeaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT));
        // Detect the telemetry Eddystone-TLM frame:
        mBeaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(BeaconParser.EDDYSTONE_TLM_LAYOUT));

        mBeaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(BeaconParser.EDDYSTONE_URL_LAYOUT));

        mBeaconManager = BeaconManager.getInstanceForApplication(this);
        mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));

        mBeaconManager.bind(this);
    }

    public void StopBeacon() {
        if (mBeaconManager != null) {
            mBeaconManager.unbind(this);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
       // getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        //finish();
    }




   /* Callback mListProjectsCallBack = new Callback() {
        @Override
        public void onFailure(Request request, IOException e) {

        }

        @Override
        public void onResponse(Response response) throws IOException {

            try {
                Log.d("token==========>pro", String.valueOf(response));
                String body = response.body().string();
                final JSONObject jsonResponse = new JSONObject(body);
                if (!response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "not get projects", Toast.LENGTH_SHORT).show();
                }
                parseProjectsFromResponse(jsonResponse);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
ArrayList<ProjectModel> mArraylist = new ArrayList<>();
    private void parseProjectsFromResponse(JSONObject jsonResponse){
        try {

            JSONObject tempJsonObj;
            if(jsonResponse.has("projects"))
            {
                JSONArray jsonArr = jsonResponse.getJSONArray("projects");

                for (int i = 0; i < jsonArr.length(); i++) {
                    tempJsonObj = jsonArr.getJSONObject(i);
                    ProjectModel m = new ProjectModel();
                    m.project_num =tempJsonObj.getString("name");
                    m.project_id =tempJsonObj.getString("projectId");
                  mArraylist.add(m);
                }
                Log.d("ProjectList=======>", String.valueOf(mArraylist.size()));
            } else {

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
*/
    private void startGoogleSignin(String acc_name) {
        Scope scope = new Scope(GlobalApplication.SCOPE_CLOUD_PLATFORM/*"https://www.googleapis.com/auth/cloud-platform"*/);
        Scope scope1 = new Scope(GlobalApplication.SCOPE_PROXIMITY_TOKEN/*"https://www.googleapis.com/auth/userlocation.beacon.registry"*/);
        mGso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .setAccountName(acc_name)
                .requestScopes(scope, scope1)
                .requestServerAuthCode(getString(R.string.web_client_id))
                .build();


        mGoogleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, mGso)
                .build();

        mGoogleApiClient.connect();

        signIn();
    }
    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, SIGN_IN);
    }
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("result=====>", String.valueOf(result));

        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.d("Result=======>", "handleSignInResult:" + acct.getEmail() + " " + acct.getDisplayName());
            Log.d("Result=======>acc",acct.getServerAuthCode());
            //  Log.d("Result=======>acc",acct.getIdToken());
            new RequestAccessTokenTask(MainActivity.this,  mRequestAccessTokenCallback, "https://www.googleapis.com/oauth2/v4/token", getString(R.string.web_client_id),getString(R.string.web_client_secret), acct.getServerAuthCode()).execute();

        } else {
            Toast.makeText(MainActivity.this, "not get responce", Toast.LENGTH_LONG).show();
        }
    }

    Callback mRequestAccessTokenCallback = new Callback() {
        @Override
        public void onFailure(Request request, IOException e) {

        }

        @Override
        public void onResponse(Response response) throws IOException {

            try {
                String body = response.body().string();
                final JSONObject jsonResponse = new JSONObject(body);
                if (!response.isSuccessful()) {
                } else {
                    final String access_token = jsonResponse.getString("access_token");
                   // listProjectsFromConsole(access_token);
                    ProximityApi.getInstance(MainActivity.this)
                            .setAuthToken(access_token);
                    Log.d("Result=======>token",access_token);
                   // listProjectsFromConsole(access_token);
                    SharedPrefsUtils.setStringPreference(MainActivity.this,GlobalApplication.ACCOUNT_TOKEN,access_token);
                    SharedPrefsUtils.setBooleanPreference(MainActivity.this,GlobalApplication.BOOLEAN_ACCOUNT_TOKEN,false);
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }

                    if(jsonResponse.has("refresh_token")) {
                        final String refresh_token = jsonResponse.getString("refresh_token");
                      /*  ProximityApi.getInstance(MainActivity.this)
                                .setAuthToken(refresh_token);*/
                        Log.d("Result=======>token",refresh_token);

                      //  listProjectsFromConsole(refresh_token);

                    } else {

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };


}
