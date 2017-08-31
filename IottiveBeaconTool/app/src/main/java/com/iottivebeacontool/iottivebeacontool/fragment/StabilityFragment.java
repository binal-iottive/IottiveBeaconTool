package com.iottivebeacontool.iottivebeacontool.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iottivebeacontool.iottivebeacontool.GlobalApplication;
import com.iottivebeacontool.iottivebeacontool.R;
import com.iottivebeacontool.iottivebeacontool.SharedPrefsUtils;
import com.iottivebeacontool.iottivebeacontool.activity.MainActivity;

/**
 * Created by iottive on 7/25/17.
 */

public class StabilityFragment extends Fragment implements View.OnClickListener {
    private Activity stable_activity;
    private TextView tv_stable,tv_portable,tv_mobile,tv_roving;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        stable_activity=getActivity();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_stability,
                container, false);

        stable_activity=getActivity();
        initStabilityUi(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (stable_activity instanceof MainActivity) {
            MainActivity myactivity = (MainActivity) stable_activity;
            myactivity.setToolbar(getString(R.string.stable), View.GONE);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        stable_activity=getActivity();

    }
    private void initStabilityUi(View view) {
        tv_stable=  (TextView) view.findViewById(R.id.tv_stable);
        tv_portable=  (TextView) view.findViewById(R.id.tv_portable);
        tv_mobile=  (TextView) view.findViewById(R.id.tv_mobile);
        tv_roving=  (TextView) view.findViewById(R.id.tv_roving);

        tv_stable.setOnClickListener(this);
        tv_portable.setOnClickListener(this);
        tv_mobile.setOnClickListener(this);
        tv_roving.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_stable:
                SharedPrefsUtils.setStringPreference(stable_activity, GlobalApplication.stable_text,tv_stable.getText().toString());
                SharedPrefsUtils.setBooleanPreference(stable_activity, GlobalApplication.boolean_stable,true);
                stable_activity.onBackPressed();
                break;
            case R.id.tv_portable:
                SharedPrefsUtils.setStringPreference(stable_activity, GlobalApplication.stable_text,tv_portable.getText().toString());
                SharedPrefsUtils.setBooleanPreference(stable_activity, GlobalApplication.boolean_stable,true);
                stable_activity.onBackPressed();
                break;
            case R.id.tv_mobile:
                SharedPrefsUtils.setStringPreference(stable_activity, GlobalApplication.stable_text,tv_mobile.getText().toString());
                SharedPrefsUtils.setBooleanPreference(stable_activity, GlobalApplication.boolean_stable,true);
                stable_activity.onBackPressed();
                break;
            case R.id.tv_roving:
                SharedPrefsUtils.setStringPreference(stable_activity, GlobalApplication.stable_text,tv_roving.getText().toString());
                SharedPrefsUtils.setBooleanPreference(stable_activity, GlobalApplication.boolean_stable,true);
                stable_activity.onBackPressed();
                break;
        }
    }
}
