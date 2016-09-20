package com.woyuce.activity.Application;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.woyuce.activity.Bean.UserInfo;

public class MyApplication extends Application {

    private static MyApplication mInstance = null;
    private UserInfo info;

    private static RequestQueue mQueue;

    public static MyApplication getInstance() {
        return mInstance;
    }

    public UserInfo getInfo() {
        return this.info;
    }

    /**
     * 初始化全局
     */
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        mQueue = Volley.newRequestQueue(getApplicationContext());

        // 创建默认的ImageLoader配置参数
//		ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
//		ImageLoader.getInstance().init(configuration);
//		File cacheDir = StorageUtils.getCacheDirectory(this);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
                .diskCacheExtraOptions(480, 800, null)
                .threadPoolSize(3) // default
                .threadPriority(Thread.NORM_PRIORITY - 1) // default
                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(50 * 1024 * 1024))
                .memoryCacheSize(50 * 1024 * 1024)
                .memoryCacheSizePercentage(13) // default
//		        .diskCache(new UnlimitedDiscCache(cacheDir)) // default
                .diskCacheSize(10 * 1024 * 1024)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .imageDownloader(new BaseImageDownloader(this)) // default
                .imageDecoder(new BaseImageDecoder(false)) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);
    }

    public static RequestQueue getHttpQueue() {
        return mQueue;
    }

    public void setInfo(UserInfo paramUserInfo) {
        this.info = paramUserInfo;
    }

}