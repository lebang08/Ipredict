package com.woyuce.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.woyuce.activity.Adapter.SpeakingSearchAdapter;
import com.woyuce.activity.Application.MyApplication;
import com.woyuce.activity.Bean.SpeakingSearch;
import com.woyuce.activity.Utils.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/22.
 */
public class SpeakingSearchActivity extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private LinearLayout llback;
    private ListView lvSearch;
    private Button btnBack;
    private TextView txtNull;

    private String localsearch;
    private String URL_SEARCH = "http://iphone.ipredicting.com/kysubSearch.aspx";
    private List<SpeakingSearch> searchList = new ArrayList<>();

    @Override
    protected void onStop() {
        super.onStop();
        MyApplication.getHttpQueue().cancelAll("search");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speakingsearch);

        initView();
        getJson();
    }

    private void initView() {
        Intent it_search = getIntent();
        localsearch = it_search.getStringExtra("localsearch");

        txtNull = (TextView) findViewById(R.id.txt_search_null);
        btnBack = (Button) findViewById(R.id.button_search_back);
        lvSearch = (ListView) findViewById(R.id.listview_search);
        llback = (LinearLayout) findViewById(R.id.ll_search_back);

        btnBack.setOnClickListener(this);
        llback.setOnClickListener(this);
        lvSearch.setOnItemClickListener(this);
    }

    public void getJson() {
        StringRequest strinRequest = new StringRequest(Request.Method.POST, URL_SEARCH, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject;
                SpeakingSearch search;
                try {
                    jsonObject = new JSONObject(response);
                    int result = jsonObject.getInt("code");
                    if (result == 0) {
                        JSONArray data = jsonObject.getJSONArray("data");
                        if (data.length() == 0) {
                            txtNull.setText("没有找到您需要的内容呢，亲...");
                        }
                        for (int i = 0; i < data.length(); i++) {
                            jsonObject = data.getJSONObject(i);
                            search = new SpeakingSearch();
                            search.subCategoryid = jsonObject.getString("subCategoryid");
                            search.subname = jsonObject.getString("subname");
                            searchList.add(search);
                        }
                    } else {
                        LogUtil.e("code!=0 Data-BACK", "读取页面失败： " + jsonObject.getString("message"));
                        txtNull.setText("您没有输入搜索内容哦，亲!");
                    }
                    // 第二步，将数据放到适配器中
                    SpeakingSearchAdapter adapter = new SpeakingSearchAdapter(SpeakingSearchActivity.this, searchList);
                    lvSearch.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Log.e("DATA-BACK", "JSON接口返回的信息： " + response);
                // 这是返回的完整JSON信息，未解析，如果要JSON数据，可以从这里拿
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.e("Wrong_BACK", "联接错误原因： " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> hashMap = new HashMap<>();
                hashMap.put("key", localsearch);
                return hashMap;
            }
        };
        strinRequest.setTag("search");
        MyApplication.getHttpQueue().add(strinRequest);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SpeakingSearch search = searchList.get(position);
        String localsubCategoryid = search.subCategoryid;
        Intent it_subContent = new Intent(this, SpeakingContentActivity.class);
        it_subContent.putExtra("localsubCategoryid", localsubCategoryid);
        startActivity(it_subContent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_search_back:
                Intent it_speaking = new Intent(this, SpeakingActivity.class);
                startActivity(it_speaking);
                overridePendingTransition(0, 0); // ****设置无跳转动画
                break;
            case R.id.button_search_back:
                finish();
                break;
        }
    }
}