//package com.woyuce.activity.Utils;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import com.android.volley.Request.Method;
//import com.android.volley.Response.Listener;
//import com.android.volley.toolbox.StringRequest;
//import com.woyuce.activity.R;
//import com.woyuce.application.MyApplication;
//import android.annotation.SuppressLint;
//import android.app.AlertDialog;
//import android.app.AlertDialog.Builder;
//import android.app.Dialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.DialogInterface.OnClickListener;
//import android.content.Intent;
//import android.content.pm.PackageManager.NameNotFoundException;
//import android.net.Uri;
//import android.os.Environment;
//import android.os.Handler;
//import android.os.Message;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.ProgressBar;
//
//public class UpdateManager {
//
//	private ProgressBar pb;
//	private Dialog mDownLoadDialog;
//
//	private final String URL_SERVE = "http://www.iyuce.com/Scripts/andoird.json";
//	private static final int DOWNLOADING = 1;
//	private static final int DOWNLOAD_FINISH = 0;
//
//	private String mVersion;
//	private String mVersionURL;
//	private String mMessage;
//	private String mSavePath;
//	private int mProgress;
//	private boolean mIsCancel = false;
//
//	private Context mcontext;
//
//	public UpdateManager(Context context) {
//		mcontext = context;
//	}
//
//	@SuppressLint("HandlerLeak")
//	private Handler mGetVersionHandler = new Handler() {
//		public void handleMessage(Message msg) {
//			try {
//				String jsonString = (String) msg.obj;
//				String parseString = new String(jsonString.getBytes("ISO-8859-1"), "utf-8");
//				JSONObject jsonObject;
//				jsonObject = new JSONObject(parseString);
//				int result = jsonObject.getInt("code");
//				if (result == 0) {
//					JSONArray data = jsonObject.getJSONArray("data");
//					jsonObject = data.getJSONObject(0);
//					mVersion = jsonObject.getString("version");
//					mVersionURL = jsonObject.getString("apkurl");              //������Ϣ��û�У���ʱ���̨���������
//					mMessage = jsonObject.getString("detail");
////					Log.e("mVersionURL", "VersionURL = " + mVersionURL);
//				}
////				Log.e("version", "Զ��version = " + mVersion);
//				if (isUpdate()) {
//					showNoticeDialog();
//				} else {
////					Toast.makeText(mcontext, "don't need update", Toast.LENGTH_SHORT).show();
//				}
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	};
//
//	@SuppressLint("HandlerLeak")
//	private Handler mUpdateProgressHandler = new Handler() {
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//			case DOWNLOADING:
//				pb.setProgress(mProgress);
//				break;
//			case DOWNLOAD_FINISH:
//				mDownLoadDialog.dismiss();
//				installAPK();
//				break;
//			}
//		};
//	};
//
//	public void checkUpdate() {
//		StringRequest request = new StringRequest(Method.GET, URL_SERVE, new Listener<String>() {
//			@Override
//			public void onResponse(String response) {
//				Message msg = Message.obtain();
//				msg.obj = response;
//				mGetVersionHandler.sendMessage(msg);			}
//		}, null);
//		request.setTag("updatemanager");
//		MyApplication.getHttpQueue().add(request);
//	}
//
//	public boolean isUpdate() {   /*boolean�Ƚϱ��ذ汾�Ƿ���Ҫ����*/
//		float serverVersion = Float.parseFloat(mVersion);
//		/*�������ݱ�����sharepreference������*/
//		PreferenceUtil.save(mcontext, "serverVersion", String.valueOf(serverVersion));
//		String localVersion = null;
//
//		try {
//			localVersion = mcontext.getPackageManager().getPackageInfo("com.woyuce.activity", 0).versionName;   //��ȡversionName���Ƚ�
//			/*�������ݱ�����sharepreference������*/
//			PreferenceUtil.save(mcontext, "localVersion", mcontext.getPackageManager().getPackageInfo("com.woyuce.activity", 0).versionName);
////			Log.e("localVersion", "localVersion = " + localVersion);
//		} catch (NameNotFoundException e) {
//			e.printStackTrace();
//		}
//		if (serverVersion > Float.parseFloat(localVersion)) {
//			return true;
//		} else {
//			return false;
//		}
//	}
//
//	@SuppressLint("InlinedApi")
//	@SuppressWarnings("deprecation")
//	protected void showNoticeDialog() {     //show ������ѡ���Ƿ����
//		Builder builder = new Builder(mcontext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
//		builder.setTitle("�����°汾");
//		builder.setMessage(mMessage);
//		builder.setPositiveButton("����", new OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//				showDownloadDialog();
//			}
//		});
//		builder.setNegativeButton("�´���˵", new OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//			}
//		});
//		builder.create().show();
//	}
//
//	@SuppressWarnings("deprecation")
//	@SuppressLint({ "InflateParams", "InlinedApi" })
//	protected void showDownloadDialog() {     //��ʾ���ؽ���
//		Builder builder = new Builder(mcontext,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
//		builder.setTitle("������");
//
//		View view = LayoutInflater.from(mcontext).inflate(R.layout.dialog_progress, null);
//		pb = (ProgressBar) view.findViewById(R.id.update_progress);
//		builder.setView(view);
//		builder.setNegativeButton("ȡ������", new OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//				mIsCancel = true;
//			}
//		});
//		mDownLoadDialog = builder.create();
//		mDownLoadDialog.show();
//
//		/*�����ļ�*/
//		downloadAPK();
//	}
//
//	private void downloadAPK() {         //�ļ����صĲ���   1.�洢��    2.������
//		new Thread(new Runnable() {
//			public void run() {
//				try {
//					if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//						String sdPath = Environment.getExternalStorageDirectory() + "/";// sd����Ŀ¼
//						mSavePath = sdPath + "iyuce";
//
//						File dir = new File(mSavePath);
//						if (!dir.exists()) {
//							dir.mkdir();
//						}
//
//						HttpURLConnection conn = (HttpURLConnection) new URL(mVersionURL).openConnection();
//						conn.connect();
//						InputStream is = conn.getInputStream();
//						int length = conn.getContentLength();
//
//						File apkFile = new File(mSavePath, mVersion);
//						FileOutputStream fos = new FileOutputStream(apkFile);
//
//						int count = 0;
//						byte[] buffer = new byte[1024];
//
//						while (!mIsCancel) {
//							int numread = is.read(buffer);
//							count += numread;
//							mProgress = (int) (((float) count / length) * 100);
//							// ���½�����
//							mUpdateProgressHandler.sendEmptyMessage(DOWNLOADING);
//							// �������
//							if (numread < 0) {
//								mUpdateProgressHandler.sendEmptyMessage(DOWNLOAD_FINISH);
//								break;
//							}
//							fos.write(buffer, 0, numread);
//						}
//						fos.close();
//						is.close();
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}).start();
//	}
//
//	//��װ���غõ�APK
//	private void installAPK() {
//		//�Ƴ�����ֵ��ʹ��һ������������������
//		PreferenceUtil.removefirstguide(mcontext);
//		File apkFile = new File(mSavePath, mVersion);
//		if (!apkFile.exists()) {
//			return;
//		}
//		Intent it = new Intent(Intent.ACTION_VIEW);
//		it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		Uri uri = Uri.parse("file://" + apkFile.toString());
//		it.setDataAndType(uri, "application/vnd.android.package-archive");
//		mcontext.startActivity(it);
//		android.os.Process.killProcess(android.os.Process.myPid());
//	}
//}