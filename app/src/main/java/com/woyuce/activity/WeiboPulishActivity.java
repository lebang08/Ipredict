package com.woyuce.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import com.woyuce.activity.Adapter.WeiboPulishAdapter;
import com.woyuce.activity.Utils.LogUtil;
import com.woyuce.activity.Utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/9/23.
 */
public class WeiboPulishActivity extends Activity {

    private GridView mGridViewShowPhoto;
    private List<String> mList = new ArrayList<>();
    private WeiboPulishAdapter mAdapter;

    private String local_fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weibopulish);

        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //GridView中添加数据源
        local_fileName = getIntent().getStringExtra("fileName");
        mList.add("file://" + local_fileName);
        mAdapter = new WeiboPulishAdapter(this, mList);
        mGridViewShowPhoto.setAdapter(mAdapter);

        //TODO 数组没有加长，难道要用一个数组在两个Activity之间持续传递？
        LogUtil.e("mList = " + mList);
    }

    private void initView() {
        mGridViewShowPhoto = (GridView) findViewById(R.id.gridview_activity_weibopulish);
    }

    public void doPulish(View view) {
//        ToastUtil.showMessage(this, "发表成功啦");
//        finish();
        ToastUtil.showMessage(this, "继续添加图片");
        startActivity(new Intent(this, WeiboPhotoWallActivity.class));
    }
}