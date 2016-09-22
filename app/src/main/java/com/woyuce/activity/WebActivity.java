package com.woyuce.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/9/22.
 */
public class WebActivity extends BaseActivity implements View.OnClickListener {

    private TextView title;
    private WebView web;
    private ImageView imgClose, imgBack;
    private LinearLayout mLinearlayout;

    private String local_URL = null;
    private String local_code = null;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (web.canGoBack()) {
                web.goBack();
            } else {
                WebActivity.this.finish();
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        initView();
        initEvent();
    }

    private void initView() {
        local_URL = getIntent().getStringExtra("URL");
        local_code = getIntent().getStringExtra("CODE");

        title = (TextView) findViewById(R.id.web_title);
        web = (WebView) findViewById(R.id.web);
        imgClose = (ImageView) findViewById(R.id.img_close);
        imgBack = (ImageView) findViewById(R.id.img_back);
        mLinearlayout = (LinearLayout) findViewById(R.id.linearlayout_webview_title);

        imgClose.setOnClickListener(this);
        imgBack.setOnClickListener(this);

        //根据传入的参数选择不同的样式
        if (local_code.equals("zhibo")) {
            title.setText("网络班直播报名");
            mLinearlayout.setBackgroundColor(Color.parseColor("#1e87e2"));
        } else if (local_code.equals("lubo")) {
            //什么都不做,因为xml文件就是录播的样式
        } else if (local_code.equals("gongyi")) {
            title.setText("我预测腾讯课堂");
            mLinearlayout.setBackgroundColor(Color.parseColor("#366090"));
        } else if (local_code.equals("witcontent")) {
            title.setText("写作答案");
            mLinearlayout.setBackgroundColor(Color.parseColor("#9f6eaf"));
        } else if (local_code.equals("apply")) {
            title.setText("集训营申请");
            mLinearlayout.setBackgroundColor(Color.parseColor("#f7941d"));
        } else if (local_code.equals("examfinish")) {
            title.setText("考后笔试答案");
            mLinearlayout.setBackgroundColor(Color.parseColor("#408f40"));
        }
    }

//    /**
//     * 将cookie同步到WebView
//     *
//     * @param url    WebView要加载的url
//     * @param cookie 要同步的cookie
//     * @return true 同步cookie成功，false同步cookie失败
//     * @Author JPH
//     */
//    public static boolean syncCookie(String url, String cookie) {
//        // if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//        // CookieSyncManager.createInstance(context);
//        // }
//        CookieManager cookieManager = CookieManager.getInstance();
//        String oldcookie = cookieManager.getCookie(url);
//        LogUtil.e("oldcookie = " + oldcookie);
//        cookieManager.setCookie(url, cookie.substring(0, cookie.indexOf("1")));//
//        如果没有特殊需求，这里只需要将session, id以 "key=value" 形式作为cookie即可
//        String newCookie = cookieManager.getCookie(url);
//        LogUtil.e("newCookie = " + newCookie);
//        return TextUtils.isEmpty(newCookie) ? false : true;
//    }

    private void initEvent() {
        progressdialogshow(this);
        web.loadUrl(local_URL);
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setSupportZoom(true);
        web.getSettings().setBuiltInZoomControls(true);
        web.getSettings().setUseWideViewPort(true);
        web.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        web.getSettings().setLoadWithOverviewMode(true);

        // H5处理localstrage
        web.getSettings().setDomStorageEnabled(true);
        // H5的缓存打开
        web.getSettings().setAppCacheEnabled(true);
        // 根据setAppCachePath(String appCachePath)提供的路径，在H5使用缓存过程中生成的缓存文件。
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        web.getSettings().setAppCachePath(appCachePath);
        // 设置缓冲大小8M
        web.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);

        web.getSettings().setAllowFileAccess(true);
        web.setWebChromeClient(new WebChromeClient());
        web.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                view.loadUrl("file:///android_asset/index.html");
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http:") || url.startsWith("https:")) {
                    return false;
                }
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressdialogcancel();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                if (web.canGoBack()) {
                    web.goBack();
                } else {
                    WebActivity.this.finish();
                }
                break;
            case R.id.img_close:
                finish();
                break;
        }
    }
}