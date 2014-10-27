package com.cornu.PA.DAO;

import java.util.ArrayList;
import java.util.List;

import com.cornu.PA.bean.Payment;
import com.cornu.PA.bean.User;

public interface PaymentDAO {
	public List<Payment> getAll(User user);
	public ArrayList<Payment> getArrayList(User user);
	public List<Payment> getUnSynced(User user);
	public List<Integer> getSyncedRemoteIDList(User user);
	public void save(Payment payment);
	public void saveList(List<Payment> paymentList);
	public void update(Payment payment);
	public void updateList(List<Payment> paymentList);
	public void saveOrUpdate(Payment payment);
	public void saveOrUpdateList(List<Payment> paymentList);
}
