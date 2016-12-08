package com.qz.zqb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.qz.transaction.MyHelper;
import com.qz.transaction.R;

public class NormalUsers extends Activity {
	private Button quit;
	private Button buyList;
	private EditText search;
	private Button searchBut;
	private ListView listview;
	private TextView user;
	private String str_bundlePass = new String();
	private String str_bundleAccount = new String();
	private String str_bundleCommonname = new String();
	private String type = "visitor";
	
	Listener butListener = new Listener();
	List<Map<String, String>> list = new ArrayList<Map<String, String>>();
	MyHelper helper;
	SQLiteDatabase db;
	Cursor cursor;
	SimpleAdapter adapter;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.normalusers);
		
		search = (EditText) super.findViewById(R.id.search);
		searchBut = (Button) super.findViewById(R.id.searchBut);
		listview = (ListView) super.findViewById(R.id.listview);
		quit = (Button) super.findViewById(R.id.quit);
		buyList = (Button) super.findViewById(R.id.buyList);
		user = (TextView) findViewById(R.id.user);

		//获取传递过来的信息
		Bundle bundle = getIntent().getExtras();
		str_bundleAccount = bundle.getString("account");
		str_bundlePass = bundle.getString("pass");
		type = bundle.getString("type");
		if (type.equalsIgnoreCase("visitor")) {
			buyList.setVisibility(View.GONE);
			user.setText("访客");
		}

		searchBut.setOnClickListener(butListener);
		quit.setOnClickListener(butListener);
		buyList.setOnClickListener(butListener);
		search.addTextChangedListener(new TextChangeListener());
		listview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				@SuppressWarnings("unchecked")
				Map<String, String> map = (Map<String, String>) arg0
						.getItemAtPosition(arg2);
				String publisher = map.get("publisher");
				String goods_name = map.get("goods_name");
				String goods_price = map.get("goods_price");
				String goods_describe = map.get("goods_describe");

				Bundle bundle = new Bundle();
				bundle.putString("publisher", publisher);
				bundle.putString("goods_name", goods_name);
				bundle.putString("goods_price", goods_price);
				bundle.putString("goods_describe", goods_describe);
				bundle.putString("common_name", str_bundleCommonname);
				if (type.equals("visitor")) {
					bundle.putString("type", type);
				} else {
					bundle.putString("type", "common");
				}

				Intent intent = new Intent();
				intent.setClass(NormalUsers.this, ProductMessage.class);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		helper = new MyHelper(getApplicationContext());
		GetAccountAndName();
		initallPro();

	}

	//获取用户账户和用户名
	private void GetAccountAndName() {
		helper = new MyHelper(getApplicationContext());
		db = helper.getWritableDatabase();
		cursor = db.query("common", null, null, null, null, null, null);
		if (cursor != null) {
			for (int i = 0; i < cursor.getCount(); i++) {
				cursor.moveToPosition(i);
				String account = cursor.getString(cursor
						.getColumnIndex("common_account"));
				if (account.equals(str_bundleAccount)) {
					str_bundleCommonname = cursor.getString(cursor
							.getColumnIndex("common_name"));
					break;
				}

			}
		}
		cursor.close();
		db.close();
		helper.close();
	}

	
	private class TextChangeListener implements TextWatcher {

		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			if (TextUtils.isEmpty(s)) {
				initallPro();
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

	private void AddData2List() {
		adapter = new SimpleAdapter(getApplicationContext(), list,
				R.layout.list_item, new String[] { "publisher", "goods_name" },
				new int[] { R.id.publisher, R.id.goods_name });
		listview.setAdapter(adapter);
	}

	@SuppressLint("ParserError")
	public class Listener implements OnClickListener {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.searchBut:
				list.clear();
				db = helper.getWritableDatabase();
				cursor = db.query("goods", new String[] { "goods_name",
						"publisher_name", "goods_price", "goods_describe" },
						"goods_name like ?", new String[] { "%"
								+ search.getText().toString() + "%" }, null,
						null, null);
				for (int i = 0; i < cursor.getCount(); i++) {
					cursor.moveToPosition(i);
					String goods_name = cursor.getString(cursor
							.getColumnIndex("goods_name"));
					String publisher = cursor.getString(cursor
							.getColumnIndex("publisher_name"));
					Map<String, String> map = new HashMap<String, String>();
					map.put("publisher", publisher);
					map.put("goods_name", goods_name);
					map.put("goods_price", cursor.getString(cursor
							.getColumnIndex("goods_price")));
					map.put("goods_describe", cursor.getString(cursor
							.getColumnIndex("goods_describe")));
					list.add(map);
				}
				AddData2List();
				cursor.close();
				db.close();
				helper.close();
				break;
			case R.id.quit:
				showDialog(0);

				break;
			case R.id.buyList:
				if (type.equals("common")) {
					Bundle bundle = new Bundle();
					bundle.putString("common_userName", str_bundleCommonname);
					Intent intent = new Intent();
					intent.setClass(NormalUsers.this, BuyLists.class);
					intent.putExtras(bundle);
					startActivity(intent);

				} else {
					Toast.makeText(getApplicationContext(), "访客无法购买", 1).show();
				}
				break;
			}
		}

	}

	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch (id) {
		case 0:
			return show();
		}
		return super.onCreateDialog(id);
	}

	private Dialog show() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("确定注销？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						/* User clicked Yes so do some stuff */
						NormalUsers.this.finish();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				});
		return builder.create();
	}

	public void initallPro() {
		db = helper.getWritableDatabase();
		cursor = db.query("goods", null, null, null, null, null, null);
		if (cursor != null) {
			for (int i = 0; i < cursor.getCount(); i++) {
				cursor.moveToPosition(i);
				String goods_name = cursor.getString(cursor
						.getColumnIndex("goods_name"));
				String publisher = cursor.getString(cursor
						.getColumnIndex("publisher_name"));
				String goods_price = cursor.getString(cursor
						.getColumnIndex("goods_price"));
				String goods_describe = cursor.getString(cursor
						.getColumnIndex("goods_describe"));
				Map<String, String> map = new HashMap<String, String>();
				map.put("publisher", publisher);
				map.put("goods_name", goods_name);
				map.put("goods_price", goods_price);
				map.put("goods_describe", goods_describe);
				list.add(map);
			}
			AddData2List();
		}
		cursor.close();
		db.close();
		helper.close();
	}
}
