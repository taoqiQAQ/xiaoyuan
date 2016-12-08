package com.qz.transaction;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MyHelper extends SQLiteOpenHelper {

	public MyHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	public MyHelper(Context context) {
		super(context, "Transaction", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("create table admin(admin_account varchar(20), admin_password varchar(20))");
		db.execSQL("create table publisher(type varchar(20), publisher_name varchar(20), publisher_account varchar(20),"
				+ "publisher_password varchar(20),contact varchar(20))");
		db.execSQL("create table common(type varchar(20), common_name varchar(20),common_account varchar(20)," +
				"common_password varchar(20))");
		db.execSQL("create table goods(publisher_name varchar(20),goods_name varchar(20),goods_price varchar(20)," +
				"goods_describe varchar(255))");
		db.execSQL("create table ordergoods(common_name varchar(20),goods_name varchar(20))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
