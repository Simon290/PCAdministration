package com.endress.infoserve.test.pca.listener;

import org.apache.log4j.Logger;
import org.webguitoolkit.components.assortmenttables.AbstractAssortmentTablesListener;
import org.webguitoolkit.components.assortmenttables.AssortmentTablesWidget;
import org.webguitoolkit.ui.base.IDataBag;

import com.endress.infoserve.persistence.PersistenceContext;
import com.endress.infoserve.persistence.query.NonUniqueResultException;
import com.endress.infoserve.test.pca.impl.UserResourcePermission;
import com.endress.infoserve.test.pca.model.IPermission;
import com.endress.infoserve.test.pca.model.IResource;
import com.endress.infoserve.test.pca.model.IUser;
import com.endress.infoserve.test.pca.model.IUserResourcePermission;
import com.endress.infoserve.test.pca.query.PermissionQuery;
import com.endress.infoserve.test.pca.util.SessionContext;

public class ResourceAssortmentTablesListener extends AbstractAssortmentTablesListener {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(ResourceAssortmentTablesListener.class);

	private final IUser currentUser;

	public ResourceAssortmentTablesListener(int theActionType, AssortmentTablesWidget theAtw, IUser theCurrentUser) {
		super(theActionType, theAtw);
		this.currentUser = theCurrentUser;
	}

	@Override
	public Object determineAssociatedParent() {
		return this.currentUser;
	}

	@Override
	public void addToModel(IDataBag bag, Object parent) {
		// it is possible that the bag contains either a Resource or a UserResourcePermission depending on which side
		// of the assortment tables the delegate object was created - in either way, we have to convert a Resource to a
		// UserResourcePermission and vice versa
		if (bag.getDelegate() instanceof IResource) {
			IResource selectedResource = (IResource)bag.getDelegate();
			PermissionQuery permissionQuery = new PermissionQuery(SessionContext.getPersistenceManager(), IPermission.PERMISSION_NAME_READ);
			try {
				IPermission readPermission = permissionQuery.execute();
				if (readPermission != null) {
					bag.setDelegate(new UserResourcePermission(currentUser, selectedResource, readPermission));
				}
			}
			catch (NonUniqueResultException e) {
				logger.error("could not load read-permission from database", e);
			}
		}
		else if (bag.getDelegate() instanceof IUserResourcePermission) {
			IUserResourcePermission oldUserResourcePermission = (IUserResourcePermission)bag.getDelegate();
			currentUser.removeUserResourcePermission(oldUserResourcePermission);
			bag.setDelegate(oldUserResourcePermission.getResource());
		}
	}

	@Override
	public void removeFromModel(IDataBag bag, Object parent) {
		// removing a Resource from the Resource table is handled automatically. Removing a UserResourcePermission requires
		// changes in the database.
		if (bag.getDelegate() instanceof IUserResourcePermission) {
			IUserResourcePermission oldUserResourcePermission = (IUserResourcePermission)bag.getDelegate();
			currentUser.removeUserResourcePermission(oldUserResourcePermission);
			bag.setDelegate(oldUserResourcePermission.getResource());
		}
	}

	// try not to commit here - commit on button bar save instead (method "persist")
	@Override
	public void commit() {
		try {
			PersistenceContext.getPersistenceManager().commit();
		}
		catch (Exception e) {
			logger.error("commit failed", e);
		}
	}
}
