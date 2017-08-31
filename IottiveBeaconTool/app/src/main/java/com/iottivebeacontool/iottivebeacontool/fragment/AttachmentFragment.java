package com.iottivebeacontool.iottivebeacontool.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.iottivebeacontool.iottivebeacontool.GlobalApplication;
import com.iottivebeacontool.iottivebeacontool.R;
import com.iottivebeacontool.iottivebeacontool.SharedPrefsUtils;
import com.iottivebeacontool.iottivebeacontool.activity.AttachmentListActivity;
import com.iottivebeacontool.iottivebeacontool.activity.MainActivity;
import com.iottivebeacontool.iottivebeacontool.api.ProximityApi;
import com.iottivebeacontool.iottivebeacontool.model.AddPropertyModel;

import java.util.ArrayList;

import static com.iottivebeacontool.iottivebeacontool.GlobalApplication.BEACON_ROOT;
import static com.iottivebeacontool.iottivebeacontool.GlobalApplication.SELECTED_PROJECT;

/**
 * Created by iottive on 7/26/17.
 */

public class AttachmentFragment extends Fragment implements View.OnClickListener{
    private Activity attechment_activity;
    private EditText et_namespaceType,et_data;
    private TextView tv_addAttachmnent,tv_project_name;
    private ArrayList<AddPropertyModel> AttechmentList ;
    AddPropertyModel addProperty ;
    private ProximityApi mProximityApi;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        attechment_activity=getActivity();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_attachment, container, false);
        attechment_activity=getActivity();
        mProximityApi = ProximityApi.getInstance(attechment_activity);
        initPropertyUi(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (attechment_activity instanceof MainActivity) {
            MainActivity myactivity = (MainActivity) attechment_activity;
            myactivity.setToolbar(getString(R.string.attachment), View.GONE);
        }
        if(!SELECTED_PROJECT.equals("")){
            tv_project_name.setText(SELECTED_PROJECT+"/");
        }else {
            tv_project_name.setText("iottivebeacontool/");
        }
    }

    private void initPropertyUi(View view) {
        et_namespaceType = (EditText) view.findViewById(R.id.et_namespaceType);
        et_data = (EditText) view.findViewById(R.id.et_data);
        tv_project_name = (TextView) view.findViewById(R.id.tv_project_name);
        tv_addAttachmnent = (TextView) view.findViewById(R.id.tv_addAttachmnent);
        tv_addAttachmnent.setOnClickListener(this);
        AttechmentList = new ArrayList<>();

        if(SharedPrefsUtils.getBooleanPreference(attechment_activity, GlobalApplication.boolean_attechment)==true){
            AttechmentList = SharedPrefsUtils.getArraylist(attechment_activity,GlobalApplication.attach_list);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        attechment_activity=getActivity();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_addAttachmnent:
                if(et_namespaceType.getText().toString().trim().equals("")){
                    Toast.makeText(attechment_activity, "Please enter key", Toast.LENGTH_SHORT).show();
                }else  if(et_data.getText().toString().trim().equals("")){
                    Toast.makeText(attechment_activity, "Please enter value", Toast.LENGTH_SHORT).show();
                }else {
                    addProperty = new AddPropertyModel();
                    //addProperty.setAttach_name(et_namespaceType.getText().toString());
                    addProperty.setAttach_type(tv_project_name.getText().toString()+et_namespaceType.getText().toString());
                    addProperty.setAttch_data(et_data.getText().toString());
                    AttechmentList.add(addProperty);
                    SharedPrefsUtils.setArraylist(attechment_activity,GlobalApplication.attach_list,AttechmentList);
                    SharedPrefsUtils.setBooleanPreference(attechment_activity, GlobalApplication.boolean_attechment,true);
                    String BraconName;
                 /*   Bundle bundle = this.getArguments();

                    if (bundle != null) {
                        BraconName = bundle.getString(GlobalApplication.BEACON_NAME_ATTACH, null);
                        mProximityApi.createAttachment(BraconName,et_data.getText().toString(),tv_project_name.getText().toString()+et_namespaceType.getText().toString());

                    }*/
                 if( GlobalApplication.ATTACHMENT_LOG.equals("addEditAttach")){
                     BraconName = SharedPrefsUtils.getStringPreference(getActivity(), GlobalApplication.BEACON_NAME_ATTACH);
                     mProximityApi.createAttachment(BraconName,et_data.getText().toString(),tv_project_name.getText().toString()+et_namespaceType.getText().toString());
                     if (attechment_activity instanceof AttachmentListActivity) {
                         AttachmentListActivity myactivity = (AttachmentListActivity) attechment_activity;
                         myactivity.SetRlVisibility();
                     }
                 }
                    getActivity().onBackPressed();
                }

                break;
        }

    }

}
