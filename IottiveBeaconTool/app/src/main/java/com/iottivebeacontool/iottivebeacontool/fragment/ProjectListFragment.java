package com.iottivebeacontool.iottivebeacontool.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.iottivebeacontool.iottivebeacontool.GlobalApplication;
import com.iottivebeacontool.iottivebeacontool.ListProjectsTask;
import com.iottivebeacontool.iottivebeacontool.R;
import com.iottivebeacontool.iottivebeacontool.SharedPrefsUtils;
import com.iottivebeacontool.iottivebeacontool.activity.MainActivity;
import com.iottivebeacontool.iottivebeacontool.adapter.ProjectListAdapter;
import com.iottivebeacontool.iottivebeacontool.api.ProximityApi;
import com.iottivebeacontool.iottivebeacontool.model.ProjectModel;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by iottive on 8/8/17.
 */

public class ProjectListFragment extends Fragment{
    private Activity pro_list_act;
    private ListView lv_project_list;
    private TextView tv_pro_impty;
    private ProjectListAdapter mAdapter = null;
    private ArrayList<ProjectModel> mArrayList;
    private ProjectModel projectModel = null;
    private ProgressDialog pListDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.project_list_fragment, container, false);
        pro_list_act=getActivity();
        lv_project_list =(ListView) view.findViewById(R.id.lv_project_list);
        tv_pro_impty =(TextView) view.findViewById(R.id.tv_pro_impty);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pro_list_act=getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
        pro_list_act = getActivity();
        mArrayList = new ArrayList<>();
        pListDialog = new ProgressDialog(pro_list_act);
        pListDialog.setMessage("Getting ProjectList......");
        pListDialog.setCanceledOnTouchOutside(false);
        pListDialog.show();

        if(pro_list_act instanceof MainActivity){
            MainActivity myactivity = (MainActivity) pro_list_act;
            myactivity.setToolbar(getString(R.string.project_list),View.GONE);
        }
        if (ProximityApi.getInstance(getActivity()).hasToken()) {
            listProjectsFromConsole(SharedPrefsUtils.getStringPreference(pro_list_act, GlobalApplication.ACCOUNT_TOKEN));

        }else {
            Toast.makeText(pro_list_act, "Your token is null", Toast.LENGTH_SHORT).show();
        }

        lv_project_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                SharedPrefsUtils.setStringPreference(pro_list_act,GlobalApplication.CURRENT_PROJECT_ID,mArrayList.get(position).project_id);
                SharedPrefsUtils.setStringPreference(pro_list_act,GlobalApplication.CURRENT_PROJECT_NUM,mArrayList.get(position).project_num);

               if(mArrayList.get(position).project_lifeCycle_state.equals("ACTIVE")) {
                   if (pro_list_act instanceof MainActivity) {
                       MainActivity myactivity = (MainActivity) pro_list_act;
                       SharedPrefsUtils.setBooleanPreference(pro_list_act, GlobalApplication.PROJECT_SELECTED, false);
                       myactivity.setCurrentProject(mArrayList.get(position).project_id, mArrayList.get(position).project_num);
                   }
                   pro_list_act.onBackPressed();
               }else {
                   Toast.makeText(pro_list_act, "This project is requested to delete", Toast.LENGTH_SHORT).show();
               }

            }
        });

    }

    private void listProjectsFromConsole(final String accessToken){
        new ListProjectsTask(pro_list_act, GlobalApplication.GET_PROJECT_LIST, mListProjectsCallBack, accessToken).execute();
    }

    Callback mListProjectsCallBack = new Callback() {
        @Override
        public void onFailure(Request request, IOException e) {

        }

        @Override
        public void onResponse(Response response) throws IOException {

            try {

                String body = response.body().string();
                final JSONObject jsonResponse = new JSONObject(body);
                if (!response.isSuccessful()) {
                    final JSONObject jsonError = jsonResponse.getJSONObject("error");
                    final int errorCode = jsonError.getInt("code");
                    final String message = jsonError.getString("message");
                    tv_pro_impty.setVisibility(View.VISIBLE);
                    if(pListDialog.isShowing()){
                        pListDialog.dismiss();
                    }

                }else {
                    Log.v("Result=======>responce", jsonResponse.toString());
                    parseProjectsFromResponse(jsonResponse);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private void parseProjectsFromResponse(JSONObject jsonResponse){
        try {


            JSONObject tempJsonObj;

            if(jsonResponse.has("projects"))
            {
                JSONArray jsonArr = jsonResponse.getJSONArray("projects");
                Log.v("Result=======>projects", String.valueOf(jsonArr));

                for (int i = 0; i < jsonArr.length(); i++) {
                    tempJsonObj = jsonArr.getJSONObject(i);
                     projectModel = new ProjectModel();
                    projectModel.project_id=tempJsonObj.optString("projectId");
                    projectModel.project_num=tempJsonObj.optString("projectNumber");
                    projectModel.project_lifeCycle_state=tempJsonObj.optString("lifecycleState");

                    mArrayList.add(projectModel);
                }
                if(pListDialog.isShowing()){
                    pListDialog.dismiss();
                }
               /* if(mArrayList.size()>0) {
                    SharedPrefsUtils.setStringPreference(pro_list_act, GlobalApplication.CURRENT_PROJECT_ID, mArrayList.get(0).project_id);
                    SharedPrefsUtils.setStringPreference(pro_list_act, GlobalApplication.CURRENT_PROJECT_NUM, mArrayList.get(0).project_num);
                    SharedPrefsUtils.setBooleanPreference(pro_list_act, GlobalApplication.PROJECT_SELECTED, false);

                    if (pro_list_act instanceof MainActivity) {
                        MainActivity myactivity = (MainActivity) pro_list_act;
                        myactivity.setCurrentProject(mArrayList.get(0).project_id, mArrayList.get(0).project_num);
                    }
                }*/
                mAdapter = new ProjectListAdapter(pro_list_act,mArrayList);
                lv_project_list.setAdapter(mAdapter);
            } else {
                if(pListDialog.isShowing()){
                    pListDialog.dismiss();
                }
                tv_pro_impty.setVisibility(View.VISIBLE);
                lv_project_list.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
