package com.endress.infoserve.test.pca.model;

import java.util.List;

import com.endress.infoserve.persistence.model.IPersistable;

public interface IRole extends IPersistable {
	public static final String ROLENAME_ADMIN = "Administrator";
	public static final String ROLENAME_STANDARD = "StandardUser";

	public String getRolename();

	public void setRolename(String rolename);

	public List<IUser> getUsers();

	public void setUsers(List<IUser> users);

	public void addUser(IUser newUser);

	public void removeUser(IUser oldUser);
}
