package com.iottivebeacontool.iottivebeacontool.data;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.iottivebeacontool.iottivebeacontool.HttpCallback;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;


/**
 * Created by rora on 07.04.2016.
 */
public class RequestAccessTokenTask extends AsyncTask<Void, Void, Void> {
    private final OkHttpClient mOkHttpClient;
    private final Callback mCallBack;
    private final String url;
    private String clientSecret;
    private String mClientId;
    private String mCode;
    private ProgressDialog mProgressDialog;
    private Activity mActivity;

    public RequestAccessTokenTask(final Activity activity, final Callback mCallBack, final String url, String clientId, String clientSecret, String code){
        this.mActivity = activity;
        this.mOkHttpClient = new OkHttpClient();
        this.mCallBack = mCallBack;
        this.url = url;
        this.mClientId = clientId;
        this.clientSecret = clientSecret;
        this.mCode = code;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        showProgressDialog();
    }

    @Override
    protected Void doInBackground(Void... params) {

        RequestBody requestBody = new FormEncodingBuilder()
                .add("grant_type", "authorization_code")
                .add("client_id", mClientId)
                .add("client_secret", clientSecret)
                .add("redirect_uri","http://www.iottive.com")
                .add("code", mCode)
                .build();
        final Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        mOkHttpClient.newCall(request).enqueue(new HttpCallback((Callback) mCallBack));
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        hideProgressDialog();
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mActivity);
            mProgressDialog.setMessage("please wait");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

}
