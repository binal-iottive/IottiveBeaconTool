package com.iottivebeacontool.iottivebeacontool.api;

import android.content.Context;
import android.util.Log;

import com.iottivebeacontool.iottivebeacontool.data.Attachment;
import com.iottivebeacontool.iottivebeacontool.data.MyStaticBeacon;
import com.iottivebeacontool.iottivebeacontool.model.Attachmentmodel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Loader to execute and parse beacons.attachments.list
 */
public class AttachmentsLoader extends BaseApiLoader<List<Attachmentmodel>> {
    private static final String TAG = AttachmentsLoader.class.getSimpleName();

    private String mBeaconName;

    public AttachmentsLoader(Context context, String beaconName) {
        super(context);
        mBeaconName = beaconName;
    }

    @Override
    protected void onStartRequest() {
        ProximityApi.getInstance(getContext())
                .getAttachmentsList(mBeaconName, this, this);
    }

    @Override
    protected List<Attachmentmodel> onHandleResponse(JSONObject response) {
        //API Error
        if (response == null) return null;

        try {
            return MyStaticBeacon.AttchfromJson(response);
        } catch (JSONException e) {
            Log.w(TAG, "Parsing error", e);
            return null;
        }
    }
}
