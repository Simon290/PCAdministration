package com.endress.infoserve.test.pca.query;

import com.endress.infoserve.persistence.PersistenceException;
import com.endress.infoserve.persistence.model.IPersistable;
import com.endress.infoserve.persistence.test.HibernateLayer;
import com.endress.infoserve.persistence.test.HsqlTestCase;
import com.endress.infoserve.persistence.test.IDatabaseLayer;
import com.endress.infoserve.test.pca.util.SessionContext;

public class GeneralTestCase extends HsqlTestCase {

	public static final String LOG4J_CONFIG_XML = "/pca-log-conf.xml";
	public static final String HIBERNATE_PCA_TEST_CFG_XML = "hibernate-pca.cfg.xml";

	/**
	 * @return
	 */
	@Override
	protected IDatabaseLayer createDatabaseLayer() {
		return new HibernateLayer(HIBERNATE_PCA_TEST_CFG_XML, HibernateLayer.DEFAULT_HIBERNATE_CONFIG_DIR);
	}

	/**
	 * @see com.endress.infoserve.persistence.test.BaseHibernateTest#getLog4jXmlFilename()
	 */
	@Override
	protected String getLog4jXmlFilename() {
		return LOG4J_CONFIG_XML;
	}

	@Override
	protected Class<IPersistable>[] getAnnotatedClasses() {
		return SessionContext.getAnnotatedClasses();
	}

	protected void commit() {
		try {
			getPersistenceManager().commit();
		}
		catch (PersistenceException e) {
			e.printStackTrace();
		}
	}
}