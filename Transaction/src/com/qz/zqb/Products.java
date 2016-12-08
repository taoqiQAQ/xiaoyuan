package com.qz.zqb;

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
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.qz.transaction.MyHelper;
import com.qz.transaction.R;
import com.qz.transaction.UserInformation;
import com.qz.transaction.UserOperate;

public class Products extends Activity {
	private Button publish;
	private Button back;
	private EditText search;
	private Button searchBut;
	private Button myProducts;
	private Button allProducts;
	private ListView listview;
	Listener butListener = new Listener();
	List<Map<String, String>> list = new ArrayList<Map<String, String>>();
	private String str_bundleAccount = new String();
	private String str_bundleName = new String();
	private String mainUserName;
	MyHelper helper;
	SQLiteDatabase db;
	Cursor cursor;
	SimpleAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.products);
		publish = (Button) super.findViewById(R.id.publish);
		search = (EditText) super.findViewById(R.id.search);
		searchBut = (Button) super.findViewById(R.id.searchBut);
		myProducts = (Button) super.findViewById(R.id.myProducts);
		allProducts = (Button) super.findViewById(R.id.allProducts);
		listview = (ListView) super.findViewById(R.id.listview);
		back = (Button) findViewById(R.id.publish_back);
		
		Bundle bundle = getIntent().getExtras();
		str_bundleAccount = bundle.getString("account");
		str_bundleName = bundle.getString("name");
		helper = new MyHelper(this);
		
		initmyPro();
		
		GetLandedName();
		

		searchBut.setOnClickListener(butListener);
		publish.setOnClickListener(butListener);
		myProducts.setOnClickListener(butListener);
		allProducts.setOnClickListener(butListener);
		back.setOnClickListener(butListener);
		search.addTextChangedListener(new TextChangeListener());
		listview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				HashMap map = (HashMap)arg0.getItemAtPosition(arg2);
				String publisher = (String) map.get("publisher");
				String goods_name = (String) map.get("goods_name");
				
				
				Intent intent = new Intent(Products.this,ProductInfo.class);
				Bundle bundle = new Bundle();
				bundle.putString("publisher", publisher);
				bundle.putString("goods_name", goods_name);
				bundle.putString("mainUserName", mainUserName);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});


	}

	//获取登录的用户的名字
	private void GetLandedName(){
		db = helper.getWritableDatabase();
		cursor = db.query("publisher", null, null, null, null, null, null);
		for(int i=0;i<cursor.getCount();i++){
			cursor.moveToPosition(i);
			if(cursor.getString(cursor.getColumnIndex("publisher_account")).equals(str_bundleAccount)){
				mainUserName = cursor.getString(cursor.getColumnIndex("publisher_name"));
			}
		}
		cursor.close();
		db.close();
		helper.close();
	}
	
	private class TextChangeListener implements TextWatcher{

		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			if(TextUtils.isEmpty(s)){
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
	
	
	private void AddData2ListView() {
		adapter = new SimpleAdapter(getApplicationContext(), list,
				R.layout.list_item, new String[] { "publisher", "goods_name","mainUserName" },
				new int[] { R.id.publisher, R.id.goods_name });
		listview.setAdapter(adapter);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		initmyPro();
		initallPro();
		super.onResume();
	}

	public class Listener implements OnClickListener {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.searchBut:
				Search();
				break;
			case R.id.publish:
				Bundle bundle = new Bundle();
				bundle.putString("account", str_bundleAccount);
				bundle.putString("name", str_bundleName);
				Intent intent = new Intent();
				intent.setClass(Products.this, Publish.class);
				intent.putExtras(bundle);
				startActivity(intent);

				break;
			case R.id.myProducts:
				initmyPro();
				break;
			case R.id.allProducts:
				initallPro();
				break;
			case R.id.publish_back:
				Products.this.finish();
				break;
			}
		}

	}
	
	//搜索
	private void Search(){
		list.clear();
		String name = search.getText().toString();
		db = helper.getWritableDatabase();
		cursor = db.query("goods", new String[] { "publisher_name",
				"goods_name" }, "goods_name like ?", new String[] { "%"
				+ name + "%" }, null, null, null);

		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);
			String goods_name = cursor.getString(cursor
					.getColumnIndex("goods_name"));
			String publisher = cursor.getString(cursor
					.getColumnIndex("publisher_name"));
			Map<String, String> map = new HashMap<String, String>();
			map.put("publisher", publisher);
			map.put("goods_name", goods_name);
			list.add(map);

		}
		AddData2ListView();
		cursor.close();
		db.close();
		helper.close();
	}
	
	//初始化自己发布的商品
	public void initmyPro() {
		list.clear();
		db = helper.getWritableDatabase();
		cursor = db.query("goods", null, null, null, null, null, null);
		if (cursor != null) {
			for (int i = 0; i < cursor.getCount(); i++) {
				cursor.moveToPosition(i);
				String publisher = cursor.getString(cursor
						.getColumnIndex("publisher_name"));
				if (publisher.equals(str_bundleName)) {
					String goods_name = cursor.getString(cursor
							.getColumnIndex("goods_name"));
					Map<String, String> map = new HashMap<String, String>();
					map.put("publisher", str_bundleName);
					map.put("goods_name", goods_name);
					map.put("mainUserName", mainUserName);
					list.add(map);
				}
			}
			AddData2ListView();
		}
		cursor.close();
		db.close();
		helper.close();
	}

	
	//初始化全部商品
	@SuppressLint("ParserError")
	public void initallPro() {
		list.clear();
		db = helper.getWritableDatabase();
		cursor = db.query("goods", null, null, null, null, null, null);
		if (cursor != null) {
			for (int i = 0; i < cursor.getCount(); i++) {
				cursor.moveToPosition(i);
				String goods_name = cursor.getString(cursor
						.getColumnIndex("goods_name"));
				String publisher = cursor.getString(cursor
						.getColumnIndex("publisher_name"));
				Map<String, String> map = new HashMap<String, String>();
				map.put("publisher", publisher);
				map.put("goods_name", goods_name);
				map.put("mainUserName", mainUserName);
				list.add(map);
			}
			AddData2ListView();

		}
		cursor.close();
		db.close();
		helper.close();
	}

}
