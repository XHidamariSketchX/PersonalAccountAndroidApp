package com.cornu.PA.bean;

import java.io.Serializable;


public class Account implements Serializable{
	public static int ID_NEW=0;
	public static int STATE_NEW=1;
	public static int STATE_SYNCED=2;
	private int id;
	private int remoteID;
	private int state=STATE_NEW;
	private String accountName;
	private Float balance;
	private User user;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public int getRemoteID() {
		return remoteID;
	}
	public void setRemoteID(int remoteID) {
		this.remoteID = remoteID;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public Float getBalance() {
		return balance;
	}
	public void setBalance(Float balance) {
		this.balance = balance;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
}
