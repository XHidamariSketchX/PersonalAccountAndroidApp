package com.cornu.PA.DAO;

import java.util.List;

import com.cornu.PA.bean.PaymentCategory;
import com.cornu.PA.bean.User;

public interface PaymentCategoryDAO {
	public PaymentCategory getOneByID(User user,int id);
	public PaymentCategory getOneByRemoteID(User user,int remoteID);
	public List<PaymentCategory> getAll(User user);
	public List<PaymentCategory> getUnSynced(User user);
	public List<Integer> getSyncedRemoteIDList(User user);
	public void save(PaymentCategory pc);
	public void saveList(List<PaymentCategory> pcList);
}
