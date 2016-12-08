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

public class GoodsInformationChange extends Activity {

	private Button back;
	private Button del;
	private Button sure;
	private EditText name;
	private EditText price;
	private EditText describe;

	private String publisherName;
	private String goodsName;

	private MyHelper myHelper;
	private SQLiteDatabase db;
	Cursor cursor;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.goodsinformation);

		back = (Button) findViewById(R.id.goodsoperate_add_change_back);
		del = (Button) findViewById(R.id.goodsoperate_add_change_del);
		sure = (Button) findViewById(R.id.goodsoperate_sure);
		name = (EditText) findViewById(R.id.goodsoperate_name);
		price = (EditText) findViewById(R.id.goodsoperate_price);
		describe = (EditText) findViewById(R.id.goodsoperate_describe);
		
		back.setOnClickListener(new BtnListener());
		sure.setOnClickListener(new BtnListener());
		del.setOnClickListener(new BtnListener());

		myHelper = new MyHelper(this);

		GoodsInfo();
	}

	private class BtnListener implements OnClickListener {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.goodsoperate_add_change_back:
				GoodsInformationChange.this.finish();
				break;

			case R.id.goodsoperate_add_change_del:
				DeleteGoods();
				break;
			case R.id.goodsoperate_sure:
				UpdataGoodsData();
				break;
			}
		}

	}

	private void GoodsInfo() {
		Bundle b = getIntent().getExtras();
		publisherName = b.getString("publisherName");
		goodsName = b.getString("goodsName");
		db = myHelper.getWritableDatabase();
		cursor = db.query("goods", null, null, null, null, null, null);
		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);
			if (cursor.getString(cursor.getColumnIndex("goods_name")).equals(
					goodsName)) {
				name.setText(cursor.getString(
						cursor.getColumnIndex("goods_name")).toString());
				price.setText(cursor.getString(
						cursor.getColumnIndex("goods_price")).toString());
				describe.setText(cursor.getString(
						cursor.getColumnIndex("goods_describe")).toString());

			}
		}
		cursor.close();
		db.close();
		myHelper.close();

	}

	private void DeleteGoods() {
		db = myHelper.getWritableDatabase();
		cursor = db.query("goods", null, null, null, null, null, null);
		db.delete("goods", "goods_name = ?", new String[] { goodsName });
		name.setText("");
		price.setText("");
		describe.setText("");
		Toast.makeText(getApplicationContext(), "删除成功", Toast.LENGTH_SHORT)
				.show();
		cursor.close();
		db.close();
		myHelper.close();
	}

	private void UpdataGoodsData() {
		db = myHelper.getWritableDatabase();
		cursor = db.query("goods", null, null, null, null, null, null);
		ContentValues values = new ContentValues();
		values.put("goods_name", name.getText().toString());
		values.put("goods_price", price.getText().toString());
		values.put("goods_describe", describe.getText().toString());
		db.update("goods", values, "goods_name = ?",
				new String[] { goodsName });
		name.setText("");
		price.setText("");
		describe.setText("");
		Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT)
				.show();
		cursor.close();
		db.close();
		myHelper.close();
	}

}
