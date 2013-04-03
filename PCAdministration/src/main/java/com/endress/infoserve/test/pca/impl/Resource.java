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
import com.endress.infoserve.test.pca.model.IResource;
import com.endress.infoserve.test.pca.model.IUserResourcePermission;

@Entity
@Table(name = "um_resources")
@DiscriminatorColumn(name = "class_id", discriminatorType = DiscriminatorType.INTEGER)
@DiscriminatorOptions(force = true)
@DiscriminatorValue(value = "1003")
@AttributeOverrides(value = { @AttributeOverride(name = "id", column = @Column(name = "resource_id")) })
public class Resource extends Persistable implements IResource {

	@Column(nullable = false)
	private String resourceName;

	@OneToMany(targetEntity = UserResourcePermission.class, mappedBy = "resource", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OptimisticLock(excluded = true)
	private final List<IUserResourcePermission> userResourcePermissions;

	protected Resource() {
		// default constructor required by PFW/Hibernate
		super();
		userResourcePermissions = new ArrayList<IUserResourcePermission>();
	}

	public Resource(final String newResourceName) {
		this();
		setResourceName(newResourceName);
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(final String newResourceName) {
		resourceName = newResourceName;
	}

	/**
	 * @return the users
	 */
	public List<IUserResourcePermission> getUserResourcePermissions() {
		return userResourcePermissions;
	}

	// ---- Add/Remove ----
	@RelationManagement(thisSide = "userResourcePermissions", relationSide = "role")
	public void addUserResourcePermission(final IUserResourcePermission newUserResourcePermission) {
		userResourcePermissions.add(newUserResourcePermission);
	}

	@RelationManagement(thisSide = "userResourcePermissions", relationSide = "role")
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
			urpDelete.markDeleted();
		}
	}

}
