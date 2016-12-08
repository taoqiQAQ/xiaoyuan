package com.qz.transaction;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class AdminAddUser extends Activity {
	private Button back;
	private Button del;
	private Button sure;
	private EditText account;
	private EditText password1;
	private EditText password2;
	private EditText name;
	private EditText contact;
	private RadioGroup radioGroup;
	private String userType = "��Ʒ������";
	
	private MyHelper myHelper;
	private SQLiteDatabase db;
	Cursor cursor;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.admin_useroperate_add_change);
		
		back = (Button)findViewById(R.id.useroperate_add_change_back);
		del = (Button)findViewById(R.id.useroperate_add_change_del);
		sure = (Button)findViewById(R.id.useroperate_sure);
		radioGroup = (RadioGroup)findViewById(R.id.useroperate_type);
		account = (EditText)findViewById(R.id.useroperate_account);
		password1 = (EditText)findViewById(R.id.useroperate_password);
		password2 = (EditText)findViewById(R.id.useroperate_password_again);
		name = (EditText)findViewById(R.id.useroperate_name);
		contact = (EditText)findViewById(R.id.useroperate_contact);
		
		back.setOnClickListener(new BtnListener());
		radioGroup.setOnCheckedChangeListener(new CheckListener());
		sure.setOnClickListener(new BtnListener());
		del.setVisibility(View.GONE);
		
		myHelper = new MyHelper(this);
	}
	
	private class CheckListener implements OnCheckedChangeListener {

		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub
			switch (checkedId) {
			case R.id.useroperate_type_publisher:
				userType = "��Ʒ������";
				contact.setInputType(InputType.TYPE_CLASS_TEXT);
				break;

			case R.id.useroperate_type_common:
				userType = "��ͨ�û�";
				contact.setInputType(InputType.TYPE_NULL);
				break;
			}
		}

	}
	
	private class BtnListener implements OnClickListener{

		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.useroperate_add_change_back:
				AdminAddUser.this.finish();
				break;

			case R.id.useroperate_sure:
				db = myHelper.getWritableDatabase();
				if (password1.getText().toString()
						.equals(password2.getText().toString())) {
					if (userType == "��Ʒ������") {
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
							values.put("type", userType);
							values.put("publisher_name", name.getText()
									.toString());
							values.put("publisher_account", account.getText()
									.toString());
							values.put("publisher_password", password1
									.getText().toString());
							values.put("contact", contact.getText().toString());
							db.insert("publisher", null, values);
							Toast.makeText(getApplicationContext(),
									"��ӳɹ�", Toast.LENGTH_SHORT).show();
							account.setText("");
							password1.setText("");
							password2.setText("");
							name.setText("");
							contact.setText("");
							
						}
						cursor.close();
					} else if (userType == "��ͨ�û�") {
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
							values.put("type", userType);
							values.put("common_name", name.getText().toString());
							values.put("common_account", account.getText()
									.toString());
							values.put("common_password", password1.getText()
									.toString());
							db.insert("common", null, values);
							Toast.makeText(getApplicationContext(),
									"��ӳɹ�", Toast.LENGTH_SHORT).show();
							account.setText("");
							password1.setText("");
							password2.setText("");
							name.setText("");
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
				break;
			}
		}
		
	}
}
