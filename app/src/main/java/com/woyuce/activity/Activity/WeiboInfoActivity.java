package com.woyuce.activity.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.woyuce.activity.Adapter.WeiboInfoAdapter;
import com.woyuce.activity.Application.AppContext;
import com.woyuce.activity.Bean.WeiboBean;
import com.woyuce.activity.R;
import com.woyuce.activity.Utils.LogUtil;
import com.woyuce.activity.Utils.PreferenceUtil;
import com.woyuce.activity.Utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/11.
 */
public class WeiboInfoActivity extends Activity implements AdapterView.OnItemClickListener {

    private String local_headurl, local_body, local_time, local_author, local_reply_count, local_token;
    private int local_microblog_id;

    //把URL抽出去
    private String URL = "http://api.iyuce.com/v1/bbs/getrootcomments?MicroblogId=";
    private String URL_SUBCOMMIT = "http://api.iyuce.com/v1/bbs/subcomment";


    private TextView mTxtAuthor, mTxtBody, mTxtTime, mTxtReplyCount;
    private ImageView mIconHead;

    private ListView mListView;
    private WeiboInfoAdapter mAdapter;
    private List<WeiboBean> mDataList = new ArrayList<>();

    @Override
    protected void onStop() {
        super.onStop();
        AppContext.getHttpQueue().cancelAll("weiboinfo");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weiboinfo);

        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        local_body = intent.getStringExtra("local_body");
        local_time = intent.getStringExtra("local_time");
        local_author = intent.getStringExtra("local_author");
        local_headurl = intent.getStringExtra("local_headurl");
        local_reply_count = intent.getStringExtra("local_reply_count");
        local_token = intent.getStringExtra("local_token");
        local_microblog_id = intent.getIntExtra("local_microblog_id", 0);

        mTxtAuthor = (TextView) findViewById(R.id.txt_weiboinfo_username);
        mTxtBody = (TextView) findViewById(R.id.txt_weiboinfo_body);
        mTxtTime = (TextView) findViewById(R.id.txt_weiboinfo_time);
        mTxtReplyCount = (TextView) findViewById(R.id.txt_weiboinfo_replycount);
        mIconHead = (ImageView) findViewById(R.id.img_weiboinfo_headphoto);
        mListView = (ListView) findViewById(R.id.listview_activity_weiboinfo);
        mListView.setOnItemClickListener(this);

        mTxtAuthor.setText(local_author);
        mTxtBody.setText(local_body);
        mTxtTime.setText(local_time);
        mTxtReplyCount.setText("评论数  " + local_reply_count);
        DisplayImageOptions options = new DisplayImageOptions.Builder().
                showImageOnLoading(R.mipmap.img_duck)
                .showImageOnFail(R.mipmap.img_duck)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(new RoundedBitmapDisplayer(10))
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoader.getInstance().displayImage(local_headurl, mIconHead, options);

        //ListView填充数据
        requestJson();
    }

    private void requestJson() {
        StringRequest weiboDataRequest = new StringRequest(Request.Method.GET, URL + local_microblog_id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject obj;
                JSONArray arr;
                try {
                    obj = new JSONObject(response);
                    if (obj.getInt("code") == 0) {
                        arr = obj.getJSONArray("data");
                        LogUtil.e("arr = ?? " + arr);
                        WeiboBean weibo;
                        for (int i = 0; i < arr.length(); i++) {
                            obj = arr.getJSONObject(i);
                            weibo = new WeiboBean();
                            weibo.author = obj.getString("author");
                            weibo.body = obj.getString("body");
                            weibo.date_created = obj.getString("date_created");
                            weibo.subject = obj.getString("subject");
                            weibo.commented_object_id = obj.getString("commented_object_id");
                            weibo.parent_id = obj.getInt("parent_id");
                            mDataList.add(weibo);
                        }
                    } else {
                        LogUtil.e("code!=0 Data-BACK", "读取页面失败： " + obj.getString("message"));
                    }
                    // 将数据放到适配器中
                    mAdapter = new WeiboInfoAdapter(WeiboInfoActivity.this, mDataList);
                    mListView.setAdapter(mAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, null) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + local_token);
                return headers;
            }
        };
        weiboDataRequest.setTag("weiboinfo");
        AppContext.getHttpQueue().add(weiboDataRequest);
    }

    //评论微博
    private void requestSubcommit(final int parent_id, final String subject, final String commented_object_id, final String body) {
        StringRequest weiboSubcommitRequest = new StringRequest(Request.Method.POST, URL_SUBCOMMIT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogUtil.e("评论?? = " + response);
                JSONObject obj;
                try {
                    obj = new JSONObject(response);
                    if (obj.getInt("code") == 0) {
                        ToastUtil.showMessage(WeiboInfoActivity.this, "评论成功啦");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.e("wrong message = " + volleyError);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + local_token);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                LogUtil.e("param = " + body + local_author + subject);
                map.put("author", local_author);
                map.put("user_id", PreferenceUtil.getSharePre(WeiboInfoActivity.this).getString("userId", ""));
                map.put("owner_id", PreferenceUtil.getSharePre(WeiboInfoActivity.this).getString("userId", ""));
                map.put("body", body);
                map.put("subject", subject);
                map.put("commented_object_id", commented_object_id);
                return map;
            }
        };
        weiboSubcommitRequest.setTag("weiboinfo");
        AppContext.getHttpQueue().add(weiboSubcommitRequest);
    }

    public void back(View view) {
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final int parent_id = mDataList.get(position).parent_id;
        final String subject = mDataList.get(position).subject;
        final String commented_object_id = mDataList.get(position).commented_object_id;

        final EditText mEdit = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("请输入评论内容")
                .setView(mEdit)
                .setPositiveButton("确定评论", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String body = mEdit.getText().toString();
                        requestSubcommit(parent_id, subject, commented_object_id, body);
                    }
                }).show();
    }
}
