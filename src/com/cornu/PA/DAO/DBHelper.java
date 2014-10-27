package com.cornu.PA.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DBHelper extends SQLiteOpenHelper {

	public final static int VERSION=1;
	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);	
	}
	public DBHelper(Context context, String name,int version) {
		super(context, name, null, version);	
	}
	public DBHelper(Context context, String name) {
		super(context, name, null, VERSION);	
	}
	public DBHelper(Context context) {
		super(context, "PersonalAccount", null, VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		System.out.println("create db");
		db.execSQL("create table Payment (ID INTEGER PRIMARY KEY,RemoteID int,State int ,PaymentCategoryID int," +
					"AccountID int,Amount real,CreateTime date,Place nvarchar(50),Remarks nvarchar(50),UserID int)");
		db.execSQL("create table PaymentCategory(ID INTEGER PRIMARY KEY,RemoteID int,State int ,CategoryName nvarchar(50),UserID int )");
		db.execSQL("create table Account (ID INTEGER PRIMARY KEY,RemoteID int,State int ,AccountName nvarchar(50),Balance real,UserID int)");
		db.execSQL("create table User(ID INTEGER PRIMARY KEY,RemoteID int,UserName nvarchar(50),Password nvarchar(50),Email nvarchar(50))");
		

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}
}
