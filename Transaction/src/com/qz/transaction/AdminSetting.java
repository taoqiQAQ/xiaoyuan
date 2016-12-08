package com.qz.transaction;

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

public class AdminSetting extends Activity {
	private Button back;
	private Button sure;
	private EditText oldPassword;
	private EditText newPassword1;
	private EditText newPassword2;
	
	private MyHelper myHelper;
	private SQLiteDatabase db;
	Cursor cursor;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.admin_personal_setting);
        
        sure = (Button)findViewById(R.id.admin_personal_setting_sure);
        back = (Button)findViewById(R.id.admin_personal_setting_back);
        oldPassword = (EditText)findViewById(R.id.admin_personal_setting_oldpassword);
        newPassword1 = (EditText)findViewById(R.id.admin_personal_setting_newpassword);
        newPassword2 = (EditText)findViewById(R.id.admin_personal_setting_newpassword_again);
        
        back.setOnClickListener(new BtnListener());
        sure.setOnClickListener(new BtnListener());
        
        myHelper = new MyHelper(this);
    }
    
    private class BtnListener implements OnClickListener{

	
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.admin_personal_setting_sure:
				PasswordChange();
				break;

			case R.id.admin_personal_setting_back:
				AdminSetting.this.finish();
				break;
			}
		}
    	
    }
    /**
     * 确认修改密码
     */
    private void PasswordChange(){
    	db = myHelper.getWritableDatabase();
		cursor = db.query("admin", null, null, null, null, null, null);
		cursor.moveToFirst();
		if(!(cursor.getString(cursor.getColumnIndex("admin_password")).equals(oldPassword.getText().toString()))){
			Toast.makeText(getApplicationContext(),
					"原密码错误，请重新输入", Toast.LENGTH_SHORT).show();
			oldPassword.setText("");
			newPassword1.setText("");
			newPassword2.setText("");
			cursor.close();
			db.close();
			myHelper.close();
			return;
		}
		if(!(newPassword1.getText().toString().equals(newPassword2.getText().toString()))){
			Toast.makeText(getApplicationContext(),
					"两次新密码不同，请重新输入", Toast.LENGTH_SHORT).show();
			oldPassword.setText("");
			newPassword1.setText("");
			newPassword2.setText("");
			cursor.close();
			db.close();
			myHelper.close();
			return;
		}
		
			ContentValues values = new ContentValues();
			values.put("admin_password", newPassword1.getText().toString());
			db.update("admin", values, "admin_password = ?", new String[]{oldPassword.getText().toString()});
			oldPassword.setText("");
			newPassword1.setText("");
			newPassword2.setText("");
			Toast.makeText(getApplicationContext(),
					"密码修改成功", Toast.LENGTH_SHORT).show();
		cursor.close();
		db.close();
		myHelper.close();
    }
}
