package com.iottivebeacontool.iottivebeacontool.api;


import com.iottivebeacontool.iottivebeacontool.data.Attachment;
import com.iottivebeacontool.iottivebeacontool.data.MyStaticBeacon;
import com.iottivebeacontool.iottivebeacontool.model.NearByBeaconModel;

public interface ApiDataCallback {
    void onBeaconResponse(NearByBeaconModel beacon);
    void onAttachmentResponse();
}
