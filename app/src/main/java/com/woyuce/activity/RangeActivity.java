package com.woyuce.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.woyuce.activity.Adapter.RangeAdapter;
import com.woyuce.activity.Application.MyApplication;
import com.woyuce.activity.Bean.Range;
import com.woyuce.activity.Utils.LogUtil;
import com.woyuce.activity.Utils.PreferenceUtil;
import com.woyuce.activity.Utils.ToastUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

public class RangeActivity extends BaseActivity implements OnItemClickListener, OnClickListener {

	private ImageView mBack, mGuide;
	private Button mBtnClear;
	private ListView mListView;

	private String localtoken;
	private String URL = "http://api.iyuce.com/v1/exam/free";
	private List<Range> rangeList = new ArrayList<>();

	@Override
	protected void onStop() {
		super.onStop();
		MyApplication.getHttpQueue().cancelAll("range");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_range);

		initView();
		requestJson();
	}

	private void initView() {
		mBack = (ImageView) findViewById(R.id.arrow_back);
		mGuide = (ImageView) findViewById(R.id.img_range_guide);
		mBtnClear = (Button) findViewById(R.id.btn_range_clearcache);
		mListView = (ListView) findViewById(R.id.listview_activity_rang);

		mBack.setOnClickListener(this);
		mGuide.setOnClickListener(this);
		mBtnClear.setOnClickListener(this);
		mListView.setOnItemClickListener(this);

		// 第一次引导的动画
		boolean b = PreferenceUtil.getSharePre(RangeActivity.this).contains("imgclearguide");
		if (b == true) {
			mGuide.setVisibility(View.GONE);
		}
	}

	private void requestJson() {
		progressdialogshow(this);
		StringRequest rangeRequest = new StringRequest(Method.POST, URL, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				doSuccess(response);
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				progressdialogcancel();
				LogUtil.e("Wrong-BACK", "联接错误原因： " + error.getMessage());
			}
		}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> headers = new HashMap<String, String>();
				localtoken = PreferenceUtil.getSharePre(RangeActivity.this).getString("localtoken", "");
				headers.put("Authorization", "Bearer " + localtoken);
				return headers;
			}
		};
		rangeRequest.setTag("range");
		MyApplication.getHttpQueue().add(rangeRequest);
	}

	/**
	 * 请求成功后调用
	 *
	 * @param response
	 */
	private void doSuccess(String response) {
		JSONObject jsonObject;
		Range range;
		try {
			jsonObject = new JSONObject(response);
			int result = jsonObject.getInt("code");
			if (result == 0) {
				JSONArray data = jsonObject.getJSONArray("data");
				for (int i = 0; i < data.length(); i++) {
					jsonObject = data.getJSONObject(i);
					range = new Range();
					range.setId(jsonObject.getString("month_id"));
					range.setTitle(jsonObject.getString("title"));
					rangeList.add(range);
				}
			} else {
				LogUtil.e("code!=0 --DATA-BACK", "读取页面失败： " + jsonObject.getString("message"));
			}
			// 第二步，将数据放到适配器中
			RangeAdapter adapter = new RangeAdapter(RangeActivity.this, rangeList);
			mListView.setAdapter(adapter);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		progressdialogcancel();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//		Range range = (Range) rangeList.get(position);
//		String localMonthid = range.getId();
//		String localTitle = range.getTitle();
//		Intent intent = new Intent(this, LessonActivity.class);
//		Bundle bundle = new Bundle();
//		bundle.putString("localTitle", localTitle);
//		bundle.putString("localMonthid", localMonthid);
//		intent.putExtras(bundle);
//		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_range_clearcache:
				ImageLoader.getInstance().clearDiskCache();
				ImageLoader.getInstance().clearMemoryCache();
				ToastUtil.showMessage(RangeActivity.this, "清除缓存,更新书籍成功");
				break;
			case R.id.img_range_guide:
				mGuide.setVisibility(View.GONE);
				PreferenceUtil.save(RangeActivity.this, "imgclearguide", "guided");
				break;
			default:
				finish();
				break;
		}
	}
}