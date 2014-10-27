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
				//无网络连接
					Toast.makeText(Main.this, "首次使用，请检查WIFI连接", Toast.LENGTH_LONG).show();
				}
				else{
					try {
						//创建连接
						clientNet=new SocketClient(IP, 8086);
						
						DataExchange loginDe=new DataExchange();//数据请求对象
						DataExchange resultDe=new DataExchange();//结果对象
						//设置请求类型为登录
						loginDe.setType(DataExchange.REQUEST_TYPE_USER_LOGIN);
						//设置登录请求类为用户
						loginDe.setObject(user);
						System.out.println(""+user.getUsername()+"/");
						//发送登录请求
						clientNet.sendObject(loginDe);
						//获取登录结果
						resultDe=(DataExchange)clientNet.getObject();
						//登录成功
						if(resultDe.getType()==DataExchange.RESULT_TYPE_SUCCESS){					
							user=(User) resultDe.getObject();
							user.setUsername(user.getUsername().trim());
							user.setPassword(user.getPassword().trim());
							Toast.makeText(Main.this, user.getUsername()+"登录成功",Toast.LENGTH_LONG).show();
							this.syncAll();
						}
						//登录失败
						else{
							Toast.makeText(Main.this, user.getUsername()+"登录失败,请重新输入",Toast.LENGTH_LONG).show();
							Intent intent=new Intent();
							intent.setClass(Main.this, index.class);
							Main.this.startActivity(intent);		
						}
						
						
						
					} catch (Exception e) {
						Toast.makeText(Main.this, "网络连接异常", Toast.LENGTH_LONG).show();
						e.printStackTrace();
					}
				}
			
				}	
			private boolean syncAll() throws Exception{
				DBHelper dbHelper=new DBHelper(Main.this);
				
				DataExchange dataexchange=new DataExchange();
				//设置请求类型为同步
				dataexchange.setType(DataExchange.REQUEST_TYPE_SYNC);
				//创建同步数据对象
				DataSync datasync=new DataSync();
				//创建DAO对象
				PaymentCategoryDAO pcDAO=new PaymentCategoryDAOImpl(dbHelper);
				AccountDAO accountDAO=new AccountDAOImpl(dbHelper);
				PaymentDAO paymentDAO=new PaymentDAOImpl(dbHelper);
				//设置用户
				datasync.setUser(user);
				//获取并设置已同步的IDList
				datasync.setAccountRemoteIDList(accountDAO.getSyncedRemoteIDList(user));
				datasync.setPcRemoteIDList(pcDAO.getSyncedRemoteIDList(user));
				datasync.setPaymentRemoteIDList(paymentDAO.getSyncedRemoteIDList(user));
				//获取未同步的Payment 列表
				List<Payment> unSyncedPaymentList=paymentDAO.getUnSynced(user);
				//设置未同步的Payment 列表
				datasync.setPaymentList(unSyncedPaymentList);
				System.out.println("unsynced size is="+unSyncedPaymentList.size());
				//设置同步数据
				dataexchange.setObject(datasync);
				//发送同步请求
				clientNet.sendObject(dataexchange);
				//获取同步结果
				DataExchange syncResultDe=(DataExchange) clientNet.getObject();
				//获取同步结果成功
				if(syncResultDe.getType()==DataExchange.RESULT_TYPE_SUCCESS){
					//获取同步数据
					DataSync dataSync=(DataSync) syncResultDe.getObject();
					//获取服务器端发送过来的列表
					List<Account> accountList=dataSync.getAccountList();
					List<PaymentCategory> pcList=dataSync.getPcList();
					//Payment列表包含   刚从客户端发送过去的同步PaymentList  加上   服务器端的PaymentList
					List<Payment> paymentList=dataSync.getPaymentList();
					
					accountDAO.saveList(accountList);
					pcDAO.saveList(pcList);
					
					paymentDAO.saveOrUpdateList(paymentList);
					System.out.println("accountList size="+accountList.size()+"pcList size="+pcList.size()+"paymentList size="+paymentList.size());
					Toast.makeText(Main.this,"同步成功",Toast.LENGTH_LONG).show();
					return true;
				}
				//获取结果失败
				else{
					Toast.makeText(Main.this,"同步失败",Toast.LENGTH_LONG).show();
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
