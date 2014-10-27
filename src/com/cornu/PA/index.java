package com.cornu.PA;


import java.io.IOException;
import java.util.List;

import com.cornu.PA.DAO.AccountDAO;
import com.cornu.PA.DAO.AccountDAOImpl;
import com.cornu.PA.DAO.DBHelper;
import com.cornu.PA.DAO.PaymentCategoryDAO;
import com.cornu.PA.DAO.PaymentCategoryDAOImpl;
import com.cornu.PA.DAO.UserDAO;
import com.cornu.PA.DAO.UserDAOImpl;
import com.cornu.PA.bean.Account;
import com.cornu.PA.bean.DataExchange;
import com.cornu.PA.bean.DataSync;
import com.cornu.PA.bean.PaymentCategory;
import com.cornu.PA.bean.User;
import com.cornu.PA.net.SocketClient;

import android.R.bool;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class index extends Activity {
	Button btnLogin,btnCancel;
    EditText edtUserName,edtPassword;
    EditText edtServerIP,edtPort;
    SocketClient clientNet;
    Context context;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index);
        this.findViews();
        this.setListeners();
    }
    private void findViews(){
    	btnLogin=(Button) findViewById(R.id.btnLogin);
    	btnCancel=(Button) findViewById(R.id.btnCancel);
    	edtPassword=(EditText)findViewById(R.id.edtPassword);
    	edtUserName=(EditText)findViewById(R.id.edtUserName);
    	edtServerIP=(EditText)findViewById(R.id.edtServerIP);
    	edtPort=(EditText)findViewById(R.id.edtPort);
    }
    private void setListeners(){
    	btnLogin.setOnClickListener(new OnClickListener() {
			User loginuser;
			public void onClick(View v) {
				DBHelper dbOpen=new DBHelper(index.this);
				UserDAO userDAO=new UserDAOImpl(dbOpen);
				User user=userDAO.getOneUserByUsername(edtUserName.getText().toString().trim());
				//�״�ʹ�ã���δ���û���¼��
				if(user==null){
					//�ж����������
					ConnectivityManager connManager = (ConnectivityManager)v.getContext().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
					if(connManager==null||connManager.getActiveNetworkInfo()==null){
					//����������
						Toast.makeText(index.this, "�״�ʹ�ã�����WIFI����", Toast.LENGTH_LONG).show();
					}
					else {
						try {
							//���ӷ�����
							clientNet=new SocketClient(edtServerIP.getText().toString(), Integer.parseInt(edtPort.getText().toString()));
							User userLogin=new User();
							userLogin.setUsername(edtUserName.getText().toString());					
							userLogin.setPassword(edtPassword.getText().toString());
							//���͵�¼����
							DataExchange dx=new DataExchange();
							dx.setObject(userLogin);							
							dx.setType(DataExchange.REQUEST_TYPE_USER_LOGIN);
							clientNet.sendObject(dx);
							//��ȡ��¼���
							DataExchange result=(DataExchange)clientNet.getObject();
							if(result.getType()==DataExchange.RESULT_TYPE_SUCCESS){	
								//��¼�ɹ�
								loginuser=(User)result.getObject();
								Toast.makeText(index.this,loginuser.getUsername()+"��¼�ɹ�", Toast.LENGTH_LONG).show();	
								loginuser.setEmail(loginuser.getEmail().trim());
								loginuser.setPassword(loginuser.getPassword().trim());
								loginuser.setUsername(loginuser.getUsername().trim());
								//ͬ������  �˻�  ����
								if(this.syncSetting()){
									Toast.makeText(index.this, "ͬ���ɹ�", Toast.LENGTH_LONG);
									//�����û������ݿ�
									userDAO.save(loginuser);
									//��ת�������� ����¼�û�����Bundle
									Intent intent=new Intent();
									intent.setClass(index.this, Main.class);
									Bundle bundle = new Bundle();
									bundle.putSerializable("user", loginuser);
									bundle.putString("IP", edtServerIP.getText().toString());
									intent.putExtras(bundle);
									index.this.startActivity(intent);	
								}else{
									Toast.makeText(index.this, "ͬ��ʧ��", Toast.LENGTH_LONG);
								}							
							}
							else{
								Toast.makeText(index.this, "��¼ʧ��", Toast.LENGTH_LONG).show();
							}
						} catch (Exception e) {
							Toast.makeText(index.this, "���������쳣", Toast.LENGTH_LONG).show();
							e.printStackTrace();
						}
					}
				}//�û�������¼�� ��������ϴ�����һ��
				else if(user.getPassword().equals(edtPassword.getText().toString().trim())){
					Intent intent=new Intent();
					intent.setClass(index.this, Main.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("user", user);
					bundle.putString("IP", edtServerIP.getText().toString());
					intent.putExtras(bundle);
					index.this.startActivity(intent);
				}//�û�������¼�� ��������ϴ����벻һ��
				else{
					Toast.makeText(index.this, "�������", Toast.LENGTH_LONG).show();
				}
			}
			private boolean syncSetting() throws Exception{
    			//�������ݿ�
    			DBHelper dbhelper=new DBHelper(index.this);
    			//����������
    			DataExchange dx=new DataExchange();
    			//����ͬ������
				dx.setType(DataExchange.REQUEST_TYPE_SYNC_SETTINGS);
				//ͬ����
				DataSync settingSync=new DataSync();
				settingSync.setUser(loginuser);
				dx.setObject(settingSync);
				//��������
				System.out.println("syncSetting�� send sync setting request");
				clientNet.sendObject(dx);
				System.out.println("syncSetting:getting result ");
				dx=(DataExchange)clientNet.getObject();
				System.out.println("syncSetting:got result");
				if(dx.getType()==DataExchange.RESULT_TYPE_SUCCESS){
					DataSync dataSync=(DataSync)dx.getObject();
					AccountDAO accountDAO=new AccountDAOImpl(dbhelper);
					PaymentCategoryDAO pcDAO=new PaymentCategoryDAOImpl(dbhelper);
					accountDAO.saveList(dataSync.getAccountList());
					pcDAO.saveList(dataSync.getPcList());
					return true;
				}
				else{
					return false;
				}
    		}
		});
    	btnCancel.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				index.this.finish();
			}
		});
    }

}