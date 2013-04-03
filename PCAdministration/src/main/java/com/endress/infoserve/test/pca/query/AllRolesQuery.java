package com.endress.infoserve.test.pca.query;

import java.util.Collection;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;

import com.endress.infoserve.persistence.PersistenceManager;
import com.endress.infoserve.persistence.query.hibernate.BaseCollectionQuery;
import com.endress.infoserve.test.pca.impl.Role;
import com.endress.infoserve.test.pca.model.IRole;

public class AllRolesQuery extends BaseCollectionQuery<IRole> {

	public AllRolesQuery(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	@Override
	protected Collection<IRole> performQuery() {
		Criteria searchCriteria = getDBSession().createCriteria(getSearchClass());
		searchCriteria.addOrder(Order.asc("rolename").ignoreCase());

		return searchCriteria.list();
	}

	@Override
	protected Class<? extends IRole> getSearchClass() {
		return Role.class;
	}

}
