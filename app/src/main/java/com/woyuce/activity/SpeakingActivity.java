package com.woyuce.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.woyuce.activity.Adapter.SpeakingAdapter;
import com.woyuce.activity.Application.MyApplication;
import com.woyuce.activity.Bean.SpeakingBean;
import com.woyuce.activity.Utils.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/22.
 */
public class SpeakingActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private Button btnShare, btnBack;
    private LinearLayout llStatis;
    private ListView mListView;
    private TextView txtRefresh;

    private String URL = "http://iphone.ipredicting.com/getvoteMge.aspx";
    private List<SpeakingBean> speakingList = new ArrayList<>();
    private SpeakingAdapter adapter;

    @Override
    protected void onStop() {
        super.onStop();
        MyApplication.getHttpQueue().cancelAll("speaking");
    }

    @Override
    protected void onRestart() { // 该生命周期调用方法刷新数据，没能成功
        super.onRestart();
        speakingList.clear();
        adapter.notifyDataSetChanged();
        getJson();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_speaking);

        initView();
        getJson();
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.listview_speaking_vote);
        btnShare = (Button) findViewById(R.id.button_speaking_share);
        btnBack = (Button) findViewById(R.id.button_speaking_back);
        llStatis = (LinearLayout) findViewById(R.id.ll_speaking_gaopintongji);
        txtRefresh = (TextView) findViewById(R.id.txt_speaking_refreshtitle);

        btnShare.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        llStatis.setOnClickListener(this);
        mListView.setOnItemClickListener(this);
    }

    private void getJson() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject;
                SpeakingBean speaking;
                try {
                    jsonObject = new JSONObject(response);
                    int result = jsonObject.getInt("code");
                    if (result == 0) {
                        JSONArray data = jsonObject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            jsonObject = data.getJSONObject(i);
                            speaking = new SpeakingBean();
                            speaking.uname = jsonObject.getString("uname");
                            speaking.message = jsonObject.getString("message");
                            speaking.examroom = jsonObject.getString("examroom");
                            speaking.vtime = jsonObject.getString("vtime");
                            speakingList.add(speaking);
                        }
                    } else {
                        LogUtil.e("code!=0 --DATA-BACK", "读取页面失败： " + jsonObject.getString("message"));
                    }
                    // 第二步，将数据放到适配器中
                    adapter = new SpeakingAdapter(SpeakingActivity.this, speakingList);
                    mListView.setAdapter(adapter);
                    // progressdialogcancel();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                txtRefresh.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                txtRefresh.setVisibility(View.GONE);
            }
        });
        stringRequest.setTag("speaking");
        MyApplication.getHttpQueue().add(stringRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_speaking_share:
                startActivity(new Intent(this, SpeakingShareActivity1.class));
                overridePendingTransition(0, 0); // ****设置无跳转动画
                break;
            case R.id.ll_speaking_gaopintongji:
                startActivity(new Intent(this, SpeakingStatisActivity.class));
                overridePendingTransition(0, 0); // ****设置无跳转动画
                break;
            case R.id.button_speaking_back:
                finish();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SpeakingBean localspeaking = speakingList.get(position);
        Intent it = new Intent(this, SpeakingDetailActivity.class);
        it.putExtra("localspeaking", localspeaking);
        startActivity(it);
    }

}