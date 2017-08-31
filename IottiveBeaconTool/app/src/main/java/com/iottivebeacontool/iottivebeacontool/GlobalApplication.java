package com.iottivebeacontool.iottivebeacontool;

import android.app.Application;

/**
 * Created by iottive on 7/25/17.
 */

public class GlobalApplication extends Application {

    public static final String GET_PROJECT_LIST ="https://cloudresourcemanager.googleapis.com/v1beta1/projects";
    public static final String SCOPE_CLOUD_PLATFORM ="https://www.googleapis.com/auth/cloud-platform";
    public static final String SCOPE_PROXIMITY_TOKEN = "https://www.googleapis.com/auth/userlocation.beacon.registry";
    public static final String BEACON_ROOT = "https://proximitybeacon.googleapis.com/v1beta1";
    public static String SELECTED_PROJECT="";


    public static final String description_text="Description_text";
    public static final String boolean_description="Boolean_description";
    public static final String floor_lvl_text="Floor_lvl_text";
    public static final String boolean_floor_lvl="Boolean_floor_lvl";
    public static final String stable_text="Stable_text";
    public static final String boolean_stable="Boolean_stable";
    public static final String latitude_text="Latitude_text";
    public static final String longitude_text="Longitude_text";
    public static final String place_id_text="Place_id_text";
    public static final String boolean_place_id="Boolean_place_id";
    public static final String property_list="Property_list";
    public static final String boolean_property="Boolean_property";
    public static final String attach_list="Attach_list";
    public static final String boolean_attechment="Boolean_attechment";

    public static final String PREF_FILE = "Iottive_PREF";
    public static final String NAMESPACE_UID = "namespace_uid";
    public static final String INSTANCE_UID = "instance_uid";
    public static final String BEACON_NAME_ATTACH = "beacon_name_attach";

    public static final String ACCOUNT_TOKEN = "account_token";
    public static final String BOOLEAN_ACCOUNT_TOKEN ="boolean_acount_token";
    public static final String BOOLEAN_ACCOUNT_PIC ="boolean_acount_pic";
    public static final String ACCOUNT_NAME ="account_name";
    public static final String CURRENT_PROJECT_ID ="current_pro_id";
    public static final String CURRENT_PROJECT_NUM ="current_pro_num";
    public static final String PROJECT_SELECTED="project_selected";

    public static final String BEACON_NAMESPACE ="beacon_namespace";
    public static final String BEACON_INSTANCE ="beacon_instance";
    public static final String BEACON_NAME ="beacon_name";

    public static final String PLACE_ID ="place_id";
    public static final String PLACE_LAT ="place_lat";
    public static final String PLACE_LONG ="place_long";

    public static String ATTACHMENT_LOG ="ATTACHMENT_LOG";


}
