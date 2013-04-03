package com.endress.infoserve.test.pca.query;

import com.endress.infoserve.persistence.PersistenceManager;
import com.endress.infoserve.persistence.query.hibernate.ObjectByNameQuery;
import com.endress.infoserve.test.pca.impl.Permission;
import com.endress.infoserve.test.pca.model.IPermission;

public class PermissionQuery extends ObjectByNameQuery<IPermission> {

	public PermissionQuery(PersistenceManager pm, String permissionName) {
		super(pm, permissionName);
	}

	@Override
	protected Class<? extends IPermission> getSearchClass() {
		return Permission.class;
	}

	@Override
	protected String getSearchFieldName() {
		return "permissionName";
	}

}
