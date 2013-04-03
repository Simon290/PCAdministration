package com.endress.infoserve.test.pca.query;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.endress.infoserve.persistence.PersistenceManager;
import com.endress.infoserve.persistence.model.IPersistable;
import com.endress.infoserve.persistence.query.hibernate.BaseHibernateQuery;
import com.endress.infoserve.test.pca.impl.User;
import com.endress.infoserve.test.pca.model.IUser;

public class UserQuery extends BaseHibernateQuery<IPersistable> {

	private String userName;
	private Long userOid;
	private String userPassword;
	private String userEmail;
	private String userFirstname;
	private String userLastname;
	private String userGender;
	private String userCountry;
	private String userAdress;
	private String userLocation;
	private String userPLZ;
	private String userDescription;
	private String userRoleName;

	public List<IUser> execute() {
		Criteria criteria = getDBSession().createCriteria(getSearchClass());

		if (userOid != null) {
			criteria.add(Restrictions.eq("OBJ_ID", userOid));
		}
		if (userName != null) {
			criteria.add(Restrictions.eq("name", userName));
		}
		if (userPassword != null) {
			criteria.add(Restrictions.eq("password", userPassword));
		}
		if (userEmail != null) {
			criteria.add(Restrictions.eq("email", userEmail));
		}
		if (userFirstname != null) {
			criteria.add(Restrictions.eq("firstname", userFirstname));
		}
		if (userLastname != null) {
			criteria.add(Restrictions.eq("lastname", userLastname));
		}
		if (userGender != null) {
			criteria.add(Restrictions.eq("gender", userGender));
		}
		if (userCountry != null) {
			criteria.add(Restrictions.eq("country", userCountry));
		}
		if (userAdress != null) {
			criteria.add(Restrictions.eq("adress", userAdress));
		}
		if (userLocation != null) {
			criteria.add(Restrictions.eq("location", userLocation));
		}
		if (userPLZ != null) {
			criteria.add(Restrictions.eq("plz", userPLZ));
		}
		if (userDescription != null) {
			criteria.add(Restrictions.eq("description", userDescription));
		}
		if (userRoleName != null) {
			criteria.add(Restrictions.eq("role_name", userRoleName));
		}

		return criteria.list();
	}

	public UserQuery(PersistenceManager pm) {
		super(pm);
	}

	@Override
	protected Class<? extends IPersistable> getSearchClass() {
		return User.class;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param newUserName the userName to set
	 */
	public void setUserName(String newUserName) {
		this.userName = newUserName;
	}

	/**
	 * @return the userOid
	 */
	public Long getUserOid() {
		return userOid;
	}

	/**
	 * @param userOid the userOid to set
	 */
	public void setUserOid(Long userOid) {
		this.userOid = userOid;
	}

	/**
	 * @return the userPassword
	 */
	public String getUserPassword() {
		return userPassword;
	}

	/**
	 * @param userPassword the userPassword to set
	 */
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	/**
	 * @return the userEmail
	 */
	public String getUserEmail() {
		return userEmail;
	}

	/**
	 * @param userEmail the userEmail to set
	 */
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	/**
	 * @return the userFirstname
	 */
	public String getUserFirstname() {
		return userFirstname;
	}

	/**
	 * @param userFirstname the userFirstname to set
	 */
	public void setUserFirstname(String userFirstname) {
		this.userFirstname = userFirstname;
	}

	/**
	 * @return the userLastname
	 */
	public String getUserLastname() {
		return userLastname;
	}

	/**
	 * @param userLastname the userLastname to set
	 */
	public void setUserLastname(String userLastname) {
		this.userLastname = userLastname;
	}

	/**
	 * @return the userGender
	 */
	public String getUserGender() {
		return userGender;
	}

	/**
	 * @param userGender the userGender to set
	 */
	public void setUserGender(String userGender) {
		this.userGender = userGender;
	}

	/**
	 * @return the userCountry
	 */
	public String getUserCountry() {
		return userCountry;
	}

	/**
	 * @param userCountry the userCountry to set
	 */
	public void setUserCountry(String userCountry) {
		this.userCountry = userCountry;
	}

	/**
	 * @return the userAdress
	 */
	public String getUserAdress() {
		return userAdress;
	}

	/**
	 * @param userAdress the userAdress to set
	 */
	public void setUserAdress(String userAdress) {
		this.userAdress = userAdress;
	}

	/**
	 * @return the userLocation
	 */
	public String getUserLocation() {
		return userLocation;
	}

	/**
	 * @param userLocation the userLocation to set
	 */
	public void setUserLocation(String userLocation) {
		this.userLocation = userLocation;
	}

	/**
	 * @return the userPLZ
	 */
	public String getUserPLZ() {
		return userPLZ;
	}

	/**
	 * @param userPLZ the userPLZ to set
	 */
	public void setUserPLZ(String userPLZ) {
		this.userPLZ = userPLZ;
	}

	/**
	 * @return the userDescription
	 */
	public String getUserDescription() {
		return userDescription;
	}

	/**
	 * @param userDescription the userDescription to set
	 */
	public void setUserDescription(String userDescription) {
		this.userDescription = userDescription;
	}

}
