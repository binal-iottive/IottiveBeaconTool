package com.iottivebeacontool.iottivebeacontool.api;


import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.iottivebeacontool.iottivebeacontool.data.Attachment;
import com.iottivebeacontool.iottivebeacontool.data.MyStaticBeacon;
import com.iottivebeacontool.iottivebeacontool.model.AddPropertyModel;
import com.iottivebeacontool.iottivebeacontool.model.Attachmentmodel;
import com.iottivebeacontool.iottivebeacontool.model.NearByBeaconModel;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;

import static com.iottivebeacontool.iottivebeacontool.GlobalApplication.BEACON_ROOT;
import static com.iottivebeacontool.iottivebeacontool.GlobalApplication.SELECTED_PROJECT;

public class ProximityApi {
    private static final String TAG = ProximityApi.class.getSimpleName();

    private static ProximityApi sInstance;
    public static synchronized ProximityApi getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ProximityApi(context.getApplicationContext());
        }

        return sInstance;
    }



    private RequestQueue mRequestQueue;
    private HashSet<ApiDataCallback> mCallbacks;
    private String mAuthToken;

    private ProximityApi(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
        mCallbacks = new HashSet<>();
    }

    public void setAuthToken(String token) {
        mAuthToken = token;
    }

    public boolean hasToken() {
        return !TextUtils.isEmpty(mAuthToken);
    }

    /* Callback Handlers */

    public boolean registerDataCallback(ApiDataCallback callback) {
        return mCallbacks.add(callback);
    }

    public boolean unregisterDataCallback(ApiDataCallback callback) {
        return mCallbacks.remove(callback);
    }

    //Callback to receive beacon register/get responses
    private class DecommiconDataCallback implements Response.Listener<JSONObject>,Response.ErrorListener{
        private int i;
        private Activity all_bcn_activity;
        public DecommiconDataCallback(int i, Activity all_bcn_activity) {
            this.i=i;
            this.all_bcn_activity=all_bcn_activity;
        }
        @Override
        public void onErrorResponse(VolleyError error) {
            if (error.networkResponse == null) {
                Log.d("DecommiconResponce====>", String.valueOf(error));

            }else {
                Log.d("DecommiconResponce====>", String.valueOf(error));

            }

        }

        @Override
        public void onResponse(JSONObject response) {
            Log.d("DecommiconResponce====>", String.valueOf(response));
            if(i==0){
                Toast.makeText(all_bcn_activity, "Deactivated Successfully", Toast.LENGTH_SHORT).show();
            }
            if(i==1){
                Toast.makeText(all_bcn_activity, "Decommissioned Successfully", Toast.LENGTH_SHORT).show();
            }


        }
    }
    private class BeaconDataCallback implements
            Response.Listener<JSONObject>, Response.ErrorListener {
        private String name,type,namespace,instance,tx,rssi,mac_address;
        public BeaconDataCallback(String type, String namespace, String instance, String tx, String rssi, String name, String mac_address) {
            this.name = name;
            this.type = type;
            this.namespace = namespace;
            this.instance = instance;
            this.tx = tx;
            this.rssi = rssi;
            this.mac_address = mac_address;
        }

        @Override
        public void onResponse(JSONObject response) {
            //Called for 2xx responses
            Log.d("MyStaticBeacon=====>", String.valueOf(response));
             String status = String.valueOf(MyStaticBeacon.Status.valueOf(response.optString("status")));
            NearByBeaconModel beacon = new NearByBeaconModel(status,type,namespace,instance,tx,rssi,name,mac_address);
            notifyListeners(beacon);
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            if (error.networkResponse == null) {
                NearByBeaconModel beacon = new NearByBeaconModel(String.valueOf(MyStaticBeacon.Status.STATUS_UNSPECIFIED),type,namespace,instance,tx,rssi,name,mac_address);
                notifyListeners(beacon);
                Log.w(TAG, "Unknown error response from Proximity API", error);
                return;
            }

            //Called for 4xx responses
            switch (error.networkResponse.statusCode) {
                case 403:
                    notifyListeners(new NearByBeaconModel(String.valueOf(MyStaticBeacon.Status.UNAUTHORIZED),type,namespace,instance,tx,rssi,name,mac_address));
                    break;
                case 404:
                    notifyListeners(new NearByBeaconModel(String.valueOf(MyStaticBeacon.Status.UNREGISTERED),type,namespace,instance,tx,rssi,name,mac_address));
                    break;
                default:
                    Log.w(TAG, "Unknown error response from Proximity API", error);
            }
        }

        private void notifyListeners(NearByBeaconModel result) {
            for (ApiDataCallback callback : mCallbacks) {
                 callback.onBeaconResponse(result);
            }
        }
    }

    //Callback to receive attachment create/delete responses
    private class AttachmentDataCallback implements
            Response.Listener<JSONObject>, Response.ErrorListener {
        @Override
        public void onResponse(JSONObject response) {
            Log.v(TAG, "Attachment Request Completed");
            notifyListeners();
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            Log.w(TAG, "Attachment API Error", error);
            notifyListeners();
        }

        private void notifyListeners() {
            for (ApiDataCallback callback : mCallbacks) {
                callback.onAttachmentResponse();
            }
        }
    }

    /* API Endpoint Methods */

    /**
     * namespaces.list
     * Return a list of usable project namespaces for attachments
     */
    public void getNamespaces(Response.Listener<JSONObject> listener,
                               Response.ErrorListener errorListener) {
        final String endpoint = String.format("%s/namespaces", BEACON_ROOT);
        Log.v(TAG, "Getting namespaces: " + endpoint);
        performGetRequest(endpoint, listener, errorListener);
    }


    public void registerBeacon(String adv_id, String desc, String stability, String floor_lvl, ArrayList<AddPropertyModel> prorety_list, String place_lat, String place_long, String place_id) {
        BeaconDataCallback callback = new BeaconDataCallback(adv_id, null, null, null, null, null,null);
        final String endpoint;
        if(SELECTED_PROJECT.equals("")){
              endpoint = String.format("%s/beacons:register", BEACON_ROOT);
        }else {
              endpoint = String.format("%s/beacons:register?projectId="+SELECTED_PROJECT, BEACON_ROOT);
        }

        Log.v(TAG, "Creating beacon: " + endpoint);

        try {
            performPostRequest(endpoint, toJson(adv_id,desc,stability,floor_lvl,prorety_list,place_lat,place_long,place_id), callback, callback);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public JSONObject toJson(String adv_id, String desc, String stability, String floor_lvl, ArrayList<AddPropertyModel> prorety_list, String place_lat, String place_long, String place_id) throws JSONException {

        JSONObject object = new JSONObject();

        object.put("status", "ACTIVE");
        if (!desc.equals("")) {
            object.put("description", desc);
        }

        if (!stability.equals("")) {
            object.put("expectedStability",stability);
        }

        if (!floor_lvl.equals("")) {
            JSONObject indoor_lvl_obj = new JSONObject();
            indoor_lvl_obj.put("name",floor_lvl);
            object.put("indoorLevel", indoor_lvl_obj);
        }


        JSONObject advertise_obj = new JSONObject();
        advertise_obj.put("type", "EDDYSTONE");
        advertise_obj.put("id", adv_id);
        object.put("advertisedId", advertise_obj);

        if (!place_id.equals("")) {
            object.put("placeId",place_id);
        }


        if (!place_lat.equals("0")) {
            JSONObject latLngobj = new JSONObject();
            try {
                latLngobj.put("latitude",place_lat);
                latLngobj.put("longitude",place_long);
            } catch (JSONException e) {}
            object.put("latLng",latLngobj);
        }


       /* if (prorety_list.size()>=1) {
            JSONObject pro_obj = new JSONObject();
            for (int p=0;p<prorety_list.size();p++) {
                pro_obj.put("type", prorety_list.get(p).pro_name);
                pro_obj.put("location", prorety_list.get(p).pro_value);
            }
            object.put("properties", pro_obj);*/
        if (prorety_list.size()>=1) {
            JSONObject pro_obj = new JSONObject();
            pro_obj.put("type", prorety_list.get(0).pro_name);
            pro_obj.put("location", prorety_list.get(0).pro_value);
            object.put("properties", pro_obj);
        }


        /*JSONArray property_array = new JSONArray();
        JSONObject property_obj ;
        for(int i=0;i<prorety_list.size();i++){
            property_obj = new JSONObject();
            property_obj.put("type",prorety_list.get(i).pro_name);
            property_obj.put("location",prorety_list.get(i).pro_value);
            property_array.put(i,property_obj);

        }
        object.put("properties", property_array);
*/
        /*JSONArray attechment_arry = new JSONArray();
        JSONObject attech_obj ;
        for(int i=0;i<attach_list.size();i++){
            attech_obj = new JSONObject();
            attech_obj.put("data",attach_list.get(i).getAttch_data());
            attech_obj.put("namespacedType",attach_list.get(i).getAttach_type());
            attechment_arry.put(i,attech_obj);

        }
        object.put("attachments", attechment_arry);*/


        return object;
    }

    /**
     * beacons.get
     * Return beacon resource matching the given name
     * @param type
     * @param namespace
     * @param instance
     * @param tx
     * @param rssi
     * @param beaconId
     */
    public void getBeacon(String type, String namespace, String instance, String tx, String rssi, String beaconId, String mac_address) {
        BeaconDataCallback callback = new BeaconDataCallback(type,namespace,instance,tx,rssi,beaconId,mac_address);
            final String endpoint = String.format("%s/%s?projectId="+SELECTED_PROJECT, BEACON_ROOT,
                    beaconId);
            Log.v(TAG, "Getting beacon: " + endpoint);
            performGetRequest(endpoint, callback, callback);


    }

    /**
     * beacons.list
     * Return a list of all bea cons in this API project
     */
    public void getBeaconsList(Response.Listener<JSONObject> listener,
                               Response.ErrorListener errorListener) {
        final String endpoint;
       // endpoint = String.format("%s/beacons", BEACON_ROOT);

        if(SELECTED_PROJECT.equals("")){
              endpoint = String.format("%s/beacons", BEACON_ROOT);
        }else {
              endpoint = String.format("%s/beacons?projectId="+SELECTED_PROJECT, BEACON_ROOT);
        }
        Log.v(TAG, "Getting beacons: " + endpoint);
        performGetRequest(endpoint, listener, errorListener);
    }
  /*  public void getBeaconsList(Response.Listener<JSONObject> listener,
                               Response.ErrorListener errorListener) {
        final String endpoint = String.format("%s/beacons?projectId=iottivebeacontool", BEACON_ROOT);
        Log.v(TAG, "Getting beacons: " + endpoint);
        performGetRequest(endpoint, listener, errorListener);
    }*/

    /**
     * beacons.attachments.list
     * Return a list of attachments for the given beacon name
     */
    public void getAttachmentsList(String beaconName,
                                   Response.Listener<JSONObject> listener,
                                   Response.ErrorListener errorListener) {

        final String endpoint;
        if(SELECTED_PROJECT.equals("")){
            endpoint = String.format("%s/%s/attachments", BEACON_ROOT, beaconName);
        }else {
            endpoint = String.format("%s/%s/attachments?projectId="+SELECTED_PROJECT, BEACON_ROOT, beaconName);
        }

        Log.v(TAG, "Getting attachments: " + endpoint);
        performGetRequest(endpoint, listener, errorListener);
    }

    /**
     * beacons.attachments.create
     * Post a new attachment to the given beacon name
     */
    public void createAttachment(String beaconName, String data,
                                 String namespacedType) {
        AttachmentDataCallback callback = new AttachmentDataCallback();
        Attachment toCreate = new Attachment(data, namespacedType);

        final String endpoint;
        if(SELECTED_PROJECT.equals("")){
            endpoint = String.format("%s/%s/attachments", BEACON_ROOT, beaconName);
        }else {
            endpoint = String.format("%s/%s/attachments?projectId="+SELECTED_PROJECT, BEACON_ROOT, beaconName);
        }
        Log.v(TAG, "Creating attachment: " + endpoint);
        try {
            performPostRequest(endpoint, toCreate.toJson(), callback, callback);
        } catch (JSONException e) {
            Log.w(TAG, "Unable to create attachment object");
        }
    }

    /**
     * beacons.attachments.delete
     * Delete the data matching the given attachment name
     * @param attachment
     */
    public void deleteAttachment(Attachmentmodel attachment) {
        AttachmentDataCallback callback = new AttachmentDataCallback();
        //Attachment name contains beacon name
        final String endpoint;
        if(SELECTED_PROJECT.equals("")){
            endpoint = String.format("%s/%s", BEACON_ROOT, attachment.attch_name_list);
        }else {
            endpoint = String.format("%s/%s?projectId="+SELECTED_PROJECT, BEACON_ROOT, attachment.attch_name_list);
        }
        Log.v(TAG, "Deleting attachment: " + endpoint);
        performDeleteRequest(endpoint, callback, callback);
    }


    public void decommissionBeacon(String beaconName, Activity all_bcn_activity) {
        DecommiconDataCallback decommiconDataCallback = new DecommiconDataCallback(1,all_bcn_activity);
        final String endpoint = String.format("%s/"+beaconName + ":decommission?projectId="+SELECTED_PROJECT, BEACON_ROOT);
        performPostRequest(endpoint, null, decommiconDataCallback, decommiconDataCallback);

    }
    public void deActivateBeacon(String beaconName, Activity all_bcn_activity) {
        DecommiconDataCallback decommiconDataCallback = new DecommiconDataCallback(0, all_bcn_activity);
        final String endpoint = String.format("%s/"+beaconName + ":deactivate?projectId="+SELECTED_PROJECT, BEACON_ROOT);
        performPostRequest(endpoint, null, decommiconDataCallback, decommiconDataCallback);

    }

    /* Base Request Handler Methods */

    private void performPostRequest(String endpoint, JSONObject body,
                                    Response.Listener<JSONObject> listener,
                                    Response.ErrorListener errorListener) {
        AuthenticatedRequest request = new AuthenticatedRequest(
                Request.Method.POST, endpoint, body, listener, errorListener);
        request.setAuthToken(mAuthToken);
        mRequestQueue.add(request);
    }

    private void performDeleteRequest(String endpoint,
                                      Response.Listener<JSONObject> listener,
                                      Response.ErrorListener errorListener) {
        AuthenticatedRequest request = new AuthenticatedRequest(
                Request.Method.DELETE, endpoint, null, listener, errorListener);
        request.setAuthToken(mAuthToken);
        mRequestQueue.add(request);
    }

    private void performGetRequest(String endpoint,
                                   Response.Listener<JSONObject> listener,
                                   Response.ErrorListener errorListener) {
        AuthenticatedRequest request = new AuthenticatedRequest(
                Request.Method.GET, endpoint, null, listener, errorListener);
        request.setAuthToken(mAuthToken);
        request.setShouldCache(false);
        mRequestQueue.add(request);
    }

}
