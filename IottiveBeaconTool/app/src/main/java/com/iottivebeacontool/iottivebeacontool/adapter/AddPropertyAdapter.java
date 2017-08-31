package com.iottivebeacontool.iottivebeacontool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iottivebeacontool.iottivebeacontool.GlobalApplication;
import com.iottivebeacontool.iottivebeacontool.R;
import com.iottivebeacontool.iottivebeacontool.SharedPrefsUtils;
import com.iottivebeacontool.iottivebeacontool.model.AddPropertyModel;
import com.iottivebeacontool.iottivebeacontool.model.NearByBeaconModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iottive on 7/26/17.
 */

public class AddPropertyAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<AddPropertyModel> objects;
    private int list_type;


    public AddPropertyAdapter(Context context, ArrayList<AddPropertyModel> objects, int list_type) {
        this.context = context;
        this.objects = objects;
        this.list_type = list_type;
    }
    public void ListUpdate(ArrayList<AddPropertyModel> modelRangings){
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final AddPropertyAdapter.ViewHolder holder;

        if(convertView==null){
            holder = new AddPropertyAdapter.ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.add_property_row, parent, false);

            holder.tv_Key = (TextView) convertView.findViewById(R.id.tv_Key);
            holder.tv_value = (TextView) convertView.findViewById(R.id.tv_value);
            holder.iv_deteteRow = (ImageView) convertView.findViewById(R.id.iv_deteteRow);


            convertView.setTag(holder);
        }else{
            holder = (AddPropertyAdapter.ViewHolder) convertView.getTag();
        }
        if(list_type==0) {
            holder.tv_Key.setText("Name: "+objects.get(position).getPro_name());
            holder.tv_value.setText("Value: "+objects.get(position).getPro_value());
            holder.iv_deteteRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    objects.remove(position);
                    notifyDataSetChanged();
                    SharedPrefsUtils.setArraylist(context, GlobalApplication.property_list,objects);

                }
            });
        }else {
            holder.tv_Key.setText("Type: "+objects.get(position).getAttach_type());
            holder.tv_value.setText("Data: "+objects.get(position).getAttch_data());
            holder.iv_deteteRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    objects.remove(position);
                    notifyDataSetChanged();
                    SharedPrefsUtils.setArraylist(context, GlobalApplication.attach_list,objects);

                }
            });
        }


        return convertView;
    }

    class ViewHolder {
        private TextView tv_Key, tv_value;
        ImageView iv_deteteRow;

    }
}
