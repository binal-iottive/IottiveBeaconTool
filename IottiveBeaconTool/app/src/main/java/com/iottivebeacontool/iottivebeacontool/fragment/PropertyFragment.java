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
import com.iottivebeacontool.iottivebeacontool.activity.MainActivity;
import com.iottivebeacontool.iottivebeacontool.model.AddPropertyModel;

import java.util.ArrayList;

/**
 * Created by iottive on 7/26/17.
 */

public class PropertyFragment extends Fragment implements View.OnClickListener {
    private Activity property_activity;
    private EditText et_key,et_value;
    private TextView tv_addProperty;
    private ArrayList<AddPropertyModel> propertyList ;
    AddPropertyModel addProperty ;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        property_activity=getActivity();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_property,
                container, false);

        property_activity=getActivity();
        initPropertyUi(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (property_activity instanceof MainActivity) {
            MainActivity myactivity = (MainActivity) property_activity;
            myactivity.setToolbar(getString(R.string.properties), View.GONE);
        }
    }

    private void initPropertyUi(View view) {
        et_key = (EditText) view.findViewById(R.id.et_key);
        et_value = (EditText) view.findViewById(R.id.et_value);
        tv_addProperty = (TextView) view.findViewById(R.id.tv_addProperty);
        tv_addProperty.setOnClickListener(this);
        propertyList = new ArrayList<>();

        if(SharedPrefsUtils.getBooleanPreference(property_activity, GlobalApplication.boolean_property)==true){
            propertyList = SharedPrefsUtils.getArraylist(property_activity,GlobalApplication.property_list);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        property_activity=getActivity();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_addProperty:
                if(et_key.getText().toString().trim().equals("")){
                    Toast.makeText(property_activity, "Please enter key", Toast.LENGTH_SHORT).show();
                }else  if(et_value.getText().toString().trim().equals("")){
                    Toast.makeText(property_activity, "Please enter value", Toast.LENGTH_SHORT).show();
                }else {
                    addProperty = new AddPropertyModel();
                    addProperty.setPro_name(et_key.getText().toString());
                    addProperty.setPro_value(et_value.getText().toString());
                    propertyList.add(addProperty);
                    SharedPrefsUtils.setArraylist(property_activity,GlobalApplication.property_list,propertyList);
                    SharedPrefsUtils.setBooleanPreference(property_activity, GlobalApplication.boolean_property,true);
                    getActivity().onBackPressed();
                }

                break;
        }

    }
}
