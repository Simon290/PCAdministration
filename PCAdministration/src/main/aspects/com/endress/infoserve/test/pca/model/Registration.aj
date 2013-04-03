package com.endress.infoserve.test.pca.model;

import com.endress.infoserve.persistence.PersistenceContext;
import com.endress.infoserve.persistence.PersistenceManager;
import com.endress.infoserve.persistence.aspects.PFWRegistration;
import com.endress.infoserve.persistence.model.IPersistable;

public aspect Registration extends PFWRegistration {
	
	public PersistenceManager getPersistenceManager(IPersistable po) {
		return PersistenceContext.getPersistenceManager();
	}
}