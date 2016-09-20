package com.woyuce.activity.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.woyuce.activity.R;

public class Fragmentfive extends Fragment {

//	private TextView txtName, txtMoney, txtAboutUs, txtUpdate, txtSuggestion, txtRoom, txtSubject;
//	private ImageView imgIcon;
//	// �����α�����
//	private TextView mCourseTable;
//
//	private String localroomname;
//	private String URL_ROOM = "http://iphone.ipredicting.com/kymyroom.aspx";
//	private String URL_SUBJECT = "http://iphone.ipredicting.com/kymyshanesub.aspx";
//	private List<Room> roomList = new ArrayList<Room>();
//	private List<SubContent> subcontentList = new ArrayList<SubContent>();
//	private List<String> myexamList = new ArrayList<>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.tab5, container, false);

//		initView(view);
		return view;
	}

//	@Override
//	public void onResume() {
//		super.onResume();
//		txtRoom.setText("");
//		txtSubject.setText("");
//		initEvent();
//	}
//
//	private void initView(View view) {
//		imgIcon = (ImageView) view.findViewById(R.id.img_tab5_icon);
//		txtName = (TextView) view.findViewById(R.id.txt_tab5_username);
//		txtMoney = (TextView) view.findViewById(R.id.txt_tab5_localmoney);
//		txtAboutUs = (TextView) view.findViewById(R.id.txt_to_aboutus);
//		txtUpdate = (TextView) view.findViewById(R.id.txt_to_update);
//		txtSuggestion = (TextView) view.findViewById(R.id.txt_to_suggestion);
//		txtRoom = (TextView) view.findViewById(R.id.txt_tab5_localroom);
//		txtSubject = (TextView) view.findViewById(R.id.txt_tab5_localsubject);
//
//		mCourseTable = (TextView) view.findViewById(R.id.txt_tab5_localmessage);
//		mCourseTable.setOnClickListener(this);
//
//		imgIcon.setOnClickListener(this);
//		txtAboutUs.setOnClickListener(this);
//		txtUpdate.setOnClickListener(this);
//		txtSuggestion.setOnClickListener(this);
//		txtRoom.setOnClickListener(this);
//		txtSubject.setOnClickListener(this);
//	}
//
//	// fragment �������ڣ���ʱ
//	private void initEvent() {
//		if (share().getString("username", "").length() == 0) {
//			txtRoom.setText("��½��ɼ�");
//			txtSubject.setText("��½��ɼ�");
//			myexamList.clear();
//		} else {
//			roomList.clear();
//			subcontentList.clear();
//			myexamList.clear();
//			getRoomJson();
//			getSubjectJson();
//		}
//		txtName.setText(share().getString("mUserName", "���ͷ���л��˺�"));
//		txtMoney.setText(share().getString("money", "��¼��ɼ�"));
//	}
//
//	// ��initEvent �����������ã�������
//	private SharedPreferences share() {
//		return PreferenceUtil.getSharePre(getActivity());
//	}
//
//	//����
//	private void getRoomJson() {
//		StringRequest stringRequest = new StringRequest(Method.POST, URL_ROOM, new Response.Listener<String>() {
//			@Override
//			public void onResponse(String response) {
//				JSONObject jsonObject;
//				Room room;
//				try {
//					jsonObject = new JSONObject(response);
//					int result = jsonObject.getInt("code");
//					if (result == 0) {
//						JSONArray data = jsonObject.getJSONArray("data");
//						for (int i = 0; i < data.length(); i++) {
//							jsonObject = data.getJSONObject(i);
//							room = new Room();
//							room.roomid = jsonObject.getString("id");
//							room.roomname = jsonObject.getString("examroom");
//							roomList.add(room);
//							localroomname = room.roomname;
//							txtRoom.setText(localroomname);
//						}
//					}
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//			}
//		}, null) {
//			@Override
//			protected Map<String, String> getParams() throws AuthFailureError {
//				Map<String, String> hashMap = new HashMap<String, String>();
//				hashMap.put("uname", PreferenceUtil.getSharePre(getActivity()).getString("username", ""));
//				return hashMap;
//			}
//		};
//		stringRequest.setTag("fragmentfive");
//		MyApplication.getHttpQueue().add(stringRequest);
//	}
//
//	/**
//	 * ��ȡ�����б�
//	 */
//	private void getSubjectJson() {
//		StringRequest stringRequest = new StringRequest(Method.POST, URL_SUBJECT, new Response.Listener<String>() {
//			@Override
//			public void onResponse(String response) {
//				JSONObject jsonObject;
//				SubContent subcontent;
//				try {
//					jsonObject = new JSONObject(response);
//					int result = jsonObject.getInt("code");
//					if (result == 0) {
//						JSONArray data = jsonObject.getJSONArray("data");
//						for (int i = 0; i < data.length(); i++) {
//							jsonObject = data.getJSONObject(i);
//							subcontent = new SubContent();
//							subcontent.subname = jsonObject.getString("subname");
//							subcontentList.add(subcontent);
//							String localsubname = subcontent.subname;
//							myexamList.add(localsubname);
//						}
//						List<String> sublist = new ArrayList<>();
//						for (int i = 0; i < subcontentList.size(); i++) {
//							sublist.add("��" + subcontentList.get(i).subname + "��");
//						}
//						txtSubject.setText(sublist.toString());
//					}
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//			}
//		}, null) {
//			@Override
//			protected Map<String, String> getParams() throws AuthFailureError {
//				Map<String, String> hashMap = new HashMap<String, String>();
//				hashMap.put("uname", PreferenceUtil.getSharePre(getActivity()).getString("username", ""));
//				return hashMap;
//			}
//		};
//		stringRequest.setTag("fragmentfive");
//		MyApplication.getHttpQueue().add(stringRequest);
//	}
//
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.img_tab5_icon:
//			startActivity(new Intent(getActivity(), LoginActivity.class));
//			break;
//		case R.id.txt_tab5_localsubject:
//			Intent intent = new Intent(getActivity(),MyExamContent.class);
//			intent.putStringArrayListExtra("myexamList", (ArrayList<String>) myexamList);
//			startActivity(intent);
//			break;
//		case R.id.txt_to_aboutus:
//			startActivity(new Intent(getActivity(), AboutUsActivity.class));
//			break;
//		case R.id.txt_to_update:
//			String serverVersion = PreferenceUtil.getSharePre(getActivity()).getString("serverVersion", "");
//			String localVersion = PreferenceUtil.getSharePre(getActivity()).getString("localVersion", "");
//			LogUtil.e("serverVersion", "serverVersion = " + serverVersion + " localVersion" + localVersion);
//			if (Float.parseFloat(serverVersion) > Float.parseFloat(localVersion)) {
//				// �Զ�����
//				new UpdateManager(getActivity()).checkUpdate();
//				break;
//			} else {
//				ToastUtil.showMessage(getActivity(), "��ǰ�Ѿ������°汾:v" + localVersion);
//				break;
//			}
//		case R.id.txt_to_suggestion:
//			startActivity(new Intent(getActivity(), SuggestionActivity.class));
//			break;
//		case R.id.txt_tab5_localmessage:
//			// startActivity(new Intent(getActivity(), Schedule.class));
//			break;
//		}
//	}
}