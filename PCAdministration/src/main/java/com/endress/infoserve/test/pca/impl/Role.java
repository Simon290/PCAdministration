package com.endress.infoserve.test.pca.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DiscriminatorOptions;

import com.endress.infoserve.persistence.model.Persistable;
import com.endress.infoserve.persistence.model.RelationManagement;
import com.endress.infoserve.test.pca.model.IRole;
import com.endress.infoserve.test.pca.model.IUser;

@Entity
@Table(name = "um_roles")
@DiscriminatorColumn(name = "class_id", discriminatorType = DiscriminatorType.INTEGER)
@DiscriminatorOptions(force = true)
@DiscriminatorValue(value = "1002")
@AttributeOverrides(value = { @AttributeOverride(name = "id", column = @Column(name = "role_id")) })
public class Role extends Persistable implements IRole {

	@Column(nullable = false)
	private String rolename;

	@ManyToMany(targetEntity = User.class, mappedBy = "roles", fetch = FetchType.LAZY)
	private List<IUser> users;

	protected Role() {
		// default constructor required by PFW/Hibernate
		super();
		users = new ArrayList<IUser>();
	}

	public Role(final String newRole, final IUser newUser) {
		this();
		rolename = newRole;
		users.add(newUser);
	}

	public Role(final String newRolename) {
		this();
		rolename = newRolename;
	}

	/**
	 * @return the rolename
	 */
	public String getRolename() {
		return rolename;
	}

	/**
	 * @param rolename the rolename to set
	 */
	public void setRolename(final String rolename) {
		this.rolename = rolename;
	}

	/**
	 * @return the users
	 */
	public List<IUser> getUsers() {
		return users;
	}

	/**
	 * @param users the users to set
	 */
	public void setUsers(final List<IUser> users) {
		this.users = users;
	}

	// ---- Add/Remove ----
	@RelationManagement(thisSide = "users", relationSide = "roles")
	public void addUser(final IUser newUser) {
		users.add(newUser);
	}

	@RelationManagement(thisSide = "users", relationSide = "roles")
	public void removeUser(final IUser oldUser) {
		users.remove(oldUser);
	}

	/**
	 * @see com.endress.infoserve.persistence.model.CommonPersistable#toString()
	 */
	@Override
	public String toString() {
		return getRolename();
	}
}
