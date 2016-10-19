package com.woyuce.activity.Activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
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
import com.woyuce.activity.Utils.LogUtil;
import com.woyuce.activity.Utils.PreferenceUtil;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Administrator on 2016/9/22.
 */
public class WebActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTitle;
    private WebView web;
    private ImageView imgClose, imgBack, ImgLoading;
    private LinearLayout mLinearlayout;

    //初始化cookieManager
    private CookieManager cookieManager;

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
        ImgLoading.setVisibility(View.VISIBLE);
//        cookieManager.removeAllCookie();
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
        ImgLoading = (ImageView) findViewById(R.id.img_loading);
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
                ImgLoading.setVisibility(View.GONE);
                LogUtil.e("onPageFinished");
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                LogUtil.e("onPageStarted");

                //网站登录同步App登录，Cookie设置
                String cookie_string = PreferenceUtil.getSharePre(WebActivity.this).getString("userId", "");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                cookie_string = cookie_string + "_" + sdf.format(new Date());
                //加密
                String decode_cookie_string = encode(cookie_string);
                CookieSyncManager.createInstance(WebActivity.this);
                cookieManager = CookieManager.getInstance();
                cookieManager.removeAllCookie();

//                String get_cookie = cookieManager.getCookie(url);
//                if(get_cookie.contains("'iup.token=' + decode_cookie_string")){
//                    get_cookie.replaceAll("'iup.token=' + decode_cookie_string","empty");
//                    LogUtil.e("empty ? = " + get_cookie);
//                }

                LogUtil.e("cookieManager.getCookie(url) ---0 = " + cookieManager.getCookie(url));
                cookieManager.setCookie(url, "iup.token=" + decode_cookie_string + ";Max-Age=3600" + ";Domain=.iyuce.com" + ";Path=/");
                LogUtil.e("cookieManager.getCookie(url) ---1 = " + cookieManager.getCookie(url));
                CookieSyncManager.getInstance().sync();
            }
        });
        web.loadUrl(local_URL);
    }

    /**
     * AES加密
     *
     * @param content 需要加密的内容
     * @param password  加密密码
     * @return
     */
    private static String Key = "859c44adb1c34796bdb49034f85e1721";

    public static String encode(String stringToEncode) throws NullPointerException {

        try {
            SecretKeySpec skeySpec = getKey(Key);
            byte[] clearText = stringToEncode.getBytes("UTF8");
            final byte[] iv = new byte[16];
            Arrays.fill(iv, (byte) 0x00);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivParameterSpec);
            String encrypedValue = Base64.encodeToString(cipher.doFinal(clearText), Base64.DEFAULT);
            return encrypedValue;

        } catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException
                | BadPaddingException | NoSuchPaddingException | IllegalBlockSizeException
                | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取密钥
     *
     * @param password
     * @return
     * @throws UnsupportedEncodingException
     */
    private static SecretKeySpec getKey(String password) throws UnsupportedEncodingException {
        int keyLength = 256;
        byte[] keyBytes = new byte[keyLength / 8];
        Arrays.fill(keyBytes, (byte) 0x0);
        byte[] passwordBytes = password.getBytes("UTF-8");
        int length = passwordBytes.length < keyBytes.length ? passwordBytes.length : keyBytes.length;
        System.arraycopy(passwordBytes, 0, keyBytes, 0, length);
        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
        return key;
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