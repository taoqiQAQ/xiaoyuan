package com.qz.transaction;
import com.qz.zqb.NormalUsers;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AdminMain extends Activity {
	private Button personalSetting;
	private Button userOperate;
	private Button goodsOperate;
	private Button quit;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.admin_main);
        
        personalSetting = (Button)findViewById(R.id.admin_personal_setting);
        userOperate = (Button)findViewById(R.id.admin_user_operate);
        goodsOperate = (Button)findViewById(R.id.admin_goods_operate);
        quit = (Button)findViewById(R.id.admin_quit);
        
        personalSetting.setOnClickListener(new BtnListener());
        userOperate.setOnClickListener(new BtnListener());
        goodsOperate.setOnClickListener(new BtnListener());
        quit.setOnClickListener(new BtnListener());
    }
    
    private class BtnListener implements OnClickListener{
    	Intent  intent = new Intent();
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.admin_personal_setting:
				startActivity(intent.setClass(AdminMain.this, AdminSetting.class));
				break;
			case R.id.admin_user_operate:
				startActivity(intent.setClass(AdminMain.this, UserOperate.class));
				break;
			case R.id.admin_goods_operate:
				startActivity(intent.setClass(AdminMain.this, GoodsOperate.class));
				break;
			case R.id.admin_quit:
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
						AdminMain.this.finish();
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
