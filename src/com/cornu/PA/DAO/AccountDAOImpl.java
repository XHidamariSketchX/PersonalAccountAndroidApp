package com.cornu.PA.DAO;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cornu.PA.bean.Account;
import com.cornu.PA.bean.User;

public class AccountDAOImpl implements AccountDAO{
	private DBHelper dbhelper;
	public AccountDAOImpl(DBHelper dbhelper){
		this.dbhelper=dbhelper;
	}
	public List<Account> getAll(User user) {
		SQLiteDatabase db=dbhelper.getReadableDatabase();
		List<Account> accountList=new ArrayList<Account>();
		Account a;
		try {
			Cursor cs=db.query("Account", new String[]{"ID","RemoteID","State","AccountName","Balance"}, "UserID=?", new String[]{user.getRemoteID()+""}, null, null, "RemoteID asc");
			while(cs.moveToNext()){
				System.out.println("Account getAll");
				a=new Account();
				a.setId(cs.getInt(cs.getColumnIndex("ID")));
				a.setRemoteID(cs.getInt(cs.getColumnIndex("RemoteID")));
				a.setState(cs.getInt(cs.getColumnIndex("State")));
				a.setAccountName(cs.getString(cs.getColumnIndex("AccountName")));
				a.setBalance(cs.getFloat(cs.getColumnIndex("Balance")));
				a.setUser(user);
				accountList.add(a);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			db.close();
		}
		return accountList;
	}
	public void save(Account account) {
		SQLiteDatabase db=dbhelper.getWritableDatabase();
		try {
			DataConverter dc=new DataConverter();
			db.insert("Account", "AccountName", dc.AccountTOContenValues(account));
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			db.close();
		}
		
		
	}
	public void saveList(List<Account> accountList) {
		Account account;
		for(int i=0;i<accountList.size();i++){
			account=accountList.get(i);
			this.save(account);
		}
	}
	public List<Account> getUnSynced(User user) {
		SQLiteDatabase db=dbhelper.getReadableDatabase();
		List<Account> accountList=new ArrayList<Account>();
		Account a;
		try {
			Cursor cs=db.query("Account", new String[]{"ID","RemoteID","State","AccountName","Balance"}, "UserID=? and State=?", new String[]{user.getRemoteID()+"",""+Account.STATE_NEW}, null, null, "RemoteID asc");
			while(cs.moveToNext()){
				a=new Account();
				a.setId(cs.getInt(cs.getColumnIndex("ID")));
				a.setRemoteID(cs.getInt(cs.getColumnIndex("RemoteID")));
				a.setState(cs.getInt(cs.getColumnIndex("State")));
				a.setAccountName(cs.getString(cs.getColumnIndex("AccountName")));
				a.setBalance(cs.getFloat(cs.getColumnIndex("Balance")));
				a.setUser(user);
				accountList.add(a);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return accountList;
	}
	public List<Integer> getSyncedRemoteIDList(User user) {
		SQLiteDatabase db=dbhelper.getReadableDatabase();
		List<Integer> IDList=new ArrayList<Integer>();
		Integer i;
		try {
			Cursor cs=db.query("Account", new String[]{"RemoteID"}, "UserID=? and State=?", new String[]{""+user.getRemoteID(),""+Account.STATE_SYNCED}, null, null, "RemoteID asc");
			while(cs.moveToNext()){
				
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
	public Account getOneByID(User user,int id) {
		SQLiteDatabase db=dbhelper.getReadableDatabase();
		Account a=new Account();
		try {
			Cursor cs=db.query("Account", new String[]{"ID","RemoteID","State","AccountName","Balance"}, "UserID=? and ID=?", new String[]{user.getRemoteID()+"",id+""}, null, null, null);
			cs.moveToNext();
			a.setId(cs.getInt(cs.getColumnIndex("ID")));
			a.setRemoteID(cs.getInt(cs.getColumnIndex("RemoteID")));
			a.setState(cs.getInt(cs.getColumnIndex("State")));
			a.setAccountName(cs.getString(cs.getColumnIndex("AccountName")));
			a.setBalance(cs.getFloat(cs.getColumnIndex("Balance")));
			a.setUser(user);
			return a;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			db.close();
		}
		return null;
	}
	public Account getOneByRemoteID(User user, int remoteID) {
		SQLiteDatabase db=dbhelper.getReadableDatabase();
		Account a=new Account();
		try {
			Cursor cs=db.query("Account", new String[]{"ID","RemoteID","State","AccountName","Balance"}, "UserID=? and RemoteID=?", new String[]{user.getRemoteID()+"",remoteID+""}, null, null, null);
			cs.moveToNext();
			a.setId(cs.getInt(cs.getColumnIndex("ID")));
			a.setRemoteID(cs.getInt(cs.getColumnIndex("RemoteID")));
			a.setState(cs.getInt(cs.getColumnIndex("State")));
			a.setAccountName(cs.getString(cs.getColumnIndex("AccountName")));
			a.setBalance(cs.getFloat(cs.getColumnIndex("Balance")));
			a.setUser(user);
			return a;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			db.close();
		}
		return null;
	}

}
