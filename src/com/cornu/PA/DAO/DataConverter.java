package com.cornu.PA.DAO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.cornu.PA.bean.Account;
import com.cornu.PA.bean.Payment;
import com.cornu.PA.bean.PaymentCategory;
import com.cornu.PA.bean.User;

import android.content.ContentValues;

public class DataConverter {
	
	public ContentValues UserTOContentValues(User user){
		ContentValues value=new ContentValues();
		value.put("RemoteID", user.getRemoteID());
		value.put("UserName", user.getUsername());
		value.put("Password", user.getPassword());
		value.put("Email", user.getEmail());
		return value;
		
	}
	public ContentValues PaymentTOContenValues(Payment payment){
		ContentValues value=new ContentValues();
		value.put("RemoteID",payment.getRemoteID());
		value.put("State", payment.getState());
		value.put("PaymentCategoryID",payment.getPaymentCategory().getRemoteID());
		value.put("AccountID", payment.getAccount().getRemoteID());
		value.put("Amount",payment.getAmount().toString());
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		value.put("CreateTime",df.format(payment.getCreateTime()));
		value.put("Place",payment.getPlace());
		value.put("Remarks",payment.getRemarks());
		value.put("UserID",payment.getUser().getRemoteID());
		return value;	
	}
	public ContentValues AccountTOContenValues(Account account){
		ContentValues value=new ContentValues();
		value.put("RemoteID",account.getRemoteID());
		value.put("State",account.getState());
		value.put("AccountName", account.getAccountName());
		value.put("Balance", account.getBalance());
		value.put("UserID",account.getUser().getRemoteID());
		return value;	
	}
	public ContentValues PcTOContentValues(PaymentCategory pc){
		ContentValues value=new ContentValues();
		value.put("RemoteID", pc.getRemoteID());
		value.put("State", pc.getState());
		value.put("CategoryName", pc.getPaymentCategoryName());
		value.put("UserID", pc.getUser().getRemoteID());
		return value;
	}
	 
}
