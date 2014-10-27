package com.cornu.PA.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.net.Socket;
import java.net.UnknownHostException;



public class SocketClient implements Serializable{
	 Socket sktClient=null;
	 private ObjectInputStream objReader;
	 private ObjectOutputStream objWriter;

	 String action;
	 public SocketClient(String ip,int port) throws StreamCorruptedException, IOException {
		sktClient= new Socket(ip, port);
		objWriter=new ObjectOutputStream(sktClient.getOutputStream());	
		objReader=new ObjectInputStream(sktClient.getInputStream());
	 }
	 public void closeConnection() throws IOException{
		 if(sktClient!=null){
			 this.sktClient.close();
		 }		 		
	 }
	 public void sendObject(Object obj) throws IOException{
		 objWriter.writeObject(obj);
	 }
	 public Object getObject() throws Exception{
		 return objReader.readObject();
	 }
}
