package com.endress.infoserve.test.pca.model;

import java.util.List;

import com.endress.infoserve.persistence.model.IPersistable;

public interface IUser extends IPersistable {
	public String getName();

	public String getPassword();

	public String getFirstname();

	public String getLastname();

	public String getEmail();

	public String getGender();

	public String getCountry();

	public String getAdress();

	public String getLocation();

	public String getPlz();

	public String getDescription();

	public List<IRole> getRoles();

	public void setName(String name);

	public void setPassword(String password);

	public void setFirstname(String value);

	public void setLastname(String value);

	public void setLocation(String location);

	public void setPlz(String plz);

	public void setDescription(String description);

	public void addRole(IRole newRole);

	public void removeRole(IRole oldRole);

	public void addUserResourcePermission(IUserResourcePermission newUserResourcePermission);

	public void removeUserResourcePermission(IUserResourcePermission oldUserResourcePermission);

	public List<IUserResourcePermission> getUserResourcePermissions();

	public boolean hasRole(String checkRoleName);

	public boolean hasResourcePermission(IResource checkResource, IPermission hasPermission);
}
