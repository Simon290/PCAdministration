/**
 * 
 */
package com.endress.infoserve.test.pca.model;

import java.util.List;

import com.endress.infoserve.persistence.model.IPersistable;

/**
 *
 */
public interface IPermission extends IPersistable {
	public static final String PERMISSION_NAME_READ = "READ";
	public static final String PERMISSION_NAME_CREATE = "CREATE";
	public static final String PERMISSION_NAME_UPDATE = "UPDATE";
	public static final String PERMISSION_NAME_DELETE = "DELETE";

	public String getPermissionName();

	public void setPermissionName(String newPermissionName);

	public void addUserResourcePermission(IUserResourcePermission newUserResourcePermission);

	public void removeUserResourcePermission(IUserResourcePermission oldUserResourcePermission);

	public List<IUserResourcePermission> getUserResourcePermissions();

	/**
	 * Permissions with higher privilege level encompass those with lower privilege level.
	 * 
	 * @return
	 */
	public int getPrivilegeLevel();
}
