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
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class Login extends Activity {
	private Button login;
	private Button cancel;
	private EditText account;
	private EditText password1;
	private EditText password2;
	private EditText name;
	private EditText contact;
	private RadioGroup radioGroup;
	private String loginType = "��Ʒ������";

	private MyHelper myHelper;
	private SQLiteDatabase db;
	Cursor cursor;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);

		login = (Button) findViewById(R.id.login_sure);
		cancel = (Button) findViewById(R.id.login_cancel);
		account = (EditText) findViewById(R.id.login_account);
		password1 = (EditText) findViewById(R.id.login_password);
		password2 = (EditText) findViewById(R.id.login_password_again);
		name = (EditText) findViewById(R.id.login_name);
		contact = (EditText) findViewById(R.id.login_contact);
		radioGroup = (RadioGroup) findViewById(R.id.login_user_type);

		login.setOnClickListener(new BtnListener());
		cancel.setOnClickListener(new BtnListener());
		radioGroup.setOnCheckedChangeListener(new CheckListener());

		myHelper = new MyHelper(this);
	}

	private class CheckListener implements OnCheckedChangeListener {

		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub
			switch (checkedId) {
			case R.id.login_user_type_publisher:
				loginType = "��Ʒ������";
				contact.setInputType(InputType.TYPE_CLASS_TEXT);
				break;

			case R.id.login_user_type_common:
				loginType = "��ͨ�û�";
				contact.setInputType(InputType.TYPE_NULL);
				break;
			}
		}

	}

	private class BtnListener implements OnClickListener {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.login_sure:
				LoginSure();
				break;

			case R.id.login_cancel:
				Login.this.finish();
				break;
			}
		}

	}
	
	/**
	 * ȷ��ע��
	 */
	private void LoginSure(){
		db = myHelper.getWritableDatabase();
		if (password1.getText().toString()
				.equals(password2.getText().toString())) {
			if (loginType == "��Ʒ������") {
				int temp = 0;
				cursor = db.query("publisher", null, null, null, null,
						null, null);
				if (cursor != null) {
					for (int i = 0; i < cursor.getCount(); i++) {
						cursor.moveToPosition(i);
						if (account
								.getText()
								.toString()
								.equals(cursor.getString(cursor
										.getColumnIndex("publisher_account")))) {
							Toast.makeText(getApplicationContext(),
									"ע���˺��Ѿ�����", Toast.LENGTH_SHORT)
									.show();
							account.setText("");
							password1.setText("");
							password2.setText("");
							temp = 1;
							return;
						}
						if (name
								.getText()
								.toString()
								.equals(cursor.getString(cursor
										.getColumnIndex("publisher_name")))) {
							Toast.makeText(getApplicationContext(),
									"�û����Ѿ�����", Toast.LENGTH_SHORT)
									.show();
							account.setText("");
							password1.setText("");
							password2.setText("");
							temp = 1;
							return;
						}
					}
				}
				if (temp == 0) {
					ContentValues values = new ContentValues();
					values.put("type", loginType);
					values.put("publisher_name", name.getText()
							.toString());
					values.put("publisher_account", account.getText()
							.toString());
					values.put("publisher_password", password1
							.getText().toString());
					values.put("contact", contact.getText().toString());
					db.insert("publisher", null, values);
					Toast.makeText(getApplicationContext(),
							"ע��ɹ��������µ�¼", Toast.LENGTH_SHORT).show();
					Login.this.finish();
				}
				cursor.close();
			} else if (loginType == "��ͨ�û�") {
				int temp1 = 0;
				cursor = db.query("common", null, null, null, null,
						null, null);
				if (cursor != null) {
					for (int i = 0; i < cursor.getCount(); i++) {
						cursor.moveToPosition(i);
						if (account
								.getText()
								.toString()
								.equals(cursor.getString(cursor
										.getColumnIndex("common_account")))) {
							Toast.makeText(getApplicationContext(),
									"ע���˺��Ѿ�����", Toast.LENGTH_SHORT)
									.show();
							account.setText("");
							password1.setText("");
							password2.setText("");
							temp1 = 1;
							return;
						}
						if (name
								.getText()
								.toString()
								.equals(cursor.getString(cursor
										.getColumnIndex("common_name")))) {
							Toast.makeText(getApplicationContext(),
									"�û����Ѿ�����", Toast.LENGTH_SHORT)
									.show();
							account.setText("");
							password1.setText("");
							password2.setText("");
							temp1 = 1;
							return;
						}
					}
				}
				if (temp1 == 0) {
					ContentValues values = new ContentValues();
					values.put("type", loginType);
					values.put("common_name", name.getText().toString());
					values.put("common_account", account.getText()
							.toString());
					values.put("common_password", password1.getText()
							.toString());
					db.insert("common", null, values);
					Toast.makeText(getApplicationContext(),
							"ע��ɹ��������µ�¼", Toast.LENGTH_SHORT).show();
					Login.this.finish();
				}
				cursor.close();
			}
		} else {
			Toast.makeText(getApplicationContext(), "������������벻ͬ",
					Toast.LENGTH_SHORT).show();
			password1.setText("");
			password2.setText("");
		}

		db.close();
		myHelper.close();
	}
}