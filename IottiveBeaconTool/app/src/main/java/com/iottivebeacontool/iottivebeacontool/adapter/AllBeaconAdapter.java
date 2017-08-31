package com.iottivebeacontool.iottivebeacontool.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iottivebeacontool.iottivebeacontool.R;
import com.iottivebeacontool.iottivebeacontool.model.AllBeaconModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by iottive on 7/20/17.
 */

public class AllBeaconAdapter extends ArrayAdapter<AllBeaconModel> {
    private String header = "";
    List<AllBeaconModel> list = new ArrayList<AllBeaconModel>();


    public AllBeaconAdapter(Context context) {
        super(context, 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.all_bea_list_row, parent, false);
        }

        final AllBeaconModel item = getItem(position);
        list.add(item);
       TextView tv_eddystoneType = (TextView) convertView.findViewById(R.id.tv_eddystoneType);
        TextView tv_namespace = (TextView) convertView.findViewById(R.id.tv_namespace);
        TextView tv_instance = (TextView) convertView.findViewById(R.id.tv_instance);
        TextView tv_state = (TextView) convertView.findViewById(R.id.tv_state);
        TextView tv_discription = (TextView) convertView.findViewById(R.id.tv_discription);
        LinearLayout ll_bcn_type = (LinearLayout) convertView.findViewById(R.id.ll_bcn_type);

        tv_eddystoneType.setText(item.bea_type);
        tv_namespace.setText(item.bea_namespace);
        tv_instance.setText(item.bea_instance);
        tv_state.setText(item.bea_state);
       tv_discription.setText(item.bea_desc);
       /* if(item.bea_state.equals("INACTIVE")){
            tv_state.setTextColor(Color.parseColor("#303F9F"));
        }
        if(item.bea_state.equals("DECOMMISSIONED")){
            tv_state.setTextColor(Color.parseColor("#cc0000"));
        }
*/
        Collections.sort(list);

        if(!header.equals(item.bea_namespace)){
           ll_bcn_type.setVisibility(View.VISIBLE);
            header=item.bea_namespace;


        }else{
            if(position==0){
               ll_bcn_type.setVisibility(View.VISIBLE);
                header=item.bea_namespace;

            }else {
                ll_bcn_type.setVisibility(View.GONE);
            }
        }

        return convertView;
    }
  /*  private Context context;
    private List<AllBeaconModel> objects;
    private String header = "";

    public AllBeaconAdapter(Context context, List<AllBeaconModel> objects) {
        this.context = context;
        this.objects = objects;
    }
    public void ListUpdate(List<AllBeaconModel> modelRangings){
        this.objects=modelRangings;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.all_bea_list_row, parent, false);

            holder.tv_eddystoneType = (TextView) convertView.findViewById(R.id.tv_eddystoneType);
            holder.tv_namespace = (TextView) convertView.findViewById(R.id.tv_namespace);
            holder.tv_instance = (TextView) convertView.findViewById(R.id.tv_instance);
            holder.tv_state = (TextView) convertView.findViewById(R.id.tv_state);
            holder.tv_discription = (TextView) convertView.findViewById(R.id.tv_discription);
            holder.ll_bcn_type = (LinearLayout) convertView.findViewById(R.id.ll_bcn_type);


            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_eddystoneType.setText(objects.get(position).bea_type);
        holder.tv_namespace.setText(objects.get(position).bea_namespace);
        holder.tv_instance.setText(objects.get(position).bea_instance);
        holder.tv_state.setText(objects.get(position).bea_state);
        holder.tv_discription.setText(objects.get(position).bea_desc);
        Collections.sort(objects);

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
        TextView tv_eddystoneType, tv_namespace, tv_instance, tv_state,tv_discription;
        LinearLayout ll_bcn_type;

    }*/
}
