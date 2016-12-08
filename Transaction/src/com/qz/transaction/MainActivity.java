package com.qz.transaction;

import com.qz.zqb.NormalUsers;
import com.qz.zqb.ProductMessage;
import com.qz.zqb.ProductPublish;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.text.InputType;

public class MainActivity extends Activity {
	private Button landed;
	private Button login;
	private Button quit;
	private EditText account;
	private EditText password;
	private RadioGroup radioGroup;

	final static int TYPE_ADMIN = 100; // 管理者
	final static int TYPE_PUBLISHER = 101; // 商品发布者
	final static int TYPE_COMMON = 102; // 普通用户
	final static int TYPE_VISITOR = 103; // 访客
	private int flag = TYPE_ADMIN; // 监听是哪种类型的用户登陆

	private MyHelper myHepler;
	private SQLiteDatabase db;
	Cursor cursor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		myHepler = new MyHelper(this);

		DefauitData();
		landed = (Button) findViewById(R.id.landed);
		login = (Button) findViewById(R.id.login);
		quit = (Button) findViewById(R.id.quit);
		account = (EditText) findViewById(R.id.landed_account);
		password = (EditText) findViewById(R.id.landed_password);
		radioGroup = (RadioGroup) findViewById(R.id.landed_user_type);

		landed.setOnClickListener(new BtnListener());
		login.setOnClickListener(new BtnListener());
		quit.setOnClickListener(new BtnListener());
		radioGroup.setOnCheckedChangeListener(new CheckChangeListener());

	}

	/**
	 * 
	 * @author 设置单选按键监听
	 * 
	 */
	private class CheckChangeListener implements OnCheckedChangeListener {

		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub
			switch (checkedId) {
			case R.id.landed_user_type_admin: // 管理员
				flag = TYPE_ADMIN;
				account.setInputType(InputType.TYPE_CLASS_TEXT);
				password.setInputType(InputType.TYPE_CLASS_TEXT
						| InputType.TYPE_TEXT_VARIATION_PASSWORD);
				account.setText("");
				password.setText("");
				break;
			case R.id.landed_user_type_publisher: // 商品发布者
				flag = TYPE_PUBLISHER;
				account.setInputType(InputType.TYPE_CLASS_TEXT);
				password.setInputType(InputType.TYPE_CLASS_TEXT
						| InputType.TYPE_TEXT_VARIATION_PASSWORD);
				account.setText("");
				password.setText("");
				break;
			case R.id.landed_user_type_common: // 普通用户
				flag = TYPE_COMMON;
				account.setInputType(InputType.TYPE_CLASS_TEXT);
				password.setInputType(InputType.TYPE_CLASS_TEXT
						| InputType.TYPE_TEXT_VARIATION_PASSWORD);
				account.setText("");
				password.setText("");
				break;
			case R.id.landed_user_type_visitor: // 访客
				flag = TYPE_VISITOR;
				account.setInputType(InputType.TYPE_NULL);
				password.setInputType(InputType.TYPE_NULL);
				account.setText("");
				password.setText("");
				break;
			}
		}

	}

	/**
	 * 设置管理员初始账号密码
	 */
	private void DefauitData() {
		db = myHepler.getWritableDatabase();
		cursor = db.query("admin", null, null, null, null, null, null);
		if (cursor.getCount() <= 0) {
			ContentValues values = new ContentValues();
			values.put("admin_account", "javaapk");
			values.put("admin_password", "123456");
			db.insert("admin", null, values);
		}
		cursor.close();
		db.close();
		myHepler.close();
	}

	/**
	 * 
	 * @author 设置按键监听
	 * 
	 */
	private class BtnListener implements OnClickListener {
		Intent intent = new Intent();

		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.landed:
				Landed();
				break;
			case R.id.login:
				startActivity(intent.setClass(MainActivity.this, Login.class));
				break;
			case R.id.quit:
				showDialog(0);	//设置弹窗提示
				break;
			}
		}

	}

	/**
	 * 登陆响应
	 */
	private void Landed() {
		if (flag != TYPE_VISITOR) {
			if (account.getText().toString().equals("")
					|| password.getText().toString().equals("")) {
				Toast.makeText(getApplicationContext(), "账号或密码不能为空，请重新输入",
						Toast.LENGTH_SHORT).show();
				account.setText("");
				password.setText("");
				return;
			}
		}
		Intent intent = new Intent();
		db = myHepler.getWritableDatabase();
		switch (flag) {
		case TYPE_ADMIN:
			cursor = db.query("admin", null, null, null, null, null, null);
			cursor.moveToFirst();
			if (cursor.getString(cursor.getColumnIndex("admin_account"))
					.equals(account.getText().toString())
					&& cursor
							.getString(cursor.getColumnIndex("admin_password"))
							.equals(password.getText().toString())) {
				startActivity(intent.setClass(MainActivity.this,
						AdminMain.class));
				account.setText("");
				password.setText("");
			} else {
				Toast.makeText(getApplicationContext(), "管理者账号或密码错误，请重新输入",
						Toast.LENGTH_SHORT).show();
				account.setText("");
				password.setText("");
			}
			cursor.close();
			db.close();
			myHepler.close();
			break;
		case TYPE_PUBLISHER:
			int temp = 0;
			cursor = db.query("publisher", null, null, null, null, null, null);
			for (int i = 0; i < cursor.getCount(); i++) {
				cursor.moveToPosition(i);
				if (cursor
						.getString(cursor.getColumnIndex("publisher_account"))
						.equals(account.getText().toString())
						&& cursor.getString(
								cursor.getColumnIndex("publisher_password"))
								.equals(password.getText().toString())) {
					temp = 1;

					Bundle bundle = new Bundle();
					bundle.putString("account", account.getText().toString());
					bundle.putString("pass", password.getText().toString());
					account.setText("");
					password.setText("");
					Intent intent2 = new Intent();
					intent2.setClass(MainActivity.this, ProductPublish.class);
					intent2.putExtras(bundle);
					startActivity(intent2);
				}
			}
			if (temp == 0) {
				Toast.makeText(getApplicationContext(), "发布者账号或密码错误，请重新输入",
						Toast.LENGTH_SHORT).show();
				account.setText("");
				password.setText("");
			}
			cursor.close();
			db.close();
			myHepler.close();
			break;
		case TYPE_COMMON:
			int temp1 = 0;
			cursor = db.query("common", null, null, null, null, null, null);
			for (int i = 0; i < cursor.getCount(); i++) {
				cursor.moveToPosition(i);
				if (cursor.getString(cursor.getColumnIndex("common_account"))
						.equals(account.getText().toString())
						&& cursor.getString(
								cursor.getColumnIndex("common_password"))
								.equals(password.getText().toString())) {
					temp1 = 1;
					Bundle bundle = new Bundle();
					bundle.putString("account", account.getText().toString());
					bundle.putString("pass", password.getText().toString());
					bundle.putString("type", "common");
					account.setText("");
					password.setText("");
					Intent intent2 = new Intent();
					intent2.setClass(MainActivity.this, NormalUsers.class);
					intent2.putExtras(bundle);
					startActivity(intent2);
				}
			}
			if (temp1 == 0) {
				Toast.makeText(getApplicationContext(), "普通用户账号或密码错误，请重新输入",
						Toast.LENGTH_SHORT).show();
				account.setText("");
				password.setText("");
			}
			cursor.close();
			db.close();
			myHepler.close();
			break;
		case TYPE_VISITOR:
			Bundle bundle = new Bundle();
			bundle.putString("account", account.getText().toString());
			bundle.putString("pass", password.getText().toString());
			bundle.putString("type", "visitor");
			account.setText("");
			password.setText("");
			Intent intent2 = new Intent();
			intent2.setClass(MainActivity.this, NormalUsers.class);
			intent2.putExtras(bundle);
			startActivity(intent2);
			break;
		}
	}
	
	/**
	 * 弹窗
	 */
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch (id) {
		case 0:
			return show();
		}
		return super.onCreateDialog(id);
	}
	/**
	 * 弹窗显示
	 */
	private Dialog show() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("确定退出？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						/* User clicked Yes so do some stuff */
						MainActivity.this.finish();
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
