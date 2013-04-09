package com.endress.infoserve.test.pca.query;

import com.endress.infoserve.persistence.PersistenceManager;
import com.endress.infoserve.persistence.query.hibernate.ObjectByNameQuery;
import com.endress.infoserve.test.pca.impl.Role;
import com.endress.infoserve.test.pca.model.IRole;

/**
 * 
 */
public class RoleQuery extends ObjectByNameQuery<IRole> {

	public RoleQuery(PersistenceManager pm, String searchRolename) {
		super(pm, searchRolename);
	}

	@Override
	protected Class<? extends IRole> getSearchClass() {
		return Role.class;
	}

	@Override
	protected String getSearchFieldName() {
		return "rolename";
	}

}
