package com.qz.transaction;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

@SuppressLint("ParserError")
public class GoodsInformation extends Activity {
	private Button back;
	private Button del;
	private Button sure;
	private EditText name;
	private EditText price;
	private EditText describe;
	private Spinner spinner;
	
	private String publisherName;
	private String goodsName;

	private MyHelper myHelper;
	private SQLiteDatabase db;
	Cursor cursor;

	private String info;
	List<String> list = new ArrayList<String>();
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.goodsinformation);

		back = (Button) findViewById(R.id.goodsoperate_add_change_back);
		del = (Button) findViewById(R.id.goodsoperate_add_change_del);
		spinner = (Spinner)super.findViewById(R.id.spinner);
		sure = (Button) findViewById(R.id.goodsoperate_sure);
		name = (EditText) findViewById(R.id.goodsoperate_name);
		price = (EditText) findViewById(R.id.goodsoperate_price);
		describe = (EditText) findViewById(R.id.goodsoperate_describe);
		del.setVisibility(View.GONE);
		spinner.setVisibility(View.VISIBLE);
		back.setOnClickListener(new BtnListener());
		sure.setOnClickListener(new BtnListener());
		del.setOnClickListener(new BtnListener());
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				info = (String) arg0.getItemAtPosition(arg2);
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		myHelper = new MyHelper(this);
		getObj();
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list); 
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);  
        spinner.setAdapter(adapter);
	}

	private class BtnListener implements OnClickListener {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.goodsoperate_add_change_back:
				GoodsInformation.this.finish();
				break;

			case R.id.goodsoperate_add_change_del:
				break;
			case R.id.goodsoperate_sure:
				AddGoodsData();
				break;
			}
		}

	}


	private void AddGoodsData() {
		if(info==null){
			Toast.makeText(getApplicationContext(), "没有商品发布者，无法发布商品", Toast.LENGTH_SHORT)
			.show();
			return;
		}
		if(!name.getText().toString().equals("")&&!price.getText().toString().equals("")&&!describe.getText().toString().equals("")){
			db = myHelper.getWritableDatabase();
			cursor = db.query("goods", null, null, null, null, null, null);
			ContentValues values = new ContentValues();
			values.put("publisher_name", info);
			values.put("goods_name", name.getText().toString());
			values.put("goods_price", price.getText().toString());
			values.put("goods_describe", describe.getText().toString());
			db.insert("goods", null, values);
			name.setText("");
			price.setText("");
			describe.setText("");
			Toast.makeText(getApplicationContext(), "添加成功", Toast.LENGTH_SHORT)
					.show();
			cursor.close();
			db.close();
			myHelper.close();
		}else {
			Toast.makeText(getApplicationContext(), "商品信息不能为空", Toast.LENGTH_SHORT);
		}
		
	}
	private void getObj(){
		db = myHelper.getWritableDatabase();
		cursor = db.query("publisher", null, null, null, null, null, null);
		if(cursor!=null){
			for(int i = 0;i<cursor.getCount();i++){
				cursor.moveToPosition(i);
				String info = cursor.getString(cursor.getColumnIndex("publisher_name"));
				list.add(i, info);
			}
		}
		cursor.close();
		db.close();
		myHelper.close();
	}
}
