package com.woyuce.activity.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.woyuce.activity.Activity.WeiboPublishActivity;
import com.woyuce.activity.Adapter.WeiboRecommandAdapter;
import com.woyuce.activity.Bean.WeiboBean;
import com.woyuce.activity.R;

import java.util.ArrayList;
import java.util.List;

public class Fragmentthree extends Fragment implements View.OnClickListener {

    private ViewFlipper mFlipper;

    private ImageButton mImgBack;
    private Button mBtnToPulish;
    private GridView mGridView;
    private List<WeiboBean> dataList = new ArrayList<>();
    private WeiboRecommandAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab3, container, false);

        initView(view);

        return view;
    }

    private void initView(View view) {
        mImgBack = (ImageButton) view.findViewById(R.id.imgbt_tab3_back);
        mBtnToPulish = (Button) view.findViewById(R.id.btn_fragmenttab3_topulish);

        mImgBack.setOnClickListener(this);
        mBtnToPulish.setOnClickListener(this);

        //ViewFlipper加入轮播图
        mFlipper = (ViewFlipper) view.findViewById(R.id.vf_fragmenttab3_weibo);
        for (int i = 0; i < 4; i++) {
            ImageView mImg = new ImageView(getActivity());
            mImg.setBackgroundResource(R.mipmap.background_music);
            mFlipper.addView(mImg);
        }
        mFlipper.setInAnimation(getActivity(), R.anim.left_in);
        mFlipper.setOutAnimation(getActivity(), R.anim.left_out);
        mFlipper.startFlipping();

        //GridView填充数据
        mGridView = (GridView) view.findViewById(R.id.gv_fragmenttab3_weibo);
        WeiboBean weibo;
        for (int i = 0; i < 8; i++) {
            weibo = new WeiboBean();
            weibo.comment = "评论数" + i;
            weibo.like = "赞" + i;
            weibo.pulish_image = "what" + i;
            weibo.pulish_message = "shu" + i;
            dataList.add(weibo);
        }

        mAdapter = new WeiboRecommandAdapter(getActivity(), dataList);
        mGridView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgbt_tab3_back:
                Snackbar.make(v, "别逗了,这能返回去哪里", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.btn_fragmenttab3_topulish:
                startActivity(new Intent(getActivity(), WeiboPublishActivity.class));
                break;
        }
    }
}