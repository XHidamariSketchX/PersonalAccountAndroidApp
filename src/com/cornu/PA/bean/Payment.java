package com.cornu.PA.bean;

import java.io.Serializable;
import java.util.Date;
public class Payment implements Serializable{
	public static int ID_NEW=0;
	public static int STATE_NEW=1;
	public static int STATE_SYNCED=2;
	private int id=ID_NEW;
	private int remoteID=ID_NEW;
	private int state=STATE_NEW;
	private PaymentCategory paymentCategory;
	private Account account;
	private Float amount;
	private Date createTime;
	private String place;
	private String remarks;
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
	public PaymentCategory getPaymentCategory() {
		return paymentCategory;
	}
	public void setPaymentCategory(PaymentCategory paymentCategory) {
		this.paymentCategory = paymentCategory;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	public Float getAmount() {
		return amount;
	}
	public void setAmount(Float amount) {
		this.amount = amount;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
}
