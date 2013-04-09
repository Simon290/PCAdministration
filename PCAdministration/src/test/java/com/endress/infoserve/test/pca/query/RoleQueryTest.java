package com.endress.infoserve.test.pca.query;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.endress.infoserve.persistence.PersistenceContext;
import com.endress.infoserve.persistence.query.NonUniqueResultException;
import com.endress.infoserve.test.pca.impl.Role;
import com.endress.infoserve.test.pca.model.IRole;

public class RoleQueryTest extends GeneralTestCase {

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();

		IRole newRole = new Role(Role.ROLENAME_ADMIN);
		IRole newRole2 = new Role(Role.ROLENAME_STANDARD);
		commit();

	}

	@Test
	public void test01showRoles() {
		AllRolesQuery query = new AllRolesQuery(PersistenceContext.getPersistenceManager());
		Collection<IRole> allRoles = query.execute();
		assertTrue(!allRoles.isEmpty());
		assertEquals(2, allRoles.size());
	}

	@Test
	public void test02RoleName() throws NonUniqueResultException {
		RoleQuery query = new RoleQuery(PersistenceContext.getPersistenceManager(), Role.ROLENAME_ADMIN);
		IRole foundRole = query.execute();
		assertNotNull(foundRole);
	}
}
