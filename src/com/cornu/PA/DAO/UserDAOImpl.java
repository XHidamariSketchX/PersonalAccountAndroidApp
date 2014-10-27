package com.cornu.PA.DAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cornu.PA.bean.User;

public class UserDAOImpl implements UserDAO {
	private DBHelper dbhelper;
	public UserDAOImpl(DBHelper dbhelper){
		this.dbhelper=dbhelper;
	}
	public User getOneUserByUsername(String username) {
		SQLiteDatabase db=dbhelper.getReadableDatabase();
		try {
			Cursor cs=db.query("User", new String[]{"ID","RemoteID","UserName","Password","Email"}, "UserName=?", new String[]{username}, null, null, null);
			User user=new User();
			if(cs.moveToNext()){
				user.setId(cs.getInt(cs.getColumnIndex("ID")));
				user.setRemoteID(cs.getInt(cs.getColumnIndex("RemoteID")));
				user.setUsername(cs.getString(cs.getColumnIndex("UserName")));
				user.setPassword(cs.getString(cs.getColumnIndex("Password")));
				user.setEmail(cs.getString(cs.getColumnIndex("Email")));
				return user;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			db.close();
		}
		return null;
	}

	public void save(User user) {
		SQLiteDatabase db=dbhelper.getWritableDatabase();
		try {
			DataConverter dc=new DataConverter();
			db.insert("User", "UserName", dc.UserTOContentValues(user));
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			db.close();
		}
		
	}

}
