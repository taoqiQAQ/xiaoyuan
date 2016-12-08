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
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class GoodsOperate extends Activity {
	private Button back;
	private Button add;
	private Button search;
	private EditText goodsName;
	private ListView mListView;

	private MyHelper myHelper;
	private SQLiteDatabase db;
	Cursor cursor;
	Cursor cursor1;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.admin_goodsoperate);

		back = (Button) findViewById(R.id.admin_goodsoperate_back);
		add = (Button) findViewById(R.id.admin_goodsoperate_add);
		search = (Button) findViewById(R.id.user_goodssearch);
		goodsName = (EditText) findViewById(R.id.user_goodssearch_edit);
		mListView = (ListView) findViewById(R.id.user_goodssearch_listview);

		back.setOnClickListener(new BtnListener());
		add.setOnClickListener(new BtnListener());
		search.setOnClickListener(new BtnListener());
		goodsName.addTextChangedListener(new TextChangeListener());
		mListView.setOnItemClickListener(new ListViewistener());
		
		myHelper = new MyHelper(this);

		ShowGoods();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		ShowGoods();
		super.onResume();
	}

	private class ListViewistener implements OnItemClickListener{

		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			HashMap map = (HashMap)arg0.getItemAtPosition(arg2);
			String publisherName = (String) map.get("publisher_name");
			String goodsName = (String) map.get("goods_name");
			
			Intent intent = new Intent(GoodsOperate.this,GoodsInformationChange.class);
			Bundle bundle = new Bundle();
			bundle.putString("publisherName", publisherName);
			bundle.putString("goodsName", goodsName);
			intent.putExtras(bundle);
			startActivity(intent);
		}
		
	}
	
	private class TextChangeListener implements TextWatcher{

		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			if(TextUtils.isEmpty(s)){
				ShowGoods();
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
	
	@SuppressLint("ParserError")
	private void ShowGoods() {
		db = myHelper.getWritableDatabase();
		cursor = db.query("goods", null, null, null, null, null, null);
		
		AddData2ListView();

		cursor.close();
		db.close();
		myHelper.close();
	}
	
	private void AddData2ListView(){
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);
			Map<String, String> map = new HashMap<String, String>();
			map.put("goods_name",
					cursor.getString(cursor.getColumnIndex("goods_name")));
			map.put("goods_price",
					cursor.getString(cursor.getColumnIndex("goods_price")));
			map.put("goods_describe",
					cursor.getString(cursor.getColumnIndex("goods_describe")));
			list.add(map);
		}
		
		SimpleAdapter adapter = new SimpleAdapter(this, list,
				R.layout.user_listview, new String[] { "goods_name","goods_price" },
				new int[] { R.id.listview_user_type, R.id.listview_user_name });
		mListView.setAdapter(adapter);
	}

	private class BtnListener implements OnClickListener {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.admin_goodsoperate_back:
				GoodsOperate.this.finish();
				break;

			case R.id.admin_goodsoperate_add:
				Intent intent = new Intent(GoodsOperate.this,GoodsInformation.class);
				startActivity(intent);
				break;
			case R.id.user_goodssearch:
				Search();
				break;
			}
		}

	}
	/**
	 * ËÑË÷
	 */
	private void Search(){
		db = myHelper.getWritableDatabase();
		cursor = db.query("goods", new String[] { "goods_name" ,"goods_price","goods_describe"}, "goods_name like ?",
				new String[] { "%" + goodsName.getText().toString()+ "%" }, null, null, null);
		AddData2ListView();
		cursor.close();
		db.close();
		myHelper.close();
	}
}
