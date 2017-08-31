package com.iottivebeacontool.iottivebeacontool.model;

import com.iottivebeacontool.iottivebeacontool.data.MyStaticBeacon;

/**
 * Created by iottive on 7/20/17.
 */

public class NearByBeaconModel implements Comparable<NearByBeaconModel>{
    public String bea_type;
    public String bea_namespace;
    public String bea_instance;
    public String bea_tx;
    public String bea_rssi;
    public String bea_status;
    public String bea_name;
    public String bea_mac_address;
    public MyStaticBeacon.Status beacon_registord_or_not=null;

    public NearByBeaconModel(String status, String type, String namespace, String instance, String tx, String rssi,String name,String mac_address) {

        this.bea_type=type;
        this.bea_namespace=namespace;
        this.bea_instance=instance;
        this.bea_tx=tx;
        this.bea_rssi=rssi;
        this.bea_status= status;
        this.bea_name= name;
        this.bea_mac_address= mac_address;
       /* this.name = object.optString("beaconName");
        this.status = Status.valueOf(object.optString("status"));
        this.advertisedId = new AdvertisedId(object.optJSONObject("advertisedId"));
        //If no description, use the beaconId from the name
        String description = object.optString("description");
        this.description = TextUtils.isEmpty(description) ?
                name.substring(name.indexOf('!')+1, name.length()) : description;*/
    }
    @Override
    public int compareTo(NearByBeaconModel other) {
        if (bea_namespace!=null){
            return bea_namespace.compareTo(other.bea_namespace);
        }else {
            return 0;
        }

    }
}
