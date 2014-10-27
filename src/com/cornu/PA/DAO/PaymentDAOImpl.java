package com.cornu.PA.DAO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cornu.PA.bean.Payment;
import com.cornu.PA.bean.User;

public class PaymentDAOImpl implements PaymentDAO {
	private DBHelper dbhelper;
	public PaymentDAOImpl(DBHelper dbhelper){
		this.dbhelper=dbhelper;
	}
	public void save(Payment payment) {
		SQLiteDatabase db=dbhelper.getWritableDatabase();
		try {
			DataConverter dc=new DataConverter();
			db.insert("Payment", "RemoteID", dc.PaymentTOContenValues(payment));
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			db.close();
		}
		
	}
	public List<Payment> getAll(User user) {
		SQLiteDatabase db=dbhelper.getReadableDatabase();
		PaymentCategoryDAO pcDAO=new PaymentCategoryDAOImpl(dbhelper);
		AccountDAO accountDAO=new AccountDAOImpl(dbhelper);
		List<Payment> paymentList=new ArrayList<Payment>();
		Payment p;
		try {
			Cursor cs=db.query("Payment", new String[]{"ID","RemoteID","State","PaymentCategoryID",
					"AccountID","Amount","CreateTime","Place","Remarks"}, "UserID=?", new String[]{""+user.getRemoteID()}, null, null, "CreateTime desc");
			while(cs.moveToNext()){
				p=new Payment();
				p.setId(cs.getInt(cs.getColumnIndex("ID")));
				p.setRemoteID(cs.getInt(cs.getColumnIndex("RemoteID")));
				p.setState(cs.getInt(cs.getColumnIndex("State")));
				p.setPaymentCategory(pcDAO.getOneByRemoteID(user,cs.getInt(cs.getColumnIndex("PaymentCategoryID"))));
				p.setAccount(accountDAO.getOneByRemoteID(user,cs.getInt(cs.getColumnIndex("AccountID"))));
				p.setAmount(cs.getFloat(cs.getColumnIndex("Amount")));
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				p.setCreateTime(df.parse(cs.getString(cs.getColumnIndex("CreateTime"))));
				p.setPlace(cs.getString(cs.getColumnIndex("Place")));
				p.setRemarks(cs.getString(cs.getColumnIndex("Remarks")));
				paymentList.add(p);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			db.close();
		}
		return paymentList;
	}
	public List<Payment> getUnSynced(User user) {
		SQLiteDatabase db=dbhelper.getReadableDatabase();
		PaymentCategoryDAO pcDAO=new PaymentCategoryDAOImpl(dbhelper);
		AccountDAO accountDAO=new AccountDAOImpl(dbhelper);
		List<Payment> paymentList=new ArrayList<Payment>();
		Payment p;
		try {
			Cursor cs=db.query("Payment", new String[]{"ID","RemoteID","State","PaymentCategoryID",
					"AccountID","Amount","CreateTime","Place","Remarks"}, "UserID=? and State=?", new String[]{""+user.getRemoteID(),""+Payment.STATE_NEW}, null, null, "CreateTime desc");
			while(cs.moveToNext()){
				p=new Payment();
				p.setId(cs.getInt(cs.getColumnIndex("ID")));
				p.setRemoteID(cs.getInt(cs.getColumnIndex("RemoteID")));
				p.setState(cs.getInt(cs.getColumnIndex("State")));
				p.setPaymentCategory(pcDAO.getOneByRemoteID(user,cs.getInt(cs.getColumnIndex("PaymentCategoryID"))));
				p.setAccount(accountDAO.getOneByRemoteID(user,cs.getInt(cs.getColumnIndex("AccountID"))));
				p.setAmount(cs.getFloat(cs.getColumnIndex("Amount")));
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				p.setCreateTime(df.parse(cs.getString(cs.getColumnIndex("CreateTime"))));
				p.setPlace(cs.getString(cs.getColumnIndex("Place")));
				p.setRemarks(cs.getString(cs.getColumnIndex("Remarks")));
				p.setUser(user);
				paymentList.add(p);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			db.close();
		}
		return paymentList;
	}
	public List<Integer> getSyncedRemoteIDList(User user) {
		SQLiteDatabase db=dbhelper.getReadableDatabase();
		List<Integer> IDList=new ArrayList<Integer>();
		Integer i;
		try {
			Cursor cs=db.query("Payment",new String[]{"RemoteID"}, "UserID=? and State=?", new String[]{""+user.getRemoteID(),""+Payment.STATE_SYNCED}, null, null, "RemoteID asc");
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
	public void saveList(List<Payment> paymentList) {
		Payment payment;
		for(int i=0;i<paymentList.size();i++){
			payment=paymentList.get(i);
			this.save(payment);
		}
		
	}
	public void update(Payment payment) {
		SQLiteDatabase db=dbhelper.getWritableDatabase();
		try {
			DataConverter dc=new DataConverter();
			db.update("Payment", dc.PaymentTOContenValues(payment), "ID=?", new String[]{payment.getId()+""});
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			db.close();
		}
		
	}
	public void updateList(List<Payment> paymentList) {
		SQLiteDatabase db=dbhelper.getWritableDatabase();
		try {
			DataConverter dc=new DataConverter();
			for(int i=0;i<paymentList.size();i++){
				db.update("Payment", dc.PaymentTOContenValues(paymentList.get(i)), "ID=?", new String[]{paymentList.get(i).getId()+""});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			db.close();
		}
		
	}
	public void saveOrUpdate(Payment payment) {
		if(payment.getId()==Payment.ID_NEW){
			this.save(payment);
		}
		else{
			this.update(payment);
		}
		
		
	}
	public void saveOrUpdateList(List<Payment> paymentList) {
		for(int i=0;i<paymentList.size();i++){
			this.saveOrUpdate(paymentList.get(i));
		}
	}
	public ArrayList<Payment> getArrayList(User user) {
		SQLiteDatabase db=dbhelper.getReadableDatabase();
		PaymentCategoryDAO pcDAO=new PaymentCategoryDAOImpl(dbhelper);
		AccountDAO accountDAO=new AccountDAOImpl(dbhelper);
		ArrayList<Payment> paymentList=new ArrayList<Payment>();
		Payment p;
		try {
			Cursor cs=db.query("Payment", new String[]{"ID","RemoteID","State","PaymentCategoryID",
					"AccountID","Amount","CreateTime","Place","Remarks"}, "UserID=?", new String[]{""+user.getRemoteID()}, null, null, "CreateTime desc");
			while(cs.moveToNext()){
				p=new Payment();
				p.setId(cs.getInt(cs.getColumnIndex("ID")));
				p.setRemoteID(cs.getInt(cs.getColumnIndex("RemoteID")));
				p.setState(cs.getInt(cs.getColumnIndex("State")));
				p.setPaymentCategory(pcDAO.getOneByRemoteID(user,cs.getInt(cs.getColumnIndex("PaymentCategoryID"))));
				p.setAccount(accountDAO.getOneByRemoteID(user,cs.getInt(cs.getColumnIndex("AccountID"))));
				p.setAmount(cs.getFloat(cs.getColumnIndex("Amount")));
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				p.setCreateTime(df.parse(cs.getString(cs.getColumnIndex("CreateTime"))));
				p.setPlace(cs.getString(cs.getColumnIndex("Place")));
				p.setRemarks(cs.getString(cs.getColumnIndex("Remarks")));
				paymentList.add(p);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			db.close();
		}
		return paymentList;
	}

}
