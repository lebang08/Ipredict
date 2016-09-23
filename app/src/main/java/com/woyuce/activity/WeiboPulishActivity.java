package com.woyuce.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Administrator on 2016/9/23.
 */
public class WeiboPulishActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weibopulish);
    }

    public void toPulish(View view) {
        finish();
    }

}
