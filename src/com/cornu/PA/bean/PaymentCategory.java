package com.cornu.PA.bean;

import java.io.Serializable;



public class PaymentCategory implements Serializable{
	public static int ID_NEW=0;
	public static int STATE_NEW=1;
	public static int STATE_SYNCED=2;
	private int id;
	private int remoteID;
	private int state;
	private String paymentCategoryName;
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
	public String getPaymentCategoryName() {
		return paymentCategoryName;
	}
	public void setPaymentCategoryName(String paymentCategoryName) {
		this.paymentCategoryName = paymentCategoryName;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
}
