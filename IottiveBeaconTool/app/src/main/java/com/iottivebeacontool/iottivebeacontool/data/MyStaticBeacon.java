package com.iottivebeacontool.iottivebeacontool.data;


import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.iottivebeacontool.iottivebeacontool.Utils;
import com.iottivebeacontool.iottivebeacontool.model.AllBeaconModel;
import com.iottivebeacontool.iottivebeacontool.model.Attachmentmodel;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Model representation of a beacons resource
 */
public class MyStaticBeacon {

    // Filter to ensure we only get Eddystone-UID advertisements
    public static final byte[] FRAME_FILTER = {
            0x00, //Frame type
            0x00, //TX power
            0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00
    };

    // Force frame type only to match
    public static final byte[] FILTER_MASK = {
            (byte)0xFF,
            0x00,
            0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00
    };

    public static final List<AllBeaconModel> fromJson(JSONObject object) throws JSONException {
        ArrayList<AllBeaconModel> allBeaconModelArrayList = new ArrayList<>();

        JSONArray beaconList = object.optJSONArray("beacons");
        if (beaconList != null) {
            for (int i = 0; i < beaconList.length(); i++) {
                JSONObject jsonObject =beaconList.getJSONObject(i);

                AllBeaconModel allBeaconModel= new AllBeaconModel();
                allBeaconModel.beacon_name = jsonObject.optString("beaconName");
                allBeaconModel.bea_state = jsonObject.optString("status");
               // allBeaconModel.bea_stability = jsonObject.optString("expectedStability");
                allBeaconModel.bea_desc= jsonObject.optString("description");
               // allBeaconModel.bea_placeid= jsonObject.optString("placeId");

                JSONObject jsonObject1 = jsonObject.optJSONObject("advertisedId");
                allBeaconModel.bea_type = String.valueOf(AdvertisedId.Type.valueOf(jsonObject1.optString("type")));
                if (allBeaconModel.bea_type.equals("EDDYSTONE")) {
                    String mainidString = getIdFromAdvertise(jsonObject1.optString("id"));
                    mainidString.substring(0, 20);
                    allBeaconModel.bea_namespace = mainidString.substring(0, 20);
                    allBeaconModel.bea_instance = mainidString.substring(20, mainidString.length());
                }
                if (allBeaconModel.bea_type.equals("IBEACON")) {
                    String mainidString = getIdFromAdvertise(jsonObject1.optString("id"));
                    mainidString.substring(1, 30);
                    allBeaconModel.bea_namespace = mainidString.substring(0, 32);
                    allBeaconModel.bea_instance =  Integer.valueOf(mainidString.substring(32, 36), 16).intValue()+"   "+Integer.valueOf(mainidString.substring(36, mainidString.length()), 16).intValue();
                }

               /* JSONObject indoor_obj=jsonObject.getJSONObject("indoorLevel");
                allBeaconModel.bea_indoorlvl = indoor_obj.optString("name");

                JSONObject latlong_obj=jsonObject.optJSONObject("latLng");
                allBeaconModel.bea_lat = latlong_obj.optString("latitude");
                allBeaconModel.bea_long = latlong_obj.optString("longitude");*/

                allBeaconModelArrayList.add(allBeaconModel);
            }
        }

        return allBeaconModelArrayList;
    }
    public static List<Attachmentmodel> AttchfromJson(JSONObject object) throws JSONException {
        ArrayList<Attachmentmodel> items = new ArrayList<>();

        JSONArray attachmentList = object.optJSONArray("attachments");
        Log.d("attachmentList====>", String.valueOf(attachmentList));
        if (attachmentList != null) {
            for (int i = 0; i < attachmentList.length(); i++) {
                JSONObject jsonObject =attachmentList.getJSONObject(i);
                Attachmentmodel attach_model= new Attachmentmodel();
                attach_model.attch_name_list = jsonObject.optString("attachmentName");
                attach_model.attach_namespaceType_list = jsonObject.optString("namespacedType");
                byte[] data = Base64.decode(jsonObject.optString("data"), Base64.DEFAULT);
                try {
                    String text = new String(data, "UTF-8");
                    attach_model.attach_data_list = text;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                items.add(attach_model);
            }
        }

        return items;
    }
    private static String getIdFromAdvertise(String id) {
        byte[] rawId = Base64.decode(id, Base64.NO_WRAP);
        return Utils.toHexString(rawId, 0, rawId.length);
    }

    public enum Status {
        STATUS_UNSPECIFIED,
        ACTIVE,
        DECOMMISSIONED,
        INACTIVE,
        //These two statuses are used internally
        UNAUTHORIZED,
        UNREGISTERED
    }

    public String name;
    public  AdvertisedId advertisedId;
    public  String description;
    public  Status status;

  /*  public MyStaticBeacon(String status, String type, String namespace, String instance, String tx, String rssi) {
        NearByBeaconModel nearByBeaconModel = new NearByBeaconModel();
        nearByBeaconModel.bea_type=type;
        nearByBeaconModel.bea_namespace=namespace;
        nearByBeaconModel.bea_instance=instance;
        nearByBeaconModel.bea_tx=tx;
        nearByBeaconModel.bea_rssi=rssi;
        nearByBeaconModel.bea_status= status;
       *//* this.name = object.optString("beaconName");
        this.status = Status.valueOf(object.optString("status"));
        this.advertisedId = new AdvertisedId(object.optJSONObject("advertisedId"));
        //If no description, use the beaconId from the name
        String description = object.optString("description");
        this.description = TextUtils.isEmpty(description) ?
                name.substring(name.indexOf('!')+1, name.length()) : description;*//*
    }*/

    public MyStaticBeacon(AdvertisedId advertisedId, Status status) {
        this(advertisedId, status, null);
    }

    public MyStaticBeacon(AdvertisedId advertisedId, Status status, String description) {
        this.name = null;
        this.status = status;
        this.advertisedId = advertisedId;
        this.description = description;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject object = new JSONObject();
        if (!TextUtils.isEmpty(this.name)) {
            object.put("beaconName", this.name);
        }
        object.put("advertisedId", this.advertisedId.toJson());
        object.put("placeId","ChIJbeMInlSbXjkRIr5y4ctGM7o");
        JSONObject latLngobj = new JSONObject();
        try {
            latLngobj.put("latitude","23.050817");
            latLngobj.put("longitude","72.518649");
        } catch (JSONException e) {}
        object.put("latLng",latLngobj);
        object.put("status", this.status.toString());
        if (!TextUtils.isEmpty(this.description)) {
            object.put("description", this.description);
        }


        return object;
    }

    @Override
    public boolean equals(Object object) {
        return (object instanceof MyStaticBeacon
                && ((MyStaticBeacon) object).advertisedId.id.equals(this.advertisedId.id));
    }

    /* Inner Model for nested AdvertisedId objects */
    public static class AdvertisedId {

        public static AdvertisedId fromAdvertisement(byte[] advertisement) {
            //Parse out the last 16 bytes for the Eddystone id
            int packetLength = 16;
            int offset = advertisement.length - packetLength;
            String id = Base64.encodeToString(advertisement, offset, packetLength, Base64.NO_WRAP);

            return new AdvertisedId(Type.EDDYSTONE, id);
        }

        public enum Type {
            EDDYSTONE(3),
            IBEACON(1),
            ALTBEACON(5);

            private final int mCode;
            Type(int code) {
                mCode = code;
            }
            public String getCode() {
                return mCode + "!";
            }
        }

        public final Type type;
        // The data field is always Base64 encoded
        public final String id;

        public AdvertisedId(JSONObject object) {
            this.type = Type.valueOf(object.optString("type"));
            this.id = object.optString("id");
        }

        private AdvertisedId(Type type, String id) {
            this.type = type;
            this.id = id;
        }

        public String getId() {
            byte[] rawId = Base64.decode(this.id, Base64.NO_WRAP);
            return Utils.toHexString(rawId, 0, rawId.length);
        }


        public String toBeaconName() {
            StringBuilder sb = new StringBuilder();
            sb.append("beacons/");
            sb.append(type.getCode());
            sb.append(getId());

            return sb.toString();
        }

        public JSONObject toJson() throws JSONException {
            JSONObject object = new JSONObject();

            object.put("type", this.type.toString());
            object.put("id", this.id);

            return object;
        }

        @Override
        public boolean equals(Object object) {
            return (object instanceof AdvertisedId
                    && ((AdvertisedId) object).id.equals(this.id));
        }
    }
}
