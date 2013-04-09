package com.endress.infoserve.test.pca.query;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.endress.infoserve.persistence.PersistenceContext;
import com.endress.infoserve.test.pca.impl.User;
import com.endress.infoserve.test.pca.model.IUser;

public class UserQueryTest extends GeneralTestCase {

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		User user1 = new User("my name");
		user1.setCountry("de");
		user1.setDescription("Some text as description");
		user1.setEmail("NewEmail@email.de");
		user1.setFirstname("NewFirstName");
		user1.setLastname("NewLastname");
		user1.setGender("M");
		user1.setLocation("Freiburg");
		user1.setPassword("newPassword");
		user1.setPlz("newPLZ");
		user1.setAdress("newadress");

		User user2 = new User("my second Name");
		user2.setCountry("de");

		commit();

	}

	@Test
	public void test01newUser() {

		UserQuery query = new UserQuery(PersistenceContext.getPersistenceManager());
		query.setUserName("my name");
		Collection<IUser> allUser = query.execute();
		assertFalse(allUser.isEmpty());
		for (IUser currentUser : allUser) {
			assertEquals("my name", currentUser.getName());
		}
	}

	@Test
	public void test02allUserFromCountry() {
		UserQuery query = new UserQuery(PersistenceContext.getPersistenceManager());
		query.setUserCountry("de");
		Collection<IUser> allUser = query.execute();
		assertTrue(!allUser.isEmpty());
		for (IUser currentUser : allUser) {
			assertEquals("de", currentUser.getCountry());
		}

	}

	@Test
	public void test03getAllUser() {
		UserQuery query = new UserQuery(PersistenceContext.getPersistenceManager());

		Collection<IUser> allUser = query.execute();
		assertTrue(!allUser.isEmpty());
		assertEquals(2, allUser.size());

	}

	@Test
	public void test04UserPassword() {
		UserQuery query = new UserQuery(PersistenceContext.getPersistenceManager());
		query.setUserPassword("newPassword");
		Collection<IUser> allUser = query.execute();
		for (IUser currentUser : allUser) {
			assertEquals("newPassword", currentUser.getPassword());
		}
	}

	@Test
	public void test05UserLocationAndPLZ() {
		UserQuery query = new UserQuery(PersistenceContext.getPersistenceManager());
		query.setUserLocation("Freiburg");
		query.setUserPLZ("newPLZ");
		Collection<IUser> allUser = query.execute();
		for (IUser currentUser : allUser) {
			assertEquals("Freiburg", currentUser.getLocation());
			assertEquals("newPLZ", currentUser.getPlz());
		}
	}

	@Test
	public void test06UserFirstName() {
		UserQuery query = new UserQuery(PersistenceContext.getPersistenceManager());
		query.setUserFirstname("NewFirstName");

		Collection<IUser> allUser = query.execute();
		for (IUser currentUser : allUser) {
			assertEquals("NewFirstName", currentUser.getFirstname());
		}
	}

	@Test
	public void test07UserLastName() {
		UserQuery query = new UserQuery(PersistenceContext.getPersistenceManager());
		query.setUserLastname("NewLastName");

		Collection<IUser> allUser = query.execute();
		for (IUser currentUser : allUser) {
			assertEquals("NewLastName", currentUser.getLastname());
		}
	}

	@Test
	public void test08UserEmail() {
		UserQuery query = new UserQuery(PersistenceContext.getPersistenceManager());
		query.setUserEmail("newEmail@email.de");
		Collection<IUser> allUser = query.execute();
		for (IUser currentUser : allUser) {
			assertEquals("newEmail@email.de", currentUser.getEmail());
		}
	}

	@Test
	public void test09UserGender() {
		UserQuery query = new UserQuery(PersistenceContext.getPersistenceManager());
		query.setUserGender("M");
		Collection<IUser> allUser = query.execute();
		for (IUser currentUser : allUser) {
			assertEquals("M", currentUser.getGender());
		}
	}

	@Test
	public void test10UserDescription() {
		UserQuery query = new UserQuery(PersistenceContext.getPersistenceManager());
		query.setUserDescription("Some text as description");
		Collection<IUser> allUser = query.execute();
		for (IUser currentUser : allUser) {
			assertEquals("Some text as description", currentUser.getDescription());
		}
	}
}
