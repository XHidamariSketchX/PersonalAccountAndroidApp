package com.cornu.PA.DAO;

import java.util.List;

import com.cornu.PA.bean.Account;
import com.cornu.PA.bean.User;

public interface AccountDAO {
	public Account getOneByID(User user,int id);
	public Account getOneByRemoteID(User user,int remoteID);
	public List<Account> getAll(User user);
	public List<Account> getUnSynced(User user);
	public List<Integer> getSyncedRemoteIDList(User user);
	public void save(Account account);
	public void saveList(List<Account> accountList);
}
