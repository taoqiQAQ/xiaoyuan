package com.qz.zqb;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.qz.transaction.MyHelper;
import com.qz.transaction.R;

public class PersonalSetting extends Activity {
	private EditText old;
	private EditText news;
	private EditText sure_news;
	private EditText userName;
	private EditText connectWay;
	private Button save;
	private Button sure;
	private Button back;

	private String str_old = new String();
	private String str_news = new String();
	private String str_sure_news = new String();
	private String str_username = new String();
	private String str_userpass = new String();
	private String str_bundlePass = new String();
	private String str_bundleAccount = new String();
	Listener butListener = new Listener();
	MyHelper helper;
	SQLiteDatabase db;
	Cursor cursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.personalsetting);
		old = (EditText) super.findViewById(R.id.old);
		news = (EditText) super.findViewById(R.id.news);
		sure_news = (EditText) super.findViewById(R.id.sure_news);
		userName = (EditText) super.findViewById(R.id.userName);
		connectWay = (EditText) super.findViewById(R.id.connectWay);
		save = (Button) super.findViewById(R.id.save);
		sure = (Button) super.findViewById(R.id.sure);
		back = (Button) findViewById(R.id.back);

		save.setOnClickListener(butListener);
		sure.setOnClickListener(butListener);
		back.setOnClickListener(butListener);

		helper = new MyHelper(this);
		ShowNameContact();

	}

	//显示用户名和联系方式
	private void ShowNameContact() {
		Bundle bundle = getIntent().getExtras();
		str_bundleAccount = bundle.getString("account");
		str_bundlePass = bundle.getString("pass");
		db = helper.getWritableDatabase();
		cursor = db
				.query("publisher", null, null, null, null, null, null, null);
		if (cursor != null) {
			for (int i = 0; i < cursor.getCount(); i++) {
				cursor.moveToPosition(i);
				String account = cursor.getString(cursor
						.getColumnIndex("publisher_account"));
				if (account.equals(str_bundleAccount)) {
					String name = cursor.getString(cursor
							.getColumnIndex("publisher_name"));
					String contact = cursor.getString(cursor
							.getColumnIndex("contact"));
					userName.setText(name);
					connectWay.setText(contact);
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
			db = helper.getWritableDatabase();
			switch (v.getId()) {
			case R.id.save:
				InfoChange();
				break;
			case R.id.sure:
				PasswordChange();
				break;
			case R.id.back:
				PersonalSetting.this.finish();
				break;
			}
		}

	}
	
	//联系方式与用户名信息修改
	private void InfoChange(){
		cursor = db.query("publisher", null, null, null, null, null,
				null, null);
		if (!userName.getText().toString().equals("")) {
			if (cursor != null) {
				String name = userName.getText().toString();
				String contact = connectWay.getText().toString();
				ContentValues contentValues = new ContentValues();
				contentValues.put("publisher_name", name);
				contentValues.put("contact", contact);
				db.update("publisher", contentValues,
						"publisher_account = ?",
						new String[] { str_bundleAccount });
				Toast.makeText(getApplicationContext(), "信息修改成功",
						Toast.LENGTH_SHORT).show();
				userName.setText("");
				connectWay.setText("");

			}
		}else{
			Toast.makeText(getApplicationContext(), "用户名不能为空",
					Toast.LENGTH_SHORT).show();
			
		}
		cursor.close();
		db.close();
		helper.close();
	}
	
	//密码修改
	private void PasswordChange(){
		cursor = db.query("publisher", null, null, null, null, null,
				null, null);
		str_news = news.getText().toString();
		str_old = old.getText().toString();
		str_sure_news = sure_news.getText().toString();
		if (str_bundlePass.equals(str_old)) {
			if (str_news.equals(str_sure_news)) {
				if (cursor != null) {
					ContentValues contentValues = new ContentValues();
					contentValues.put("publisher_password",
							str_sure_news);
					db.update("publisher", contentValues,
							"publisher_account = ?",
							new String[] { str_bundleAccount });
					Toast.makeText(getApplicationContext(), "修改成功",
							Toast.LENGTH_SHORT).show();
					old.setText("");
					news.setText("");
					sure_news.setText("");
				}
			}else{
				Toast.makeText(getApplicationContext(), "两次输入密码不同",
						Toast.LENGTH_SHORT).show();
				old.setText("");
				news.setText("");
				sure_news.setText("");
				return;
			}
		}else{
			Toast.makeText(getApplicationContext(), "原密码错误",
					Toast.LENGTH_SHORT).show();
			old.setText("");
			news.setText("");
			sure_news.setText("");
			return;
		}
		cursor.close();
		db.close();
		helper.close();
	}
}
