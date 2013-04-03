package com.endress.infoserve.test.pca.impl;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.eventmodel.ModelFactory;

import com.endress.infoserve.persistence.AbstractModelFactory;
import com.endress.infoserve.persistence.PersistenceException;
import com.endress.infoserve.persistence.PersistenceManager;
import com.endress.infoserve.persistence.query.NonUniqueResultException;
import com.endress.infoserve.test.pca.model.IPC;
import com.endress.infoserve.test.pca.model.IPermission;
import com.endress.infoserve.test.pca.model.IResource;
import com.endress.infoserve.test.pca.model.IUser;
import com.endress.infoserve.test.pca.query.PermissionQuery;
import com.endress.infoserve.test.pca.util.SessionContext;

public class PCAModelFactory extends AbstractModelFactory {

	private static final Logger logger = Logger.getLogger(ModelFactory.class);

	public PCAModelFactory(PersistenceManager newPersistenceManager) {
		super(newPersistenceManager);
	}

	public PCAModelFactory() {
		super(SessionContext.getPersistenceManager());
	}

	public IPC newPC(String pcName, IUser loggedInUser) {
		IPC newPC = new PC(pcName);

		IResource newPcResource = new Resource("Resource: " + pcName);
		newPC.setResource(newPcResource);

		PermissionQuery permissionQuery = new PermissionQuery(getPersistenceManager(), IPermission.PERMISSION_NAME_DELETE);
		try {
			IPermission readPermission = permissionQuery.execute();
			new UserResourcePermission(loggedInUser, newPcResource, readPermission);
		}
		catch (NonUniqueResultException e) {
			logger.error("Could not obtain permission from database", e);
		}

		return newPC;
	}

	@Override
	public void commit() {
		try {
			SessionContext.getPersistenceManager().commit();
		}
		catch (PersistenceException e) {
			logger.error("commit failed", e);
		}
	}

}
