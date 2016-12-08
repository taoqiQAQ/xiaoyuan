package com.qz.zqb;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.qz.transaction.MyHelper;
import com.qz.transaction.R;

public class ProductMessage extends Activity{
	private Button back;
	private Button buy;
	private Button connectWay;
	private TextView name;
	private TextView price;
	private TextView introduce;
	private String str_publisher = new String();
	private String str_goods_name = new String();
	private String str_price = new String();
	private String str_describe = new String();
	private String str_bundleCommonname = new String();
	String contact = new String();
	private String type = "visitor";
	Listener butListener = new Listener();
	MyHelper helper;
	SQLiteDatabase db;
	Cursor cursor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.productmessage);
		back = (Button)super.findViewById(R.id.back);
		buy = (Button)super.findViewById(R.id.buy);
		connectWay = (Button)super.findViewById(R.id.connectWay);
		name = (TextView)super.findViewById(R.id.name);
		price = (TextView)super.findViewById(R.id.price);
		introduce = (TextView)super.findViewById(R.id.introduce);
		
		GetTransInfo();
		
		back.setOnClickListener(butListener);
		connectWay.setOnClickListener(butListener);
		buy.setOnClickListener(butListener);
		
		
		 helper = new MyHelper(getApplicationContext());
		
		 name.setText(str_goods_name);
		 price.setText(str_price);
		 introduce.setText(str_describe);
		 
		 GetNameContact();
	}
	
	//获取用户名和联系方式
	private void GetNameContact(){
		 db = helper.getWritableDatabase();
			cursor = db.query("publisher", null, null, null, null, null, null, null);
			if(cursor!=null){
				for(int i = 0;i<cursor.getCount();i++){
					cursor.moveToPosition(i);
					String name =  cursor.getString(cursor.getColumnIndex("publisher_name"));
					if(name.equals(str_publisher)){
						 contact =  cursor.getString(cursor.getColumnIndex("contact"));
						break;
					}
					
				}
				

			}
			cursor.close();
			db.close();
	}
	
	
	//获取传递过来的信息
	private void GetTransInfo(){
		Bundle bundle = getIntent().getExtras();
		str_publisher = bundle.getString("publisher");
		 str_goods_name = bundle.getString("goods_name");
		 str_price = bundle.getString("goods_price");
		 str_describe = bundle.getString("goods_describe");
		 str_bundleCommonname = bundle.getString("common_name");
		 type = bundle.getString("type");
		 if(type.equals("visitor")){
			 buy.setVisibility(View.GONE);
		 }
	}
	
	
	public class Listener implements OnClickListener {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.back:
				ProductMessage.this.finish();
				break;
			case R.id.connectWay:
				showDialog(0);
				break;
			case R.id.buy:
				Buy();
				break;
			}
		}

	}
	
	//购买
	private void Buy(){
		if(type.equals("visitor")){
			Toast.makeText(getApplicationContext(), "访客无法购买", 1).show();
			return;
		}else{
			db = helper.getWritableDatabase();
			cursor = db.query("ordergoods", null, null, null, null, null, null);
			if(cursor!=null){
				ContentValues contentValues = new ContentValues();
				contentValues.put("common_name", str_bundleCommonname);
				contentValues.put("goods_name", str_goods_name);
				db.insert("ordergoods", null, contentValues);
				Toast.makeText(getApplicationContext(), "购买成功", Toast.LENGTH_SHORT).show();
			}
			cursor.close();
			db.close();
			helper.close();
		}
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
		builder.setTitle("商家联系方式")
				.setMessage(contact)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						/* User clicked Yes so do some stuff */
					}
				});
		return builder.create();
	}
}
