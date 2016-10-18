package com.woyuce.activity.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.woyuce.activity.R;
import com.woyuce.activity.Utils.PreferenceUtil;

/**
 * Created by Administrator on 2016/9/22.
 */
public class WebActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTitle;
    private WebView web;
    private ImageView imgClose, imgBack;
    private LinearLayout mLinearlayout;

    private String local_URL, local_title, local_color;

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
    protected void onDestroy() {
        super.onDestroy();
        ViewGroup view = (ViewGroup) getWindow().getDecorView();
        view.removeAllViews();
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
        local_title = getIntent().getStringExtra("TITLE");
        local_color = getIntent().getStringExtra("COLOR");

        mTitle = (TextView) findViewById(R.id.web_title);
        web = (WebView) findViewById(R.id.web);
        imgClose = (ImageView) findViewById(R.id.img_close);
        imgBack = (ImageView) findViewById(R.id.img_back);
        mLinearlayout = (LinearLayout) findViewById(R.id.linearlayout_webview_title);

        imgClose.setOnClickListener(this);
        imgBack.setOnClickListener(this);

        //根据参数描绘不同的标题头
        drawTitleBar(local_title, local_color);
    }

    /**
     * 根据参数描绘不同的标题头
     */
    private void drawTitleBar(String title, String color) {
        mTitle.setText(title);
        mLinearlayout.setBackgroundColor(Color.parseColor(color));
    }

    private void initEvent() {
        progressdialogshow(this);
        web.loadUrl(local_URL);
        WebSettings webSettings = web.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);

        //设置浏览器标识
        String localVersion = PreferenceUtil.getSharePre(WebActivity.this).getString("localVersion", "1.0");
        webSettings.setUserAgentString(web.getSettings().getUserAgentString() + "; woyuce/" + localVersion);

        // H5处理localstrage
        webSettings.setDomStorageEnabled(true);
        // H5的缓存打开
        webSettings.setAppCacheEnabled(true);
        // 根据setAppCachePath(String appCachePath)提供的路径，在H5使用缓存过程中生成的缓存文件。
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        webSettings.setAppCachePath(appCachePath);
        // 设置缓冲大小8M
        webSettings.setAppCacheMaxSize(1024 * 1024 * 8);

        webSettings.setAllowFileAccess(true);
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
                //网站登录同步App登录，Cookie设置
                String cookieString = PreferenceUtil.getSharePre(WebActivity.this).getString("userId", "");
                CookieSyncManager.createInstance(WebActivity.this);
                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.setCookie(url, "iup.token=" + cookieString + ";Max-Age=3600" + ";Domain=.iyuce.com" + ";Path=/");
                CookieSyncManager.getInstance().sync();
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