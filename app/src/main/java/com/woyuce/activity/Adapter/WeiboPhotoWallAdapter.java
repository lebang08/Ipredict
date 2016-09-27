package com.woyuce.activity.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.woyuce.activity.R;

import java.util.List;

/**
 * Created by Administrator on 2016/9/27.
 */
public class WeiboPhotoWallAdapter extends BaseAdapter {

    private List<String> mList;
    private LayoutInflater mInflater;

    public WeiboPhotoWallAdapter(Context context, List<String> mList) {
        this.mList = mList;
        mInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.gvitem_weibophotowawll, null);
            viewHolder.mImg = (ImageView) convertView.findViewById(R.id.img_item_weibophotowall);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (position == 0)
            viewHolder.mImg.setBackgroundResource(R.mipmap.block_answer);
        else
            viewHolder.mImg.setBackgroundResource(R.mipmap.block_speaking);
        return convertView;
    }

    class ViewHolder {
        public ImageView mImg;
    }
}
