package com.cornu.PA.DAO;

import com.cornu.PA.bean.User;

public interface UserDAO {
	public User getOneUserByUsername(String username);
	public void save(User user);
}
