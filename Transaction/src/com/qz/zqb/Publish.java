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
import android.widget.TextView;
import android.widget.Toast;

import com.qz.transaction.MyHelper;
import com.qz.transaction.R;

public class Publish extends Activity {
	private Button delete;
	private Button back;
	private Button save;
	private TextView publisher;
	private EditText name;
	private EditText price;
	private EditText introduce;
	Listener butListener = new Listener();
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
		setContentView(R.layout.publish);
		delete = (Button) super.findViewById(R.id.delete);
		back = (Button) super.findViewById(R.id.back);
		save = (Button) super.findViewById(R.id.save);
		publisher = (TextView) super.findViewById(R.id.publisher);
		name = (EditText) super.findViewById(R.id.name);
		price = (EditText) super.findViewById(R.id.price);
		introduce = (EditText) super.findViewById(R.id.introduce);

		delete.setOnClickListener(butListener);
		back.setOnClickListener(butListener);
		save.setOnClickListener(butListener);

		Bundle bundle = getIntent().getExtras();
		str_bundleAccount = bundle.getString("account");
		str_bundleName = bundle.getString("name");
		helper = new MyHelper(getApplicationContext());
		publisher.setText(str_bundleName);
	}
	
	
	//显示商品信息
	private void ShowProductInfo(){
		Bundle b = getIntent().getExtras();
		String publisherName = b.getString("publisher");
		String goodsName = b.getString("goods_name");
		db = helper.getWritableDatabase();
		cursor = db.query("goods", null, null, null, null,
				null, null);
		if(cursor.getString(cursor.getColumnIndex("goods_name")).equals(goodsName)){
			name.setText(cursor.getString(cursor.getColumnIndex("goods_name")));
			price.setText(cursor.getString(cursor.getColumnIndex("goods_price")));
			introduce.setText(cursor.getString(cursor.getColumnIndex("goods_describe")));
		}
		cursor.close();
		db.close();
		helper.close();
	}

	public class Listener implements OnClickListener {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			db = helper.getWritableDatabase();
			switch (v.getId()) {
			case R.id.delete:
				Delete();
				break;
			case R.id.back:
				Publish.this.finish();
				break;
			case R.id.save:
				PublishGoods();
				break;

			}
		}

	}
	
	//发布商品
	private void PublishGoods(){
		cursor = db.query("goods", null, null, null, null, null, null);
		if (!price.getText().toString().equals("")
				&& !name.getText().toString().equals("")
				&& !introduce.getText().toString().equals("")) {
			ContentValues contentValues = new ContentValues();
			contentValues.put("publisher_name", str_bundleName);
			contentValues
					.put("goods_price", price.getText().toString());
			contentValues.put("goods_name", name.getText().toString());
			contentValues.put("goods_describe", introduce.getText()
					.toString());
			db.insert("goods", null, contentValues);
			Toast.makeText(getApplicationContext(), "商品发布成功",
					Toast.LENGTH_SHORT).show();
			price.setText("");
			name.setText("");
			introduce.setText("");
		}else{
			Toast.makeText(getApplicationContext(), "所有商品信息不能为空",
					Toast.LENGTH_SHORT).show();
		}
		cursor.close();
		db.close();
		helper.close();
	}
	
	//删除商品
	private void Delete(){
		cursor = db.query("goods", null, null, null, null,
				null, null);
		db.delete("goods", "goods_name = ?",
				new String[] { name.getText().toString() });
		price.setText("");
		name.setText("");
		introduce.setText("");
		Toast.makeText(getApplicationContext(), "删除成功",
				Toast.LENGTH_SHORT).show();
		cursor.close();
		db.close();
		helper.close();
	}
	
}
