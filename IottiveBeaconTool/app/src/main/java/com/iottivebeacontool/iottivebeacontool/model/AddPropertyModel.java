package com.iottivebeacontool.iottivebeacontool.model;

import java.io.Serializable;

/**
 * Created by iottive on 7/25/17.
 */

public class AddPropertyModel implements Serializable{
    public String pro_name;
    public String pro_value;
    public String attach_type;
    public String attch_data;

    public String getAttach_name() {
        return attach_name;
    }

    public void setAttach_name(String attach_name) {
        this.attach_name = attach_name;
    }

    public String attach_name;

    public String getPro_name() {
        return pro_name;
    }

    public void setPro_name(String pro_name) {
        this.pro_name = pro_name;
    }

    public String getPro_value() {
        return pro_value;
    }

    public void setPro_value(String pro_value) {
        this.pro_value = pro_value;
    }

    public String getAttach_type() {
        return attach_type;
    }

    public void setAttach_type(String attach_type) {
        this.attach_type = attach_type;
    }

    public String getAttch_data() {
        return attch_data;
    }

    public void setAttch_data(String attch_data) {
        this.attch_data = attch_data;
    }
}
