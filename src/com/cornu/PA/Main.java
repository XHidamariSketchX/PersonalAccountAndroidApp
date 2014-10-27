package com.cornu.PA;

import java.io.IOException;
import java.io.StreamCorruptedException;
import java.util.List;

import com.cornu.PA.DAO.AccountDAO;
import com.cornu.PA.DAO.AccountDAOImpl;
import com.cornu.PA.DAO.DBHelper;
import com.cornu.PA.DAO.PaymentCategoryDAO;
import com.cornu.PA.DAO.PaymentCategoryDAOImpl;
import com.cornu.PA.DAO.PaymentDAO;
import com.cornu.PA.DAO.PaymentDAOImpl;
import com.cornu.PA.bean.Account;
import com.cornu.PA.bean.DataExchange;
import com.cornu.PA.bean.DataSync;
import com.cornu.PA.bean.Payment;
import com.cornu.PA.bean.PaymentCategory;
import com.cornu.PA.bean.User;
import com.cornu.PA.net.SocketClient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Main extends Activity {
	Button btnNew,btnViewPayment,btnSync,btnSearch;
	User user;
	String IP;
	SocketClient clientNet;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	    Intent intent=this.getIntent();
		Bundle bundle = intent.getExtras();
		user=(User)bundle.get("user");
		IP=bundle.getString("IP");
		clientNet=(SocketClient)bundle.get("clientNet");
	    this.findViews();
	    this.setListeners();
	}
	private void findViews(){
		btnNew=(Button)findViewById(R.id.btnNew);
		btnViewPayment=(Button)findViewById(R.id.btnViewPayment);
		btnSync=(Button)findViewById(R.id.btnSync);
		btnSearch=(Button)findViewById(R.id.btnSearch);
	 }
	 private void setListeners(){
		 btnNew.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(Main.this, AddPayment.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("user", user);
				intent.putExtras(bundle);
				Main.this.startActivity(intent);				
			}
		});
		 btnViewPayment.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				DBHelper dbhelper=new DBHelper(Main.this);
				PaymentDAO paymentDAO=new PaymentDAOImpl(dbhelper);
				
				Intent intent=new Intent();
				intent.setClass(Main.this, ViewPaymentList.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("paymentList", paymentDAO.getArrayList(user));
				intent.putExtras(bundle);
				Main.this.startActivity(intent);	
			}
		});
		 btnSync.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				ConnectivityManager connManager = (ConnectivityManager)v.getContext().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
				if(connManager==null||connManager.getActiveNetworkInfo()==null){
				//����������
					Toast.makeText(Main.this, "�״�ʹ�ã�����WIFI����", Toast.LENGTH_LONG).show();
				}
				else{
					try {
						//��������
						clientNet=new SocketClient(IP, 8086);
						
						DataExchange loginDe=new DataExchange();//�����������
						DataExchange resultDe=new DataExchange();//�������
						//������������Ϊ��¼
						loginDe.setType(DataExchange.REQUEST_TYPE_USER_LOGIN);
						//���õ�¼������Ϊ�û�
						loginDe.setObject(user);
						System.out.println(""+user.getUsername()+"/");
						//���͵�¼����
						clientNet.sendObject(loginDe);
						//��ȡ��¼���
						resultDe=(DataExchange)clientNet.getObject();
						//��¼�ɹ�
						if(resultDe.getType()==DataExchange.RESULT_TYPE_SUCCESS){					
							user=(User) resultDe.getObject();
							user.setUsername(user.getUsername().trim());
							user.setPassword(user.getPassword().trim());
							Toast.makeText(Main.this, user.getUsername()+"��¼�ɹ�",Toast.LENGTH_LONG).show();
							this.syncAll();
						}
						//��¼ʧ��
						else{
							Toast.makeText(Main.this, user.getUsername()+"��¼ʧ��,����������",Toast.LENGTH_LONG).show();
							Intent intent=new Intent();
							intent.setClass(Main.this, index.class);
							Main.this.startActivity(intent);		
						}
						
						
						
					} catch (Exception e) {
						Toast.makeText(Main.this, "���������쳣", Toast.LENGTH_LONG).show();
						e.printStackTrace();
					}
				}
			
				}	
			private boolean syncAll() throws Exception{
				DBHelper dbHelper=new DBHelper(Main.this);
				
				DataExchange dataexchange=new DataExchange();
				//������������Ϊͬ��
				dataexchange.setType(DataExchange.REQUEST_TYPE_SYNC);
				//����ͬ�����ݶ���
				DataSync datasync=new DataSync();
				//����DAO����
				PaymentCategoryDAO pcDAO=new PaymentCategoryDAOImpl(dbHelper);
				AccountDAO accountDAO=new AccountDAOImpl(dbHelper);
				PaymentDAO paymentDAO=new PaymentDAOImpl(dbHelper);
				//�����û�
				datasync.setUser(user);
				//��ȡ��������ͬ����IDList
				datasync.setAccountRemoteIDList(accountDAO.getSyncedRemoteIDList(user));
				datasync.setPcRemoteIDList(pcDAO.getSyncedRemoteIDList(user));
				datasync.setPaymentRemoteIDList(paymentDAO.getSyncedRemoteIDList(user));
				//��ȡδͬ����Payment �б�
				List<Payment> unSyncedPaymentList=paymentDAO.getUnSynced(user);
				//����δͬ����Payment �б�
				datasync.setPaymentList(unSyncedPaymentList);
				System.out.println("unsynced size is="+unSyncedPaymentList.size());
				//����ͬ������
				dataexchange.setObject(datasync);
				//����ͬ������
				clientNet.sendObject(dataexchange);
				//��ȡͬ�����
				DataExchange syncResultDe=(DataExchange) clientNet.getObject();
				//��ȡͬ������ɹ�
				if(syncResultDe.getType()==DataExchange.RESULT_TYPE_SUCCESS){
					//��ȡͬ������
					DataSync dataSync=(DataSync) syncResultDe.getObject();
					//��ȡ�������˷��͹������б�
					List<Account> accountList=dataSync.getAccountList();
					List<PaymentCategory> pcList=dataSync.getPcList();
					//Payment�б����   �մӿͻ��˷��͹�ȥ��ͬ��PaymentList  ����   �������˵�PaymentList
					List<Payment> paymentList=dataSync.getPaymentList();
					
					accountDAO.saveList(accountList);
					pcDAO.saveList(pcList);
					
					paymentDAO.saveOrUpdateList(paymentList);
					System.out.println("accountList size="+accountList.size()+"pcList size="+pcList.size()+"paymentList size="+paymentList.size());
					Toast.makeText(Main.this,"ͬ���ɹ�",Toast.LENGTH_LONG).show();
					return true;
				}
				//��ȡ���ʧ��
				else{
					Toast.makeText(Main.this,"ͬ��ʧ��",Toast.LENGTH_LONG).show();
					return false;
					
				}
			}
			});
		 btnSearch.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
	 }
	 
}
