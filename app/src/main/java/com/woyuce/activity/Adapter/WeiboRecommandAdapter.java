package com.woyuce.activity.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.woyuce.activity.Bean.WeiboBean;
import com.woyuce.activity.R;

import java.util.List;

public class WeiboRecommandAdapter extends BaseAdapter {

    private List<WeiboBean> mList;
    private LayoutInflater mInflater;

    public WeiboRecommandAdapter(Context context, List<WeiboBean> data) {
        mList = data;
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

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder ;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.gvitem_weiborecommand, null);
            viewHolder.imgPath = (ImageView) convertView.findViewById(R.id.img_item_lesson);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

//        String url = mList.get(position).pulish_image;
//        viewHolder.imgPath.setTag(url);

        DisplayImageOptions options = new DisplayImageOptions.Builder().
                showImageOnLoading(R.mipmap.img_error)
                .showImageOnFail(R.mipmap.img_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(new RoundedBitmapDisplayer(300))
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoader.getInstance().displayImage("http://www.iyuce.com/images/2016/sy_bsyc.png", viewHolder.imgPath, options);

        return convertView;
    }

    class ViewHolder {
        public ImageView imgPath;
    }
}