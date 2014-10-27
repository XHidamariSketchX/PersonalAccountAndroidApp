package com.cornu.PA.business;

import com.cornu.PA.bean.User;

public interface SyncBis {
	public void syncAll(User user);
	public void syncSetting(User user);
}
