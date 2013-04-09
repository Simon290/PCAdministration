package com.endress.infoserve.test.pca.query;

import java.util.Collection;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;

import com.endress.infoserve.persistence.PersistenceManager;
import com.endress.infoserve.persistence.query.hibernate.BaseCollectionQuery;
import com.endress.infoserve.test.pca.impl.PC;
import com.endress.infoserve.test.pca.model.IPC;

public class AllPCQuery extends BaseCollectionQuery<IPC> {

	public AllPCQuery(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	@Override
	protected Collection<IPC> performQuery() {
		Criteria searchCriteria = getDBSession().createCriteria(getSearchClass());
		searchCriteria.addOrder(Order.asc("name").ignoreCase());

		return searchCriteria.list();
	}

	@Override
	protected Class<? extends IPC> getSearchClass() {
		return PC.class;
	}

}
