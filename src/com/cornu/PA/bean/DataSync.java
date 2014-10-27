package com.cornu.PA.bean;

import java.io.Serializable;
import java.util.List;


public class DataSync implements Serializable{
	
	private List<Account> accountList;
	private List<PaymentCategory> pcList;
	private List<Payment> paymentList;
	private List<Integer> accountRemoteIDList;
	private List<Integer> pcRemoteIDList;
	private List<Integer> paymentRemoteIDList;
	private User user;
	public List<Account> getAccountList() {
		return accountList;
	}
	public void setAccountList(List<Account> accountList) {
		this.accountList = accountList;
	}
	public List<PaymentCategory> getPcList() {
		return pcList;
	}
	public void setPcList(List<PaymentCategory> pcList) {
		this.pcList = pcList;
	}
	public List<Payment> getPaymentList() {
		return paymentList;
	}
	public void setPaymentList(List<Payment> paymentList) {
		this.paymentList = paymentList;
	}
	
	public List<Integer> getAccountRemoteIDList() {
		return accountRemoteIDList;
	}
	public void setAccountRemoteIDList(List<Integer> accountRemoteIDList) {
		this.accountRemoteIDList = accountRemoteIDList;
	}
	public List<Integer> getPcRemoteIDList() {
		return pcRemoteIDList;
	}
	public void setPcRemoteIDList(List<Integer> pcRemoteIDList) {
		this.pcRemoteIDList = pcRemoteIDList;
	}
	
	public List<Integer> getPaymentRemoteIDList() {
		return paymentRemoteIDList;
	}
	public void setPaymentRemoteIDList(List<Integer> paymentRemoteIDList) {
		this.paymentRemoteIDList = paymentRemoteIDList;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
}
