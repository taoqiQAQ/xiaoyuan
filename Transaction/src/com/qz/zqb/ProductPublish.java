package com.qz.zqb;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.qz.transaction.AdminMain;
import com.qz.transaction.MyHelper;
import com.qz.transaction.R;

public class ProductPublish extends Activity {
	private Button publish_setting;
	private Button publish_product;
	private Button publish_quit;

	Listener butListener = new Listener();
	private String str_bundlePass = new String();
	private String str_bundleAccount = new String();
	private String str_bundleName = new String();
	MyHelper helper;
	SQLiteDatabase db;
	Cursor cursor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.productpublish);
		publish_product = (Button) super.findViewById(R.id.publish_product);
		publish_setting = (Button) super.findViewById(R.id.publish_setting);
		publish_quit = (Button) super.findViewById(R.id.publish_quit);

		publish_product.setOnClickListener(butListener);
		publish_setting.setOnClickListener(butListener);
		publish_quit.setOnClickListener(butListener);
		
		 helper = new MyHelper(this);
		 GetTransName();
	}
	
	//获取传递过来的用户名
	private void GetTransName(){
		Bundle bundle = getIntent().getExtras();
		str_bundleAccount = bundle.getString("account");
		 str_bundlePass = bundle.getString("pass");
		 db = helper.getWritableDatabase();
			cursor = db.query("publisher", null, null, null, null, null, null);
			if(cursor!=null){
				for(int i  =0;i<cursor.getCount();i++){
					cursor.moveToPosition(i);
					String account =  cursor.getString(cursor.getColumnIndex("publisher_account"));
					if(account.equals(str_bundleAccount)){
						str_bundleName = cursor.getString(cursor.getColumnIndex("publisher_name"));
						break;
					}
					
				}
			}
			cursor.close();
			db.close();
			helper.close();
	}

	public class Listener implements OnClickListener {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.publish_product:
				Bundle bundle = new Bundle();
				bundle.putString("account", str_bundleAccount);
				bundle.putString("name", str_bundleName);
				Intent intent = new Intent();
				intent.setClass(ProductPublish.this,Products.class);
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case R.id.publish_setting:
				Bundle bundle2 = new Bundle();
				bundle2.putString("account", str_bundleAccount);
				bundle2.putString("pass", str_bundlePass);
				Intent intent2 = new Intent();
				intent2.setClass(ProductPublish.this,PersonalSetting.class);
				intent2.putExtras(bundle2);
				startActivity(intent2);
				break;
			case R.id.publish_quit:
				showDialog(0);
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
							ProductPublish.this.finish();
						}
					})
					.setNegativeButton("取消", new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

						}
					});
			return builder.create();
		}
}
