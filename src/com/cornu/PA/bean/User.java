package com.cornu.PA.bean;

import java.io.Serializable;

public class User implements Serializable{
	private int id;
	private int remoteID;
	private String username;
	private String password;
	private String email;
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
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
}
