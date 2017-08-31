package com.iottivebeacontool.iottivebeacontool.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iottivebeacontool.iottivebeacontool.R;
import com.iottivebeacontool.iottivebeacontool.model.AllBeaconModel;
import com.iottivebeacontool.iottivebeacontool.model.NearByBeaconModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by iottive on 7/20/17.
 */

public class NearByBeaconAdapter extends BaseAdapter {
    private Context context;
    private int int_fragment;
    private List<NearByBeaconModel> objects;
    private String header = "";


    public NearByBeaconAdapter(Context context,/* List<NearByBeaconModel> objects,*/ int int_fragment) {
        this.context = context;
        objects = new ArrayList<>();
        this.int_fragment = int_fragment;
    }
    public void ListUpdate(List<NearByBeaconModel> modelRangings){
        this.objects=modelRangings;

    }
    public void addDiscoveredBeacon(NearByBeaconModel beacon) {
        int index = isContainBeacon(beacon, (ArrayList<NearByBeaconModel>) objects);
        if (index == -1) {
          if(int_fragment==0) {
              if(beacon.bea_status.equals("STATUS_UNSPECIFIED")){
                  objects.add(beacon);
              }else if(beacon.bea_status.equals("UNREGISTERED")){
                  objects.add(beacon);
              }else if(beacon.bea_status.equals("UNAUTHORIZED")){
                  objects.add(beacon);
              }else {


              }
              notifyDataSetChanged();
              Collections.sort(objects);
          }
            if(int_fragment==1) {
                if(beacon.bea_status.equals("STATUS_UNSPECIFIED")){
                }else if(beacon.bea_status.equals("UNREGISTERED")){
                }else if(beacon.bea_status.equals("UNAUTHORIZED")){
                }else {
                    objects.add(beacon);
                }
                notifyDataSetChanged();
                Collections.sort(objects);
            }
        }
       /* if (!objects.contains(beacon)) {
            objects.add(beacon);*/


        }


    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if(convertView==null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.unregisterd_list_row, parent, false);

            holder.tv_eddystoneType = (TextView) convertView.findViewById(R.id.tv_eddystoneType);
            holder.tv_namespace = (TextView) convertView.findViewById(R.id.tv_namespace);
            holder.tv_instance = (TextView) convertView.findViewById(R.id.tv_instance);
            holder.tv_tx = (TextView) convertView.findViewById(R.id.tv_tx);
            holder.tv_rx = (TextView) convertView.findViewById(R.id.tv_rx);
            holder.ll_bcn_type = (LinearLayout) convertView.findViewById(R.id.ll_bcn_type);


            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_eddystoneType.setText(objects.get(position).bea_type);
        holder.tv_namespace.setText(objects.get(position).bea_namespace);
        holder.tv_instance.setText(objects.get(position).bea_instance);
        holder.tv_tx.setText("Tx: "+objects.get(position).bea_tx);
        holder.tv_rx.setText("Rx: "+objects.get(position).bea_rssi);

        if(!header.equals(objects.get(position).bea_namespace)){
            holder.ll_bcn_type.setVisibility(View.VISIBLE);
            header=objects.get(position).bea_namespace;

        }else{
            if(position==0){
                holder.ll_bcn_type.setVisibility(View.VISIBLE);
                header=objects.get(position).bea_namespace;

            }else {
                holder.ll_bcn_type.setVisibility(View.GONE);
            }
        }


        return convertView;
    }

    class ViewHolder {
      private TextView tv_eddystoneType, tv_namespace, tv_instance, tv_tx,tv_rx;
        private LinearLayout ll_bcn_type;

    }
    private int isContainBeacon(NearByBeaconModel modelRanging, ArrayList<NearByBeaconModel> arrayListBeacn) {
        int index = -1;

        if(arrayListBeacn.size()>1) {
            if (modelRanging != null) {
                for (int x = 0; x < arrayListBeacn.size(); x++) {
                    if (modelRanging.bea_mac_address.equals(arrayListBeacn.get(x).bea_mac_address)) {
                        index = x;
                        return index;
                    }
                }
            }
        }
        return index;
    }
}
