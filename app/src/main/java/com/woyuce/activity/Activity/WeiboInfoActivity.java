package com.woyuce.activity.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.woyuce.activity.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/11.
 */
public class WeiboInfoActivity extends Activity {

    private String local_headurl, local_body, local_time, local_author;

    private TextView mTxtAuthor, mTxtBody, mTxtTime;
    private ImageView mIconHead;

    private ListView mListView;
    private List<String> mDataList = new ArrayList<>();
    private ArrayAdapter mAdapter;

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

        mTxtAuthor = (TextView) findViewById(R.id.txt_weiboinfo_username);
        mTxtBody = (TextView) findViewById(R.id.txt_weiboinfo_body);
        mTxtTime = (TextView) findViewById(R.id.txt_weiboinfo_time);
        mIconHead = (ImageView) findViewById(R.id.img_weiboinfo_headphoto);
        mListView = (ListView) findViewById(R.id.listview_activity_weiboinfo);

        mTxtAuthor.setText(local_author);
        mTxtBody.setText(local_body);
        mTxtTime.setText(local_time);
        DisplayImageOptions options = new DisplayImageOptions.Builder().
                showImageOnLoading(R.mipmap.img_duck)
                .showImageOnFail(R.mipmap.img_duck)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(new RoundedBitmapDisplayer(10))
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoader.getInstance().displayImage(local_headurl, mIconHead, options);

//        mListView.
        for (int i = 0; i < 100; i++) {
            mDataList.add("java " + i);
        }
        mAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, mDataList);
        mListView.setAdapter(mAdapter);
    }

    public void back(View view) {
        finish();
    }

}
