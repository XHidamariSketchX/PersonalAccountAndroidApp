package com.cornu.PA;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.cornu.PA.DAO.AccountDAO;
import com.cornu.PA.DAO.AccountDAOImpl;
import com.cornu.PA.DAO.DBHelper;
import com.cornu.PA.DAO.PaymentCategoryDAO;
import com.cornu.PA.DAO.PaymentCategoryDAOImpl;
import com.cornu.PA.DAO.PaymentDAO;
import com.cornu.PA.DAO.PaymentDAOImpl;
import com.cornu.PA.bean.Account;
import com.cornu.PA.bean.Payment;
import com.cornu.PA.bean.PaymentCategory;
import com.cornu.PA.bean.User;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddPayment extends Activity {
	Button btnSave,btnCancel;
	EditText edtAmount,edtPlace,edtRemarks;
	Spinner spnPaymentCategory,spnAccount;
	List<Account> accountList;
	List<PaymentCategory> pcList;
	User user;
	SQLiteDatabase db;
	public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
	     setContentView(R.layout.addpayment);
	     Intent intent=this.getIntent();
		 Bundle bundle = intent.getExtras();
		 user=(User)bundle.get("user");
		 this.findViews();
		 this.init();
		
		 this.setListeners();
	 }
	private void init(){
		
		try {
			DBHelper dbHelper=new DBHelper(AddPayment.this);
			AccountDAO accountDAO=new AccountDAOImpl(dbHelper);
			PaymentCategoryDAO pcDAO=new PaymentCategoryDAOImpl(dbHelper);
			pcList=pcDAO.getAll(user);
			accountList=accountDAO.getAll(user);
			String accounts[]=new String[accountList.size()];
			String pcs[]=new String[pcList.size()];
			
			for(int i=0; i<accounts.length;i++){
				
				accounts[i]=accountList.get(i).getAccountName();
				
			}
			
			for(int i=0;i<pcList.size();i++){
				pcs[i]=pcList.get(i).getPaymentCategoryName();
				System.out.println("pcList.RemoteID="+pcList.get(i).getRemoteID());
			
			}
			ArrayAdapter<String> adpAccounts=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, accounts);
			ArrayAdapter<String> adpPC=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, pcs);
			
			adpAccounts.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			adpPC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			
			spnAccount.setAdapter(adpAccounts);
			spnPaymentCategory.setAdapter(adpPC);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	private void findViews(){
		btnCancel=(Button)findViewById(R.id.btnCancel);
		btnSave=(Button)findViewById(R.id.btnSave);
		spnAccount=(Spinner)findViewById(R.id.spnAccount);
		spnPaymentCategory=(Spinner)findViewById(R.id.spnPaymentCategory);
		edtAmount=(EditText)findViewById(R.id.edtAmount);
		edtPlace=(EditText)findViewById(R.id.edtPlace);
		edtRemarks=(EditText)findViewById(R.id.edtRemarks);
	}
	private void setListeners(){
		btnSave.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				try { 
					String a="ID INTEGER PRIMARY KEY,RemoteID int,State int ,PaymentCategoryID int," +
					"AccountID int,Amount real,CreateTime date,Place nvarchar(50),Remarks nvarchar(50),UserID int";
					
					DBHelper dbopen=new DBHelper(AddPayment.this);
					Payment payment=new Payment();
					payment.setRemoteID(Payment.ID_NEW);
					payment.setState(Payment.STATE_NEW);
					
					PaymentCategory pc=new PaymentCategory();
					pc.setRemoteID(pcList.get(spnPaymentCategory.getSelectedItemPosition()).getRemoteID());
					payment.setPaymentCategory(pc);
					
					Account account=new Account();
					account.setRemoteID(accountList.get(spnAccount.getSelectedItemPosition()).getRemoteID());
					payment.setAccount(account);
					
					String strAmount=edtAmount.getText().toString();
					
					
				
		
					Float amount=new Float(strAmount);
					payment.setAmount(amount);
					
					payment.setCreateTime(new Date());
					
					payment.setPlace(edtPlace.getText().toString());
					
					payment.setRemarks(edtRemarks.getText().toString());
					payment.setUser(user);
					
					PaymentDAO paymentDAO=new PaymentDAOImpl(dbopen);
					paymentDAO.save(payment);
					Toast.makeText(AddPayment.this, "保存成功",Toast.LENGTH_LONG).show();
					finish();
					
					
					
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(AddPayment.this, "金额不能为空",Toast.LENGTH_LONG).show();
				}
			}
		});
		btnCancel.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				finish();
			}
		});
	}
}
