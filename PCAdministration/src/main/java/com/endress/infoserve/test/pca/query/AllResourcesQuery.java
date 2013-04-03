package com.endress.infoserve.test.pca.query;

import java.util.Collection;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;

import com.endress.infoserve.persistence.PersistenceManager;
import com.endress.infoserve.persistence.query.hibernate.BaseCollectionQuery;
import com.endress.infoserve.test.pca.impl.Resource;
import com.endress.infoserve.test.pca.model.IResource;

public class AllResourcesQuery extends BaseCollectionQuery<IResource> {

	public AllResourcesQuery(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	@Override
	protected Collection<IResource> performQuery() {
		Criteria searchCriteria = getDBSession().createCriteria(getSearchClass());
		searchCriteria.addOrder(Order.asc("resourceName").ignoreCase());

		return searchCriteria.list();
	}

	@Override
	protected Class<? extends IResource> getSearchClass() {
		return Resource.class;
	}

}
