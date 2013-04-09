package com.endress.infoserve.test.pca.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;

import com.endress.infoserve.persistence.PersistenceContext;
import com.endress.infoserve.persistence.PersistenceManager;
import com.endress.infoserve.persistence.model.IPersistable;
import com.endress.infoserve.persistence.util.HibernatePersistenceFrameworkInitializer;
import com.endress.infoserve.persistence.util.HibernateUtility;
import com.endress.infoserve.persistence.util.IPersistenceUtility;
import com.endress.infoserve.test.pca.model.IPC;
import com.endress.infoserve.test.pca.model.IRole;
import com.endress.infoserve.test.pca.model.IUser;
import com.endress.infoserve.test.pca.query.UserQuery;

public class SessionContext extends PersistenceContext {

	private static final Logger logger = Logger.getLogger(SessionContext.class);

	public static final String HIBERNATE_CFG_XML = "hibernate-pca.cfg.xml";

	protected PersistenceManager persistenceManager;

	protected static ThreadLocal<SessionContext> sc = new ThreadLocal<SessionContext>();
	private final Map<String, Object> data = new HashMap<String, Object>(6);
	private IUser loggedInUser = null;

	public static SessionContext getInstance() {
		return sc.get();
	}

	/**
	 * Method use for cleanup of ThreadLocal for SessionContext, e.g. end of HTTP Request switch to different thread in next
	 * request (SessionFilter).
	 * 
	 * @param newSessionContext
	 */
	public static void setInstance(SessionContext newSessionContext) {
		sc.set(newSessionContext);
		if (newSessionContext != null) {
			setPersistenceManager(newSessionContext.persistenceManager);
		}
		else {
			setPersistenceManager(null);
		}
	}

	/**
	 * @param userName
	 */
	public boolean initByUser(String userName) {
		initialize(HIBERNATE_CFG_XML);
		getData().put("_username", userName);
		getPersistenceManager().setUserid(userName);

		return true;
	}

	public String getPassword() {
		return (String)getData().get("_password");
	}

	public String getUsername() {
		return (String)getData().get("_username");
	}

	public IUser getLoggedInUser() {
		if (loggedInUser == null) {
			UserQuery queryByName = new UserQuery(PersistenceContext.getPersistenceManager());
			queryByName.setUserName(getUsername());
			List<IUser> resultUser = queryByName.execute();
			loggedInUser = resultUser.get(0);
		}
		return loggedInUser;

	}

	public void initialize(String hibernateConfigFile) {
		sc.set(this);

		Configuration configuration = new Configuration();
		configuration.configure(hibernateConfigFile);
		HibernatePersistenceFrameworkInitializer hpfi = new HibernatePersistenceFrameworkInitializer();
		hpfi.initialize(configuration, hibernateConfigFile, getAnnotatedClasses(), false);

		persistenceManager = PersistenceContext.getPersistenceManager();
	}

	private Map<String, Object> getData() {
		return data;
	}

	public Session getDbSessionForClass(Class<? extends IPersistable> resultEntityClass) {
		Session associatedSession;

		IPersistenceUtility pu = getPersistenceManager().getPersistenceUtility().getDelegatePersistenceUtilityForClass(
				resultEntityClass.getCanonicalName());
		if ((pu != null) && (pu instanceof HibernateUtility)) {
			associatedSession = ((HibernateUtility)pu).getSession();
		}
		else {
			throw new RuntimeException("Cannot call getDbSessionForClass() for Non-Hibernate DB backend/PersistenceUtility");
		}

		return associatedSession;
	}

	@SuppressWarnings("unchecked")
	public static Class<IPersistable>[] getAnnotatedClasses() {
		return new Class[] { IPC.class, IRole.class, IUser.class };
	}
}
