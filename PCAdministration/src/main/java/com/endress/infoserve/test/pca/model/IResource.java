/**
 * 
 */
package com.endress.infoserve.test.pca.model;

import java.util.List;

import com.endress.infoserve.persistence.model.IPersistable;

/**
 *
 */
public interface IResource extends IPersistable {
	public String getResourceName();

	public void setResourceName(String newResourceName);

	public void addUserResourcePermission(IUserResourcePermission newUserResourcePermission);

	public void removeUserResourcePermission(IUserResourcePermission oldUserResourcePermission);

	public List<IUserResourcePermission> getUserResourcePermissions();
}
