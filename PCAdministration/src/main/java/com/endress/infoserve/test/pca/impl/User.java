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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.DiscriminatorOptions;
import org.hibernate.annotations.OptimisticLock;

import com.endress.infoserve.persistence.model.Persistable;
import com.endress.infoserve.persistence.model.RelationManagement;
import com.endress.infoserve.test.pca.model.IPermission;
import com.endress.infoserve.test.pca.model.IResource;
import com.endress.infoserve.test.pca.model.IRole;
import com.endress.infoserve.test.pca.model.IUser;
import com.endress.infoserve.test.pca.model.IUserResourcePermission;

@Entity
@Table(name = "um_users")
@DiscriminatorColumn(name = "class_id", discriminatorType = DiscriminatorType.INTEGER)
@DiscriminatorOptions(force = true)
@DiscriminatorValue(value = "1000")
@AttributeOverrides(value = { @AttributeOverride(name = "id", column = @Column(name = "user_id")) })
public class User extends Persistable implements IUser {

	// public static final String P_NAME = "name";
	// public static final String P_EMAIL = "email";
	// public static final String P_PASSWORD = "password";
	// public static final String P_FIRSTNAME = "firstname";
	// public static final String P_LASTNAME = "lastname";
	// public static final String P_GENDER = "gender";
	// public static final String P_COUNTRY = "country";
	// public static final String P_ADRESS = "adress";
	// public static final String P_LOCATION = "location";
	// public static final String P_PLZ = "plz";
	// public static final String P_DESCRIPTION = "description";

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "password")
	private String password;

	@Column(name = "email")
	private String email;

	@Column(name = "firstname")
	private String firstname;

	@Column(name = "lastname")
	private String lastname;

	@Column(name = "gender")
	private String gender;

	@Column(name = "country")
	private String country;

	@Column(name = "adress")
	private String adress;

	@Column(name = "location")
	private String location;

	@Column(name = "plz")
	private String plz;

	@Column(name = "description")
	private String description;

	@ManyToMany(fetch = FetchType.LAZY, targetEntity = Role.class)
	@JoinTable(name = "um_user_roles", joinColumns = @JoinColumn(name = "user_oid"), inverseJoinColumns = @JoinColumn(name = "role_oid"))
	private final List<IRole> roles;

	@OneToMany(targetEntity = UserResourcePermission.class, mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OptimisticLock(excluded = true)
	private final List<IUserResourcePermission> userResourcePermissions;

	/**
	 * 
	 */
	protected User() {
		// default constructor required by PFW/Hibernate
		super();
		roles = new ArrayList<IRole>();
		userResourcePermissions = new ArrayList<IUserResourcePermission>();
	}

	public User(String newName) {
		this();
		setName(newName);
	}

	public User(String newName, String newPassword, String newEmail, String newFirstname, String newLastname, String newGender,
			String newCountry, String newAdress, String newLocation, String newPlz) {
		this();
		setName(newName);
		setPassword(newPassword);
		setEmail(newEmail);
		setFirstname(newFirstname);
		setLastname(newLastname);
		setGender(newGender);
		setCountry(newCountry);
		setAdress(newAdress);
		setLocation(newLocation);
		setPlz(newPlz);

	}

	public User(String newName, String newPassword, String newEmail, String newFirstname, String newLastname, String newGender,
			String newCountry, String newAdress, String newLocation, String newPlz, String newDescription) {
		this();
		setName(newName);
		setPassword(newPassword);
		setEmail(newEmail);
		setFirstname(newFirstname);
		setLastname(newLastname);
		setGender(newGender);
		setCountry(newCountry);
		setAdress(newAdress);
		setLocation(newLocation);
		setPlz(newPlz);
		setDescription(newDescription);

	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param newName the name to set
	 */
	public void setName(String newName) {
		this.name = newName;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param newPassword the password to set
	 */
	public void setPassword(String newPassword) {
		this.password = newPassword;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the firstname
	 */
	public String getFirstname() {
		return firstname;
	}

	/**
	 * @param firstname the firstname to set
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	/**
	 * @return the lastname
	 */
	public String getLastname() {
		return lastname;
	}

	/**
	 * @param lastname the lastname to set
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the adress
	 */
	public String getAdress() {
		return adress;
	}

	/**
	 * @param adress the adress to set
	 */
	public void setAdress(String adress) {
		this.adress = adress;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the plz
	 */
	public String getPlz() {
		return plz;
	}

	/**
	 * @param plz the plz to set
	 */
	public void setPlz(String plz) {
		this.plz = plz;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the roles
	 */
	public List<IRole> getRoles() {
		return roles;
	}

	/**
	 * @return the resrouces
	 */
	public List<IUserResourcePermission> getUserResourcePermissions() {
		return userResourcePermissions;
	}

	/**
	 * @return the role
	 */
	// ---- Add/Remove ----
	@RelationManagement(thisSide = "roles", relationSide = "users")
	public void addRole(IRole newRole) {
		roles.add(newRole);
	}

	@RelationManagement(thisSide = "roles", relationSide = "users")
	public void removeRole(IRole oldRole) {
		roles.remove(oldRole);
	}

	@RelationManagement(thisSide = "userResourcePermissions", relationSide = "user")
	public void addUserResourcePermission(IUserResourcePermission newUserResourcePermission) {
		userResourcePermissions.add(newUserResourcePermission);
	}

	@RelationManagement(thisSide = "userResourcePermissions", relationSide = "user")
	public void removeUserResourcePermission(IUserResourcePermission oldUserResourcePermission) {
		userResourcePermissions.remove(oldUserResourcePermission);
	}

	public boolean hasRole(String checkRoleName) {
		boolean isRoleFound = false;
		for (IRole currentRole : getRoles()) {
			if (StringUtils.equals(currentRole.getRolename(), checkRoleName)) {
				isRoleFound = true;
			}
		}
		return isRoleFound;
	}

	public boolean hasResourcePermission(IResource checkResource, IPermission checkPermission) {
		boolean isPermissionFound = false;
		for (IUserResourcePermission currentURP : getUserResourcePermissions()) {
			if (currentURP.getResource().equals(checkResource)) {
				isPermissionFound = currentURP.getPermission().getPrivilegeLevel() >= checkPermission.getPrivilegeLevel();
			}
		}
		return isPermissionFound;
	}
}
