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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.qz.transaction.MyHelper;
import com.qz.transaction.R;

public class BuyLists extends Activity{
	private Button back;
	private ListView listview;
	private String commonUser;
	Listener butListener = new Listener();
	MyHelper helper;
	SQLiteDatabase db;
	Cursor cursor;
	List<Map<String,String>> list = new ArrayList<Map<String,String>>();
	 SimpleAdapter adapter;
	 private String username;
	 private String productname;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.buylists);
		
		back = (Button)super.findViewById(R.id.back);
		listview = (ListView)super.findViewById(R.id.listview);
		
		back.setOnClickListener(butListener);
		listview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Map<String,String> map = (Map<String, String>) arg0.getItemAtPosition(arg2);
				username = map.get("common_name");
				productname = map.get("goods_name");
				showDialog(0);
			}
		});
		
		 helper = new MyHelper(getApplicationContext());
		
		 
		Bundle bundle = getIntent().getExtras();
		commonUser = bundle.getString("common_userName");
		
		initallPro();
	}
	
	private void AddData2List(){
		adapter = new SimpleAdapter(this, list,R.layout.list_item,
				 new String[]{"common_name","goods_name"}, new int[]{R.id.publisher,R.id.goods_name});
		 listview.setAdapter(adapter);
	}
	
	public class Listener implements OnClickListener {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.back:
				BuyLists.this.finish();
				break;
			}
		}

	}
	@SuppressLint("ParserError")
	public void initallPro(){
		list.clear();
		db = helper.getWritableDatabase();
		cursor = db.query("ordergoods", null, null, null, null, null, null);
		if(cursor!=null){
			for(int i = 0;i<cursor.getCount();i++){
				cursor.moveToPosition(i);
					if(cursor.getString(cursor.getColumnIndex("common_name")).equals(commonUser)){
						Map<String,String> map = new HashMap<String, String>();
						map.put("common_name", cursor.getString(cursor.getColumnIndex("common_name")));
						map.put("goods_name", cursor.getString(cursor.getColumnIndex("goods_name")));
						list.add(map);
					}
					
				}
			AddData2List();
			}
		cursor.close();
		db.close();
		helper.close();
		}
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch (id) {
		case 0: return show();
		}
		return super.onCreateDialog(id);
	}
	private Dialog show() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("是否删除？")
				
				.setPositiveButton("是", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						/* User clicked Yes so do some stuff */
						db = helper.getWritableDatabase();
//						cursor = db.query("ordergoods", null, null, null, null,
//								null, null);
					
						db.delete("ordergoods", "goods_name = ?",
								new String[] { productname});
						Toast.makeText(getApplicationContext(), "删除成功",
								Toast.LENGTH_SHORT).show();
						cursor.close();
						db.close();
						helper.close();
						initallPro();
					}
				}).setNegativeButton("否", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				});
		return builder.create();
	}
}


