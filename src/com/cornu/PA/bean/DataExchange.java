package com.cornu.PA.bean;

import java.io.Serializable;

public class DataExchange implements Serializable{
	public static int RESULT_TYPE_ERROR=0;
	public static int RESULT_TYPE_SUCCESS=1;
	public static int REQUEST_TYPE_USER_LOGIN=2;
	public static int REQUEST_TYPE_SYNC=3;
	public static int REQUEST_TYPE_SYNC_SETTINGS=4;
	public int type;
	public Object object;
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Object getObject() {
		return object;
	}
	public void setObject(Object object) {
		this.object = object;
	}
	

	
	
}
