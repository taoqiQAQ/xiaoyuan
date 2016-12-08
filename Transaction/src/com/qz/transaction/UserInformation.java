package com.qz.transaction;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class UserInformation extends Activity {
	private Button back;
	private Button del;
	private Button sure;
	private TextView text;
	private TextView title;
	private TextView contactText;
	private EditText account;
	private EditText password1;
	private EditText password2;
	private EditText name;
	private EditText contact;
	private RadioGroup radioGroup;
	private String userType = null;
	private String type;
	private String username;

	private MyHelper myHelper;
	private SQLiteDatabase db;
	Cursor cursor;
	Cursor cursor1;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.admin_useroperate_add_change);

		back = (Button) findViewById(R.id.useroperate_add_change_back);
		del = (Button) findViewById(R.id.useroperate_add_change_del);
		sure = (Button) findViewById(R.id.useroperate_sure);
		radioGroup = (RadioGroup) findViewById(R.id.useroperate_type);
		account = (EditText) findViewById(R.id.useroperate_account);
		password1 = (EditText) findViewById(R.id.useroperate_password);
		password2 = (EditText) findViewById(R.id.useroperate_password_again);
		name = (EditText) findViewById(R.id.useroperate_name);
		contact = (EditText) findViewById(R.id.useroperate_contact);
		text = (TextView) findViewById(R.id.useroperate_password_text);
		title = (TextView) findViewById(R.id.user_select_type);
		contactText = (TextView) findViewById(R.id.useroperate_contact_text);

		text.setVisibility(View.GONE);
		password2.setVisibility(View.GONE);
		radioGroup.setVisibility(View.GONE);
		title.setVisibility(View.VISIBLE);

		back.setOnClickListener(new BtnListener());
		sure.setOnClickListener(new BtnListener());
		del.setOnClickListener(new BtnListener());

		myHelper = new MyHelper(this);
		UserInfo();
	
	}
/**
 * 显示用户信息
 */
	private void UserInfo() {
		Bundle b = getIntent().getExtras();
		type = b.getString("type");
		username = b.getString("name");
		db = myHelper.getWritableDatabase();
		if (type.equals("商品发布者")) {
			cursor = db.query("publisher", null, null, null, null, null, null);
			for (int i = 0; i < cursor.getCount(); i++) {
				cursor.moveToPosition(i);
				if (cursor.getString(cursor.getColumnIndex("publisher_name"))
						.equals(username)) {
					title.setText(type);
					name.setText(cursor.getString(
							cursor.getColumnIndex("publisher_name")).toString());
					account.setText(cursor.getString(
							cursor.getColumnIndex("publisher_account"))
							.toString());
					password1.setInputType(InputType.TYPE_CLASS_TEXT);
					password1.setText(cursor.getString(
							cursor.getColumnIndex("publisher_password"))
							.toString());
					contact.setText(cursor.getString(
							cursor.getColumnIndex("contact")).toString());
				}
			}
			cursor.close();
		}
		if (type.equals("普通用户")) {
			contactText.setVisibility(View.GONE);
			cursor = db.query("common", null, null, null, null, null, null);
			for (int i = 0; i < cursor.getCount(); i++) {
				cursor.moveToPosition(i);
				if (cursor.getString(cursor.getColumnIndex("common_name"))
						.equals(username)) {
					title.setText(type);
					name.setText(cursor.getString(
							cursor.getColumnIndex("common_name")).toString());
					account.setText(cursor.getString(
							cursor.getColumnIndex("common_account")).toString());
					password1.setInputType(InputType.TYPE_CLASS_TEXT);
					password1.setText(cursor.getString(
							cursor.getColumnIndex("common_password"))
							.toString());
					contact.setVisibility(View.GONE);
				}
			}
			cursor.close();
		}
		db.close();
		myHelper.close();

	}

	private class BtnListener implements OnClickListener {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.useroperate_add_change_back:
				UserInformation.this.finish();
				break;

			case R.id.useroperate_add_change_del:
				DeleteUser();
				break;
			case R.id.useroperate_sure:
				UpdataUserData();
				break;
			}
		}

	}
	
	/**
	 * 删除用户
	 */
	private void DeleteUser(){
		db = myHelper.getWritableDatabase();
		if (type.equals("商品发布者")) {
			db.delete("publisher", "publisher_name = ?",
					new String[] { username });
			db.delete("goods","publisher_name = ?",
					new String[] { username });
			account.setText("");
			password1.setText("");
			name.setText("");
			contact.setText("");
			Toast.makeText(getApplicationContext(), "删除成功",
					Toast.LENGTH_SHORT).show();

		}
		if (type.equals("普通用户")) {
			db.delete("common", "common_name = ?",
					new String[] { username });
			db.delete("ordergoods","common_name = ?",
					new String[] { username });
			account.setText("");
			password1.setText("");
			name.setText("");
			Toast.makeText(getApplicationContext(), "删除成功",
					Toast.LENGTH_SHORT).show();

		}
		db.close();
		myHelper.close();
	}
	
	/**
	 * 修改用户信息
	 */
	private void UpdataUserData(){
		db = myHelper.getWritableDatabase();
		if (type.equals("商品发布者")) {
			cursor = db.query("publisher", null, null, null, null,
					null, null);
			ContentValues values = new ContentValues();
			values.put("publisher_account", account.getText().toString());
			values.put("publisher_password", password1.getText().toString());
			values.put("publisher_name", name.getText().toString());
			values.put("contact", contact.getText().toString());
			db.update("publisher", values, "publisher_name = ?",
					new String[] { username });
			account.setText("");
			password1.setText("");
			name.setText("");
			contact.setText("");
			Toast.makeText(getApplicationContext(), "修改成功",
					Toast.LENGTH_SHORT).show();
			cursor.close();

		}
		if (type.equals("普通用户")) {
			cursor = db.query("common", null, null, null, null, null,
					null);
			ContentValues values = new ContentValues();
			values.put("common_account", account.getText().toString());
			values.put("common_password", password1.getText().toString());
			values.put("common_name", name.getText().toString());
			db.update("common", values, "common_name = ?",
					new String[] { username });
			account.setText("");
			password1.setText("");
			name.setText("");
			Toast.makeText(getApplicationContext(), "修改成功",
					Toast.LENGTH_SHORT).show();
			cursor.close();

		}
		db.close();
		myHelper.close();
	}
}
