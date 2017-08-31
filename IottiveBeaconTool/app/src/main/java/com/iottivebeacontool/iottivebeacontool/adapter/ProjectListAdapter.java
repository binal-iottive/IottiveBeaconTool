package com.iottivebeacontool.iottivebeacontool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iottivebeacontool.iottivebeacontool.R;
import com.iottivebeacontool.iottivebeacontool.model.AllBeaconModel;
import com.iottivebeacontool.iottivebeacontool.model.NearByBeaconModel;
import com.iottivebeacontool.iottivebeacontool.model.ProjectModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by iottive on 8/8/17.
 */

public class ProjectListAdapter extends BaseAdapter {
    private Context context;
    private List<ProjectModel> objects;

    public ProjectListAdapter(Context context, List<ProjectModel> objects) {
        this.context = context;
        this.objects = objects;

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
        final ProjectListAdapter.ViewHolder holder;

        if(convertView==null){
            holder = new ProjectListAdapter.ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.project_list_row, parent, false);

            holder.tv_pro_id = (TextView) convertView.findViewById(R.id.tv_pro_id);
            holder.tv_pro_num = (TextView) convertView.findViewById(R.id.tv_pro_num);


            convertView.setTag(holder);
        }else{
            holder = (ProjectListAdapter.ViewHolder) convertView.getTag();
        }

        holder.tv_pro_id.setText(objects.get(position).project_id);
        holder.tv_pro_num.setText(objects.get(position).project_num);




        return convertView;
    }

    class ViewHolder {
        private TextView tv_pro_id, tv_pro_num;


    }

}
