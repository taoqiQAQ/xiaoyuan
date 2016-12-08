package com.qz.zqb;

import com.qz.transaction.MyHelper;
import com.qz.transaction.R;
import com.qz.zqb.Publish.Listener;

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
import android.widget.TextView;
import android.widget.Toast;

public class ProductInfo extends Activity {
	private TextView title;
	private Button delete;
	private Button back;
	private Button save;
	private TextView publisher;
	private EditText name;
	private EditText price;
	private EditText introduce;
	Listener butListener = new Listener();
	private String publisherName;
	private String goodsName;
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
		title = (TextView) findViewById(R.id.publish_title);
		title.setText("��Ʒ��Ϣ");
		helper = new MyHelper(this);
		ShowProductInfo();

		delete.setOnClickListener(butListener);
		back.setOnClickListener(butListener);
		save.setOnClickListener(butListener);

	}

	//��ʾ��Ʒ��Ϣ
	private void ShowProductInfo() {
		Bundle b = getIntent().getExtras();
		publisherName = b.getString("publisher");
		goodsName = b.getString("goods_name");
		String mainUserName = b.getString("mainUserName");
		System.out.println(publisherName + "---------------" + goodsName);
		db = helper.getWritableDatabase();
		cursor = db.query("goods", null, null, null, null, null, null);
		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);
			if (cursor.getString(cursor.getColumnIndex("goods_name")).equals(
					goodsName)) {
				name.setText(cursor.getString(cursor
						.getColumnIndex("goods_name")));

				price.setText(cursor.getString(cursor
						.getColumnIndex("goods_price")));

				introduce.setText(cursor.getString(cursor
						.getColumnIndex("goods_describe")));

				publisher.setText(publisherName);
				
				//����ǲ鿴�������������߷�������Ʒ���������޸ģ���������ɾ���ͱ���ؼ�
				if (!(mainUserName.equals(cursor.getString(cursor
						.getColumnIndex("publisher_name"))))) {
					name.setInputType(InputType.TYPE_NULL);
					price.setInputType(InputType.TYPE_NULL);
					introduce.setInputType(InputType.TYPE_NULL);
					delete.setVisibility(View.GONE);
					save.setVisibility(View.GONE);
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
			db = helper.getWritableDatabase();
			switch (v.getId()) {
			case R.id.delete:
				Delete();
				break;
			case R.id.back:
				ProductInfo.this.finish();
				break;
			case R.id.save:
				Change();
				break;

			}
		}
		
		//ɾ����Ʒ
		private void Delete(){
			cursor = db.query("goods", null, null, null, null, null, null);
			db.delete("goods", "goods_name = ?", new String[] { name
					.getText().toString() });
			price.setText("");
			name.setText("");
			introduce.setText("");
			Toast.makeText(getApplicationContext(), "ɾ���ɹ�",
					Toast.LENGTH_SHORT).show();
			cursor.close();
			db.close();
			helper.close();
		}
		
		//�޸���Ʒ��Ϣ
		private void Change(){
			cursor = db.query("goods", null, null, null, null, null, null);
			if (!price.getText().toString().equals("")
					&& !name.getText().toString().equals("")
					&& !introduce.getText().toString().equals("")) {
				ContentValues contentValues = new ContentValues();
				contentValues.put("publisher_name", publisherName);
				contentValues
						.put("goods_price", price.getText().toString());
				contentValues.put("goods_name", name.getText().toString());
				contentValues.put("goods_describe", introduce.getText()
						.toString());
				db.update("goods", contentValues, "goods_name = ?", new String[]{goodsName});
				Toast.makeText(getApplicationContext(), "��Ʒ�޸ĳɹ�",
						Toast.LENGTH_SHORT).show();
				price.setText("");
				name.setText("");
				introduce.setText("");
			} else {
				Toast.makeText(getApplicationContext(), "������Ʒ��Ϣ����Ϊ��",
						Toast.LENGTH_SHORT).show();
			}
			cursor.close();
			db.close();
			helper.close();
		}

	}

}
