/**
 * 
 */
package com.endress.infoserve.test.pca.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DiscriminatorOptions;
import org.hibernate.annotations.OptimisticLock;

import com.endress.infoserve.persistence.model.Persistable;
import com.endress.infoserve.persistence.model.RelationManagement;
import com.endress.infoserve.test.pca.model.IPermission;
import com.endress.infoserve.test.pca.model.IUserResourcePermission;

@Entity
@Table(name = "um_permissions")
@DiscriminatorColumn(name = "class_id", discriminatorType = DiscriminatorType.INTEGER)
@DiscriminatorOptions(force = true)
@DiscriminatorValue(value = "1004")
@AttributeOverrides(value = { @AttributeOverride(name = "id", column = @Column(name = "permission_id")) })
public class Permission extends Persistable implements IPermission {

	@Column(nullable = false)
	private String permissionName;

	@Column(name = "privilege_level", nullable = false)
	private int privilegeLevel;

	@OneToMany(targetEntity = UserResourcePermission.class, mappedBy = "permission", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OptimisticLock(excluded = true)
	private final List<IUserResourcePermission> userResourcePermissions;

	protected Permission() {
		// default constructor required by PFW/Hibernate
		super();
		userResourcePermissions = new ArrayList<IUserResourcePermission>();
	}

	public Permission(final String newPermissionName) {
		this();
		setPermissionName(newPermissionName);
	}

	public String getPermissionName() {
		return permissionName;
	}

	public void setPermissionName(final String newPermissionName) {
		permissionName = newPermissionName;
	}

	/**
	 * @return the users
	 */
	public List<IUserResourcePermission> getUserResourcePermissions() {
		return userResourcePermissions;
	}

	/**
	 * @see com.endress.infoserve.test.pca.model.IPermission#getPrivilegLevel()
	 */
	public int getPrivilegeLevel() {
		return privilegeLevel;
	}

	// ---- Add/Remove ----
	@RelationManagement(thisSide = "userResourcePermissions", relationSide = "permission")
	public void addUserResourcePermission(final IUserResourcePermission newUserResourcePermission) {
		userResourcePermissions.add(newUserResourcePermission);
	}

	@RelationManagement(thisSide = "userResourcePermissions", relationSide = "permission")
	public void removeUserResourcePermission(final IUserResourcePermission oldUserResourcePermission) {
		userResourcePermissions.remove(oldUserResourcePermission);
	}

	@Override
	public void markDeleted() {
		removeAllUserResourcePermissions();
		super.markDeleted();
	}

	private void removeAllUserResourcePermissions() {
		List<IUserResourcePermission> urpsToDelete = new ArrayList<IUserResourcePermission>(userResourcePermissions);
		for (IUserResourcePermission urpDelete : urpsToDelete) {
			removeUserResourcePermission(urpDelete);
		}
	}
}
