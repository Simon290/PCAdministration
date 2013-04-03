/**
 * 
 */
package com.endress.infoserve.test.pca.impl;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DiscriminatorOptions;
import org.hibernate.annotations.Index;

import com.endress.infoserve.persistence.model.Persistable;
import com.endress.infoserve.persistence.model.RelationManagement;
import com.endress.infoserve.test.pca.model.IPermission;
import com.endress.infoserve.test.pca.model.IResource;
import com.endress.infoserve.test.pca.model.IUser;
import com.endress.infoserve.test.pca.model.IUserResourcePermission;

@Entity
@Table(name = "um_user_resource_permissions")
@DiscriminatorColumn(name = "class_id", discriminatorType = DiscriminatorType.INTEGER)
@DiscriminatorOptions(force = true)
@DiscriminatorValue(value = "1004")
@AttributeOverrides(value = { @AttributeOverride(name = "id", column = @Column(name = "urp_id")) })
public class UserResourcePermission extends Persistable implements IUserResourcePermission {

	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "user_id")
	@Index(name = "USER_FK_IDX")
	private IUser user;

	@ManyToOne(targetEntity = Permission.class, fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "permission_id")
	@Index(name = "PERMISSION_FK_IDX")
	private IPermission permission;

	@ManyToOne(targetEntity = Resource.class, fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "resource_id")
	@Index(name = "RESOURCE_FK_IDX")
	private IResource resource;

	/**
	 * 
	 */
	protected UserResourcePermission() {
		super();
	}

	public UserResourcePermission(IUser newUser, IResource newResource, IPermission newPermission) {
		setUser(newUser);
		setResource(newResource);
		setPermission(newPermission);
	}

	/**
	 * @see com.endress.infoserve.test.pca.model.IUserResourcePermission#getUser()
	 */
	public IUser getUser() {
		return user;
	}

	/**
	 * @see com.endress.infoserve.test.pca.model.IUserResourcePermission#setUser(com.endress.infoserve.test.pca.model.IUser)
	 */
	@RelationManagement(thisSide = "user", relationSide = "userResourcePermissions")
	public void setUser(IUser newUser) {
		user = newUser;
	}

	/**
	 * @see com.endress.infoserve.test.pca.model.IUserResourcePermission#getResource()
	 */
	public IResource getResource() {
		return resource;
	}

	/**
	 * @see com.endress.infoserve.test.pca.model.IUserResourcePermission#setResource(com.endress.infoserve.test.pca.model.IResource)
	 */
	@RelationManagement(thisSide = "resource", relationSide = "userResourcePermissions")
	public void setResource(IResource newResource) {
		resource = newResource;
	}

	/**
	 * @see com.endress.infoserve.test.pca.model.IUserResourcePermission#getPermission()
	 */
	public IPermission getPermission() {
		return permission;
	}

	/**
	 * @see com.endress.infoserve.test.pca.model.IUserResourcePermission#setPermission(com.endress.infoserve.test.pca.model.IPermission)
	 */
	@RelationManagement(thisSide = "permission", relationSide = "userResourcePermissions")
	public void setPermission(IPermission newPermission) {
		permission = newPermission;
	}

	@Override
	public String toString() {
		return "user: " + getUser().getName() + " resource: " + getResource().getResourceName() + " permission: "
				+ getPermission().getPermissionName();
	}
}
