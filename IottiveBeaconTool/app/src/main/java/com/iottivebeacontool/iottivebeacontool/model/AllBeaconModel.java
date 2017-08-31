package com.iottivebeacontool.iottivebeacontool.model;

import com.iottivebeacontool.iottivebeacontool.data.MyStaticBeacon;

/**
 * Created by iottive on 7/20/17.
 */

public class AllBeaconModel implements Comparable<AllBeaconModel>{
    public String bea_type;
    public String bea_namespace;
    public String bea_instance;
    public String bea_state;
/*    public String bea_stability;
    public String bea_indoorlvl;
    public String bea_placeid;
    public String bea_lat;
    public String bea_long;
    public String bea_properties;*/

    public String bea_desc;
    public String beacon_name;

    @Override
    public int compareTo(AllBeaconModel other) {
        return bea_namespace.compareTo(other.bea_namespace);
    }
}
