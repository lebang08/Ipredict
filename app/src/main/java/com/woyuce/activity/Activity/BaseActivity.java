package com.woyuce.activity.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.umeng.analytics.MobclickAgent;
import com.woyuce.activity.Application.MyApplication;
import com.woyuce.activity.Utils.LogUtil;
import com.woyuce.activity.Utils.PreferenceUtil;
import com.woyuce.activity.Utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/20.
 */
public class BaseActivity extends Activity {

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    public ProgressDialog progressdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo(); // getActiveNetworkInfo获取当前可用网络
        if (networkInfo == null || !networkInfo.isAvailable()) {
            ToastUtil.showMessage(this, "网络链接不可用，请检查网络");
        } else {
            // toast("链接成功");
        }
    }

    public void progressdialogshow(Context context) {
        progressdialog = new ProgressDialog(context);
        progressdialog.setTitle("加载中，请稍候");
        progressdialog.setMessage("Loading...");
        progressdialog.setCanceledOnTouchOutside(false);
//         progressdialog.setCancelable(false);
        progressdialog.show();
    }

    public void progressdialogcancel() {
        progressdialog.cancel();
    }

    //先获取token，token用于此后每一次接口请求的参数
    public void getBaseToken() {
        StringRequest tokenrequest = new StringRequest(Request.Method.POST, "http://api.iyuce.com/api/token",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            String localtoken = obj.getString("access_token");
//							String token_expires = obj.getString("expires_in");
                            PreferenceUtil.save(BaseActivity.this, "localtoken", localtoken);
                            LogUtil.e("localtoken1 = " + localtoken);
//							PreferenceUtil.save(BaseActivity.this, "token_expires", token_expires);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getBaseToken();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String base64EncodedString = null;
                try {
                    String encodedConsumerKey = URLEncoder.encode("defA8Dq2ambB", "UTF-8");
                    String encodedConsumerSecret = URLEncoder.encode("WM7Ei5mzrrHl42HHXuGkNR0bVJexq4P", "UTF-8");
                    String authString = encodedConsumerKey + ":" + encodedConsumerSecret;
                    base64EncodedString = Base64.encodeToString(authString.getBytes("UTF-8"), Base64.NO_WRAP);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Basic " + base64EncodedString);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("grant_type", "client_credentials");
                headers.put("scope", "");
                return headers;
            }
        };
        tokenrequest.setTag("base");
        MyApplication.getHttpQueue().add(tokenrequest);
    }
}
