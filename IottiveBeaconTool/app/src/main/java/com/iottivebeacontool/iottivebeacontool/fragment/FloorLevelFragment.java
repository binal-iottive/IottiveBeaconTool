package com.iottivebeacontool.iottivebeacontool.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.iottivebeacontool.iottivebeacontool.GlobalApplication;
import com.iottivebeacontool.iottivebeacontool.R;
import com.iottivebeacontool.iottivebeacontool.SharedPrefsUtils;
import com.iottivebeacontool.iottivebeacontool.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iottive on 7/25/17.
 */

public class FloorLevelFragment extends Fragment implements AdapterView.OnItemSelectedListener ,View.OnClickListener{
    private Spinner spinner_floor_lvl;
    private Activity floor_activity;
    private ArrayList<String> categories;
    private ArrayAdapter<String> dataAdapter=null;
    private TextView tv_addFloor;
    private String item;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_floor_lvl, container, false);
        floor_activity=getActivity();
        initFloorUi(view);
        return view;
    }

    private void initFloorUi(View view) {

        tv_addFloor= (TextView) view.findViewById(R.id.tv_addFloor);
        tv_addFloor.setOnClickListener(this);

        spinner_floor_lvl= (Spinner) view.findViewById(R.id.spinner_floor_lvl);
        categories = new ArrayList();
        getFloorList();
        dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_list_row, categories);
        spinner_floor_lvl.setAdapter(dataAdapter);
        spinner_floor_lvl.setOnItemSelectedListener(this);


    }

    private void getFloorList() {
        int i=0;
        for(i=-100;i<250;i++){
            categories.add(String.valueOf(i));

        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        floor_activity=getActivity();

    }

    @Override
    public void onResume() {
        super.onResume();
        floor_activity=getActivity();

        if(floor_activity instanceof MainActivity){
            MainActivity myactivity = (MainActivity) floor_activity;
            myactivity.setToolbar(getString(R.string.indoor_floor_lvl),View.GONE);

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
         item = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_addFloor:
                SharedPrefsUtils.setStringPreference(floor_activity, GlobalApplication.floor_lvl_text,item);
                SharedPrefsUtils.setBooleanPreference(floor_activity, GlobalApplication.boolean_floor_lvl,true);
                 getActivity().onBackPressed();
                break;
        }
    }
}
