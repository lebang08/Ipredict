package com.woyuce.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.woyuce.activity.Utils.LogUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Administrator on 2016/9/23.
 */
public class WeiboPulishActivity extends Activity {

    private ImageView mImge;

    private String fileUri;
    private static final int CODE_CAPTURE_IMG = 0x01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weibopulish);
        mImge = (ImageView) findViewById(R.id.img_weibopulish);
    }

    //TODO 调用相机也是需要6.0权限的
    public void doPulish(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // create a file to save the image
        fileUri = getExternalFilesDir("lebang_test").getAbsolutePath();

        // 此处这句intent的值设置关系到后面的onActivityResult中会进入那个分支，即关系到data是否为null，如果此处指定，则后来的data为null
        // set the image file name
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        startActivityForResult(intent, CODE_CAPTURE_IMG);
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
                    String fileName = "/sdcard/myImage/" + name;

//                    //TODO Imageload加载图片
//                    DisplayImageOptions options = new DisplayImageOptions.Builder()
//                            .cacheInMemory(true).cacheOnDisk(true)
//                            .bitmapConfig(Bitmap.Config.RGB_565)
//                            .displayer(new RoundedBitmapDisplayer(30))
//                            .build();
//                    String imageUrl = ImageDownloader.Scheme.FILE.wrap(fileName);
//                    ImageLoader.getInstance().displayImage(imageUrl, mImge, options);

                    //TODO 输入流调图片
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
                    // 将图片显示在ImageView里
                    mImge.setImageBitmap(bitmap);
                }
                break;
        }
    }
}