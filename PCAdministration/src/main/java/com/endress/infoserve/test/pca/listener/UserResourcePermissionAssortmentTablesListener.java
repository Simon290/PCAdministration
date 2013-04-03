package com.endress.infoserve.test.pca.listener;

import org.apache.log4j.Logger;
import org.webguitoolkit.components.assortmenttables.AbstractAssortmentTablesListener;
import org.webguitoolkit.components.assortmenttables.AssortmentTablesWidget;
import org.webguitoolkit.ui.base.IDataBag;

import com.endress.infoserve.persistence.PersistenceContext;
import com.endress.infoserve.test.pca.model.IUser;

public class UserResourcePermissionAssortmentTablesListener extends AbstractAssortmentTablesListener {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(ResourceAssortmentTablesListener.class);

	private final IUser currentUser;

	public UserResourcePermissionAssortmentTablesListener(int theActionType, AssortmentTablesWidget theAtw, IUser theCurrentUser) {
		super(theActionType, theAtw);
		this.currentUser = theCurrentUser;
	}

	@Override
	public Object determineAssociatedParent() {
		return this.currentUser;
	}

	@Override
	public void addToModel(IDataBag bag, Object parent) {
	}

	@Override
	public void removeFromModel(IDataBag bag, Object parent) {
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
