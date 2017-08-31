package com.iottivebeacontool.iottivebeacontool.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.iottivebeacontool.iottivebeacontool.GlobalApplication;
import com.iottivebeacontool.iottivebeacontool.R;
import com.iottivebeacontool.iottivebeacontool.SharedPrefsUtils;
import com.iottivebeacontool.iottivebeacontool.activity.MainActivity;

/**
 * Created by iottive on 7/25/17.
 */

public class DescriptionFragment extends Fragment implements View.OnClickListener{
    private Activity desc_activity;
    private EditText et_description;
    private TextView tv_adddesc;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_description,
                container, false);
        desc_activity =getActivity();
        et_description = (EditText) view.findViewById(R.id.et_description);
        tv_adddesc = (TextView) view.findViewById(R.id.tv_adddesc);
        tv_adddesc.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        desc_activity=getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
        desc_activity = getActivity();
        if(desc_activity instanceof MainActivity){
            MainActivity myactivity = (MainActivity) desc_activity;
            myactivity.setToolbar(getString(R.string.description),View.GONE);

        }
        if(SharedPrefsUtils.getStringPreference(getActivity(),GlobalApplication.description_text)!= null){
            et_description.setText(SharedPrefsUtils.getStringPreference(getActivity(),GlobalApplication.description_text));
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_adddesc:
                SharedPrefsUtils.setStringPreference(desc_activity, GlobalApplication.description_text,et_description.getText().toString());
                SharedPrefsUtils.setBooleanPreference(desc_activity, GlobalApplication.boolean_description,true);
                getActivity().onBackPressed();

                break;
        }
    }
}
