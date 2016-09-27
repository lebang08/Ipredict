package com.woyuce.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.woyuce.activity.Adapter.WeiboPhotoWallAdapter;
import com.woyuce.activity.Utils.LogUtil;
import com.woyuce.activity.Utils.ToastUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by Administrator on 2016/9/27.
 */
public class WeiboPhotoWallActivity extends Activity implements AdapterView.OnItemClickListener {

    private PhotoView mImg;

    private GridView mGridView;
    private WeiboPhotoWallAdapter mAdapter;
    private List<String> mList = new ArrayList<>();

    private static final int CODE_CAPTURE_IMG = 0x01;
    private String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weibophotowall);

        initView();
    }

    /**
     * 初始化布局
     */
    private void initView() {
        mImg = (PhotoView) findViewById(R.id.img_weibophotowall);
        mGridView = (GridView) findViewById(R.id.gridview_activity_weibophotowall);
        mGridView.setOnItemClickListener(this);

        for (int i = 0; i < 90; i++) {
            mList.add("a");
        }
        mAdapter = new WeiboPhotoWallAdapter(this, mList);
        mGridView.setAdapter(mAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CODE_CAPTURE_IMG:
                if (resultCode == Activity.RESULT_OK) {
                    String sdStatus = Environment.getExternalStorageState();
                    // 检测sd是否可用
                    if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
                        LogUtil.i("TestFile", "SD card is not avaiable/writeable right now.");
                        return;
                    }
                    String name = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
                    Toast.makeText(this, name, Toast.LENGTH_LONG).show();
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式

                    FileOutputStream b = null;
                    //???????????????????????????????为什么不能直接保存在系统相册位置呢？？？？？？？？？？？？
                    File file = new File("/sdcard/lebang/");
                    file.mkdirs();// 创建文件夹
                    fileName = "/sdcard/myImage/" + name;

                    //TODO 输入流写入图片
                    try {
                        b = new FileOutputStream(fileName);
                        // 把数据写入文件
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            b.flush();
                            b.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    //将PhotoView设为可见
                    mImg.setVisibility(View.VISIBLE);
                    // 将图片显示在PhotoView里
                    mImg.setImageBitmap(bitmap);
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
//                ToastUtil.showMessage(this, "去拍照");
                //TODO 调用相机也是需要6.0权限的
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // create a file to save the image
                // fileUri = getExternalFilesDir("lebang_test").getAbsolutePath();

                // 此处这句intent的值设置关系到后面的onActivityResult中会进入那个分支，即关系到data是否为null，如果此处指定，则后来的data为null
                // set the image file name
                // intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(intent, CODE_CAPTURE_IMG);
                break;
            default:
                ToastUtil.showMessage(this, "点右上角选中该图片，点其他部位，跳转看详细图");
                break;
        }
    }

    public void toNext(View view) {
        Intent intent = new Intent(this, WeiboPulishActivity.class);
        intent.putExtra("fileName", fileName);
        startActivity(intent);
        mImg.setVisibility(View.INVISIBLE);
    }
}
