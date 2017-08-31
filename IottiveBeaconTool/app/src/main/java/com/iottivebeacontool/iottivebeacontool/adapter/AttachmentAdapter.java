package com.iottivebeacontool.iottivebeacontool.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.iottivebeacontool.iottivebeacontool.R;
import com.iottivebeacontool.iottivebeacontool.api.ProximityApi;
import com.iottivebeacontool.iottivebeacontool.model.Attachmentmodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class AttachmentAdapter extends ArrayAdapter<Attachmentmodel> {

    private String header = "";
    List<Attachmentmodel> list = new ArrayList<Attachmentmodel>();


    public AttachmentAdapter(Context context) {
        super(context, 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.attachment_item, parent, false);
        }

        final Attachmentmodel item = getItem(position);
        list.add(item);
        TextView text1 = (TextView) convertView.findViewById(R.id.text1);
        TextView text2 = (TextView) convertView.findViewById(R.id.text2);
        ImageView button_delete = (ImageView) convertView.findViewById(R.id.button_delete);


        text1.setText(item.attach_namespaceType_list);
        text2.setText(item.attach_data_list);
        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Are You Sure?")
                        .setMessage("Do you want to delete " + item.attch_name_list + " ?")
                        .setNegativeButton("Cancel", null)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ProximityApi.getInstance(getContext()).deleteAttachment(item);
                            }
                        })
                        .show();
            }
        });


       /* Collections.sort(list);

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
*/
        return convertView;
    }

}
