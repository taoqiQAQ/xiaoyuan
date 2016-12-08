package com.qz.transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class UserOperate extends Activity {
	private Button back;
	private Button add;
	private Button search;
	private EditText userName;
	private ListView mListView;

	private MyHelper myHelper;
	private SQLiteDatabase db;
	Cursor cursor;
	Cursor cursor1;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.admin_useroperate);

		back = (Button) findViewById(R.id.admin_useroperate_back);
		add = (Button) findViewById(R.id.admin_useroperate_add);
		search = (Button) findViewById(R.id.user_search);
		userName = (EditText) findViewById(R.id.user_search_edit);
		mListView = (ListView) findViewById(R.id.user_search_listview);

		back.setOnClickListener(new BtnListener());
		add.setOnClickListener(new BtnListener());
		search.setOnClickListener(new BtnListener());
		userName.addTextChangedListener(new TextChangeListener());
		mListView.setOnItemClickListener(new ListViewistener());
		
		myHelper = new MyHelper(this);

		ShowUser();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		ShowUser();
		super.onResume();
	}

	/**
	 * 
	 * @author 监听列表点击事件
	 *
	 */
	private class ListViewistener implements OnItemClickListener{

		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			HashMap map = (HashMap)arg0.getItemAtPosition(arg2);
			String type = (String) map.get("type");
			String name = (String) map.get("name");
			
			Intent intent = new Intent(UserOperate.this,UserInformation.class);
			Bundle bundle = new Bundle();
			bundle.putString("type", type);
			bundle.putString("name", name);
			intent.putExtras(bundle);
			startActivity(intent);
		}
		
	}
	
	/**
	 * 
	 * @author 监听编辑框文本改变事件
	 *
	 */
	private class TextChangeListener implements TextWatcher{

		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			if(TextUtils.isEmpty(s)){
				ShowUser();
			}
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
		
		}
		
	}
	
	/**
	 * 显示所有已注册用户
	 */
	@SuppressLint("ParserError")
	private void ShowUser() {
		db = myHelper.getWritableDatabase();
		cursor = db.query("publisher", null, null, null, null, null, null);
		cursor1 = db.query("common", null, null, null, null, null, null);
		
		AddData2ListView();

		cursor.close();
		cursor1.close();
		db.close();
		myHelper.close();
	}
	
	/**
	 * 向listview列表添加数据
	 * 
	 */
	private void AddData2ListView(){
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);
			Map<String, String> map = new HashMap<String, String>();
			map.put("type", cursor.getString(cursor.getColumnIndex("type")));
			map.put("name",
					cursor.getString(cursor.getColumnIndex("publisher_name")));
			list.add(map);
		}
		for (int i = 0; i < cursor1.getCount(); i++) {
			cursor1.moveToPosition(i);
			Map<String, String> map = new HashMap<String, String>();
			map.put("type", cursor1.getString(cursor1.getColumnIndex("type")));
			map.put("name",
					cursor1.getString(cursor1.getColumnIndex("common_name")));
			list.add(map);
		}

		SimpleAdapter adapter = new SimpleAdapter(this, list,
				R.layout.user_listview, new String[] { "type", "name" },
				new int[] { R.id.listview_user_type, R.id.listview_user_name });
		mListView.setAdapter(adapter);
	}

	private class BtnListener implements OnClickListener {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.admin_useroperate_back:
				UserOperate.this.finish();
				break;

			case R.id.admin_useroperate_add:
				Intent intent = new Intent(UserOperate.this,AdminAddUser.class);
				startActivity(intent);
				break;
			case R.id.user_search:
				Search();
				break;
			}
		}

	}
	
	/**
	 * 搜索
	 */
	private void Search(){
		db = myHelper.getWritableDatabase();
		cursor = db.query("publisher", new String[] { "type",
				"publisher_name" }, "publisher_name like ?",
				new String[] { "%" + userName.getText().toString()
						+ "%" }, null, null, null);
		cursor1 = db.query("common", new String[] { "type",
				"common_name" },  "common_name like ?", new String[] { "%" + userName.getText().toString()
						+ "%" }, null, null, null);
		AddData2ListView();
		cursor1.close();
		cursor.close();
		db.close();
		myHelper.close();
	}
}
