package com.endress.infoserve.test.pca.query;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.endress.infoserve.persistence.PersistenceContext;
import com.endress.infoserve.test.pca.impl.PC;
import com.endress.infoserve.test.pca.model.IPC;

public class PCQueryTest extends GeneralTestCase {

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();

		IPC newPC = new PC("newPCName");
		newPC.setDescription("Description for PC");
		newPC.setGraphics("newGraphic");
		newPC.setKeyboard("newKeyboard");
		newPC.setMouse("newMouse");
		newPC.setNetworks("newNetworkcard");
		newPC.setProcessor("newProcessor");

		PC newPC2 = new PC("mySecondPC");
		newPC2.setGraphics("newGraphic");
		commit();

	}

	@Test
	public void test01PCName() {
		PCQuery query = new PCQuery(PersistenceContext.getPersistenceManager());
		query.setPcName("newPCName");
		Collection<IPC> allPCs = query.execute();
		for (IPC currentPC : allPCs) {
			assertEquals("newPCName", currentPC.getName());
		}
	}

	@Test
	public void test02AllPCLength() {
		PCQuery query = new PCQuery(PersistenceContext.getPersistenceManager());
		Collection<IPC> allPCs = query.execute();
		assertTrue(!allPCs.isEmpty());

		// Testing size
		assertEquals(2, allPCs.size());
	}

	@Test
	public void test03PCGraphic() {
		PCQuery query = new PCQuery(PersistenceContext.getPersistenceManager());
		query.setPcGraphics("newGraphic");
		Collection<IPC> allPCs = query.execute();
		for (IPC currentPC : allPCs) {
			assertEquals("newGraphic", currentPC.getGraphics());
		}
	}

	@Test
	public void test04PCNetwork() {
		PCQuery query = new PCQuery(PersistenceContext.getPersistenceManager());
		query.setPcNetworkcard("newNetworkcard");
		Collection<IPC> allPCs = query.execute();
		for (IPC currentPC : allPCs) {
			assertEquals("newNetworkcard", currentPC.getNetworks());
		}
	}

	@Test
	public void test05PCKeyboard() {
		PCQuery query = new PCQuery(PersistenceContext.getPersistenceManager());
		query.setPcKeyboard("newKeyboard");
		Collection<IPC> allPCs = query.execute();
		for (IPC currentPC : allPCs) {
			assertEquals("newKeyboard", currentPC.getKeyboard());
		}
	}

	@Test
	public void test06PCMouse() {
		PCQuery query = new PCQuery(PersistenceContext.getPersistenceManager());
		query.setPcMouse("newMouse");
		Collection<IPC> allPCs = query.execute();
		for (IPC currentPC : allPCs) {
			assertEquals("newMouse", currentPC.getMouse());
		}
	}

	@Test
	public void test07PCProcessor() {
		PCQuery query = new PCQuery(PersistenceContext.getPersistenceManager());
		query.setPcProcessor("newProcessor");
		Collection<IPC> allPCs = query.execute();
		for (IPC currentPC : allPCs) {
			assertEquals("newProcessor", currentPC.getProcessor());
		}
	}

	@Test
	public void test08PCDescription() {
		PCQuery query = new PCQuery(PersistenceContext.getPersistenceManager());
		query.setPcDescription("Description for PC");
		Collection<IPC> allPCs = query.execute();
		for (IPC currentPC : allPCs) {
			assertEquals("Description for PC", currentPC.getDescription());
		}
	}
}
