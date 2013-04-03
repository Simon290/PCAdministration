/**
 * 
 */
package com.endress.infoserve.test.pca.model;

import com.endress.infoserve.persistence.model.IPersistable;

/**
 *
 */
public interface IUserResourcePermission extends IPersistable {

	public IUser getUser();

	public void setUser(IUser newUser);

	public IResource getResource();

	public void setResource(IResource newResource);

	public IPermission getPermission();

	public void setPermission(IPermission newPermission);
}
