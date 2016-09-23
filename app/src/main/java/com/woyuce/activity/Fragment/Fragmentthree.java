package com.woyuce.activity.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;

import com.woyuce.activity.R;
import com.woyuce.activity.Utils.ToastUtil;

@SuppressLint("SimpleDateFormat")
public class Fragmentthree extends Fragment implements View.OnClickListener {

    private Button btn_apply;
    private ImageView imgLoading;
    private WebView web;

    private String URL_CAM = "http://www.iyuce.com/m/appjxy.html";

    @Override
    public void onStart() {
        super.onStart();
        // initWebView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab3, container, false);

        btn_apply = (Button) view.findViewById(R.id.btn_tab3_apply);
        btn_apply.setOnClickListener(this);

//		initView(view);
//		initWebView();
        return view;
    }

//	private void initView(View view) {
//		web = (WebView) view.findViewById(R.id.web_tab3);
//		imgLoading = (ImageView) view.findViewById(R.id.img_tab3_loading);
//		btn_apply = (Button) view.findViewById(R.id.btn_tab3_apply);
//
//		btn_apply.setOnClickListener(this);
//	}
//
//	@SuppressLint("SetJavaScriptEnabled")
//	private void initWebView() {
//		web.loadUrl(URL_CAM);
//		// web.getSettings().setJavaScriptEnabled(true);
//		web.getSettings().setSupportZoom(true);
//		web.getSettings().setBuiltInZoomControls(true);
//		web.getSettings().setUseWideViewPort(true);
//		web.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
//		web.getSettings().setLoadWithOverviewMode(true);
//
//		web.setWebChromeClient(new WebChromeClient());
//		web.setWebViewClient(new WebViewClient() {
//			@Override
//			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//				super.onReceivedError(view, errorCode, description, failingUrl);
//				view.loadUrl("file:///android_asset/index.html");
//			}
//
//			@Override
//			public void onPageFinished(WebView view, String url) {
//				web.setVisibility(View.VISIBLE);
//				imgLoading.setVisibility(View.GONE);
//			}
//		});
//	}

    @Override
    public void onClick(View v) {
        //TODO SnackBar
        Snackbar.make(v, "试一试Snacker", Snackbar.LENGTH_LONG).setAction("点我", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showMessage(getActivity(), "nothing");
            }
        }).show();
//        Intent intent = new Intent(getActivity(), WebActivity.class);
//        intent.putExtra("URL", "http://www.iyuce.com/m/appfbbsq");
//        intent.putExtra("CODE", "apply");
//        startActivity(intent);
    }
}