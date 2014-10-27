package com.cornu.PA.DAO;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cornu.PA.bean.Account;
import com.cornu.PA.bean.PaymentCategory;
import com.cornu.PA.bean.User;

public class PaymentCategoryDAOImpl implements PaymentCategoryDAO{
	private DBHelper dbhelper;
	public PaymentCategoryDAOImpl(DBHelper dbhelper){
		this.dbhelper=dbhelper;
	}
	public List<PaymentCategory> getAll(User user) {
		SQLiteDatabase db=dbhelper.getReadableDatabase();
		List<PaymentCategory> pcList=new ArrayList<PaymentCategory>();
		PaymentCategory pc;
		try {
			Cursor cs=db.query("PaymentCategory", new String[]{"ID","RemoteID","State","CategoryName"}, "UserID=?", new String[]{user.getRemoteID()+""}, null, null, "RemoteID asc");
			while(cs.moveToNext()){
				pc=new PaymentCategory();
				pc.setId(cs.getInt(cs.getColumnIndex("ID")));
				pc.setRemoteID(cs.getInt(cs.getColumnIndex("RemoteID")));
				pc.setState(cs.getInt(cs.getColumnIndex("State")));
				pc.setPaymentCategoryName(cs.getString(cs.getColumnIndex("CategoryName")));
				pc.setUser(user);
				pcList.add(pc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			db.close();
		}
		return pcList;
	}
	public void save(PaymentCategory pc) {
		SQLiteDatabase db=dbhelper.getWritableDatabase();
		try {
			DataConverter dc=new DataConverter();
			db.insert("PaymentCategory", "CategoryName", dc.PcTOContentValues(pc));
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			db.close();
		}
		
		
	}
	public void saveList(List<PaymentCategory> pcList) {
		PaymentCategory pc;
		for(int i=0;i<pcList.size();i++){
			pc=pcList.get(i);
			this.save(pc);
		}
		
	}
	public PaymentCategory getOneByID(User user,int id) {
		SQLiteDatabase db=dbhelper.getReadableDatabase();
		PaymentCategory pc=new PaymentCategory();
		try {
			Cursor cs=db.query("PaymentCategory", new String[]{"ID","RemoteID","State","CategoryName"}, "UserID=? and ID=?", new String[]{user.getRemoteID()+"",id+""}, null, null,null);
			cs.moveToNext();
			pc.setId(cs.getInt(cs.getColumnIndex("ID")));
			pc.setRemoteID(cs.getInt(cs.getColumnIndex("RemoteID")));
			pc.setState(cs.getInt(cs.getColumnIndex("State")));
			pc.setPaymentCategoryName(cs.getString(cs.getColumnIndex("CategoryName")));
			pc.setUser(user);
			return pc;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			db.close();
		}
		return null;
	}
	public List<PaymentCategory> getUnSynced(User user) {
		SQLiteDatabase db=dbhelper.getReadableDatabase();
		List<PaymentCategory> pcList=new ArrayList<PaymentCategory>();
		PaymentCategory pc;
		try {
			Cursor cs=db.query("PaymentCategory", new String[]{"ID","RemoteID","State","CategoryName"}, "UserID=? and State=?", new String[]{""+user.getRemoteID(),""+PaymentCategory.STATE_NEW}, null, null, "CreateTime desc");
			while(cs.moveToNext()){
				pc=new PaymentCategory();
				pc.setId(cs.getInt(cs.getColumnIndex("ID")));
				pc.setRemoteID(cs.getInt(cs.getColumnIndex("RemoteID")));
				pc.setState(cs.getInt(cs.getColumnIndex("State")));
				pc.setPaymentCategoryName(cs.getString(cs.getColumnIndex("CategoryName")));
				pc.setUser(user);
				pcList.add(pc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			db.close();
		}
		return pcList;
	}
	public List<Integer> getSyncedRemoteIDList(User user) {
		SQLiteDatabase db=dbhelper.getReadableDatabase();
		List<Integer> IDList=new ArrayList<Integer>();
		Integer i;
		try {
			Cursor cs=db.query("PaymentCategory", new String[]{"RemoteID"}, "UserID=? and State=?", new String[]{""+user.getRemoteID(),""+PaymentCategory.STATE_SYNCED}, null, null, "RemoteID asc");
			while(cs.moveToNext()){
				System.out.println("getSyncedRemoteIDList");
				i=new Integer(cs.getInt(cs.getColumnIndex("RemoteID")));
				IDList.add(i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			db.close();
		}
		return IDList;
	}
	public PaymentCategory getOneByRemoteID(User user, int remoteID) {
		SQLiteDatabase db=dbhelper.getReadableDatabase();
		PaymentCategory pc=new PaymentCategory();
		try {
			Cursor cs=db.query("PaymentCategory", new String[]{"ID","RemoteID","State","CategoryName"}, "UserID=? and RemoteID=?", new String[]{user.getRemoteID()+"",remoteID+""}, null, null,null);
			cs.moveToNext();
			pc.setId(cs.getInt(cs.getColumnIndex("ID")));
			pc.setRemoteID(cs.getInt(cs.getColumnIndex("RemoteID")));
			pc.setState(cs.getInt(cs.getColumnIndex("State")));
			pc.setPaymentCategoryName(cs.getString(cs.getColumnIndex("CategoryName")));
			pc.setUser(user);
			return pc;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			db.close();
		}
		return null;
	}


}
