package com.endress.infoserve.test.pca.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.webguitoolkit.ui.base.DataBag;
import org.webguitoolkit.ui.base.IDataBag;
import org.webguitoolkit.ui.base.WebGuiFactory;
import org.webguitoolkit.ui.controls.AbstractView;
import org.webguitoolkit.ui.controls.container.ICanvas;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.form.AbstractButtonBarListener;
import org.webguitoolkit.ui.controls.form.Compound;
import org.webguitoolkit.ui.controls.form.IButtonBar;
import org.webguitoolkit.ui.controls.form.ICompound;
import org.webguitoolkit.ui.controls.form.ILabel;
import org.webguitoolkit.ui.controls.layout.ITableLayout;
import org.webguitoolkit.ui.controls.tab.ITab;
import org.webguitoolkit.ui.controls.tab.ITabStrip;
import org.webguitoolkit.ui.controls.table.ITable;
import org.webguitoolkit.ui.controls.table.ITableColumn;
import org.webguitoolkit.ui.controls.util.AbstractMasterDetailListener;

import com.endress.infoserve.persistence.PersistenceContext;
import com.endress.infoserve.persistence.PersistenceException;
import com.endress.infoserve.persistence.query.NonUniqueResultException;
import com.endress.infoserve.security.SecurityCheckEngine;
import com.endress.infoserve.test.pca.impl.PC;
import com.endress.infoserve.test.pca.impl.PCAModelFactory;
import com.endress.infoserve.test.pca.model.IPC;
import com.endress.infoserve.test.pca.model.IPermission;
import com.endress.infoserve.test.pca.model.IUser;
import com.endress.infoserve.test.pca.query.AllPCQuery;
import com.endress.infoserve.test.pca.query.PermissionQuery;
import com.endress.infoserve.test.pca.util.SessionContext;

public class PCView extends AbstractView {

	private static final long serialVersionUID = 1L;
	private static final Log logger = LogFactory.getLog(PCView.class);

	private ITable table;
	private ITab tabSummary;
	private ITab tabDetails;
	private ICompound tabSummaryCompound;
	private ICompound tabDetailsCompound;
	private IButtonBar summaryButtonBar;

	public PCView(WebGuiFactory factory, ICanvas viewConnector) {
		super(factory, viewConnector);
	}

	@Override
	protected void createControls(WebGuiFactory factory, ICanvas viewConnector) {
		ITableLayout layout = factory.createTableLayout(viewConnector);
		ILabel titel = factory.createLabel(layout, "PC + Zubehör");
		titel.addCssClass("wgtHeadline");
		layout.newRow();

		ITableLayout innerLayout = factory.createTableLayout(layout);

		// erstellen der Tabelle
		table = factory.createTable(innerLayout, "Ihr PC + Zubehör", 5);

		// Tabellenspalten anlegen
		ITableColumn col = factory.createTableColumn(table, "PC-Name", PC.PROPERTY_NAME, true);
		col = factory.createTableColumn(table, "Grafikkarte", PC.PROPERTY_GRAPHICS, true);
		col = factory.createTableColumn(table, "Tastatur", PC.PROPERTY_KEYBOARD, true);
		col = factory.createTableColumn(table, "Maus", PC.PROPERTY_MOUSE, true);
		col = factory.createTableColumn(table, "Prozessor", PC.PROPERTY_PROCESSOR, true);
		col.setIsDisplayed(false);
		col = factory.createTableColumn(table, "Netzwerkkarte", PC.PROPERTY_NETWORKCARD, true);
		col.setIsDisplayed(false);

		// Tab anlegen
		innerLayout.newRow();
		ITabStrip tabStrip = factory.createTabStrip(innerLayout);
		tabSummary = factory.createTab(tabStrip, "pcview.tab.summary");
		tabDetails = factory.createTab(tabStrip, "pcview.tab.details");

		MasterDetailListener listener = new MasterDetailListener(table, tabStrip);
		table.setListener(listener);
		tabStrip.setListener(listener);

		createSummaryTabElements();
		createDetailsTabElements();
		fillTable();
		innerLayout.newRow();
	}

	private void createDetailsTabElements() {
		tabDetailsCompound = getFactory().createCompound(tabDetails);
		getFactory().createButtonBar(tabDetailsCompound, IButtonBar.BUTTON_EDIT_CANCEL_SAVE,
				new ButtonBarListener(tabDetailsCompound));

		ICanvas canTabDetails = getFactory().createCanvas(tabDetailsCompound);

		new TabDetailsView(getFactory(), canTabDetails).show();
	}

	private void createSummaryTabElements() {
		tabSummaryCompound = getFactory().createCompound(tabSummary);
		summaryButtonBar = getFactory().createButtonBar(tabSummaryCompound, IButtonBar.BUTTON_NEW_EDIT_DELETE_CANCEL_SAVE,
				new ButtonBarListener(tabSummaryCompound));
		ICanvas canTabSummary = getFactory().createCanvas(tabSummaryCompound);

		new TabSummaryView(getFactory(), canTabSummary).show();
	}

	private void fillTable() {
		IUser loggedInUser = SessionContext.getInstance().getLoggedInUser();
		SecurityCheckEngine securityCheckEngine = new SecurityCheckEngine();

		AllPCQuery pcQuery = new AllPCQuery(SessionContext.getPersistenceManager());
		Collection<IPC> allPCs = pcQuery.execute();

		PermissionQuery permissionQuery = new PermissionQuery(SessionContext.getPersistenceManager(), IPermission.PERMISSION_NAME_READ);

		if (!allPCs.isEmpty()) {
			List<IDataBag> tableData = new ArrayList<IDataBag>();
			try {
				IPermission readPermission = permissionQuery.execute();

				for (IPC currentPC : allPCs) {
					if (securityCheckEngine.hasPermission(SecurityCheckEngine.APP_CONTEXT_PCLIST, loggedInUser,
							readPermission, currentPC.getResource())) {
						IDataBag bag = new DataBag(currentPC);
						tableData.add(bag);
					}
				}
			}
			catch (NonUniqueResultException e) {
				logger.error("Error obtaining permission from database", e);
			}
			table.getDefaultModel().setTableData(tableData);
		}
		table.reload();
	}

	private class ButtonBarListener extends AbstractButtonBarListener {

		private static final long serialVersionUID = 1L;

		public ButtonBarListener(ICompound compound) {
			super(compound);
		}

		@Override
		public void onDelete(ClientEvent event) {

			IDataBag selected = table.getSelectedRow();
			if (selected != null) {
				IPC deletePC = (IPC)selected.getDelegate();
				deletePC.markDeleted();

				try {
					PersistenceContext.getPersistenceManager().commit();
				}
				catch (PersistenceException e) {
					logger.error("Error during commit", e);
				}

				table.removeAndReload(selected);
				preDelete();
			}
		}

		@Override
		public void preDelete() {
			table.selectionChange(table.getFirstRow(), true);
		}

		@Override
		public int persist() {
			try {
				PersistenceContext.getPersistenceManager().commit();
			}
			catch (PersistenceException e) {
				logger.error("Error during commit", e);
				return SAVE_ERROR;
			}

			return SAVE_OK;
		}

		@Override
		public boolean refresh(Object delegate) {
			return false;
		}

		@Override
		public boolean delete(Object delegate) {
			return false;
		}

		@Override
		public void postSave() {
			table.load();
			if (compound.getMode() == Compound.MODE_EDIT) {

				// Edit File from repository
				IUser loggedUser = SessionContext.getInstance().getLoggedInUser();
				IDataBag bag = compound.getBag();
				String textIntoFile = PC.PROPERTY_NAME + ": " + bag.getString("name") + System.getProperty("line.separator")
						+ PC.PROPERTY_GRAPHICS + " : " + bag.getString(PC.PROPERTY_GRAPHICS) + System.getProperty("line.separator")
						+ PC.PROPERTY_KEYBOARD + " : " + bag.getString(PC.PROPERTY_KEYBOARD) + System.getProperty("line.separator")
						+ PC.PROPERTY_MOUSE + " : " + bag.getString(PC.PROPERTY_MOUSE) + System.getProperty("line.separator")
						+ PC.PROPERTY_PROCESSOR + " : " + bag.getString(PC.PROPERTY_PROCESSOR) + System.getProperty("line.separator");
			}
		}

		@Override
		public Object newDelegate() {
			IDataBag bag = compound.getBag();

			PCAModelFactory modelFactory = new PCAModelFactory(SessionContext.getPersistenceManager());
			IPC newPC = modelFactory.newPC(bag.getString("name"), SessionContext.getInstance().getLoggedInUser());
			table.addAndReload(bag);

			return newPC;
		}
	}

	private class MasterDetailListener extends AbstractMasterDetailListener {

		private static final long serialVersionUID = 1L;

		public MasterDetailListener(ITable table, ITabStrip tabstrip) {
			super(table, tabstrip);
		}

		@Override
		public void loadTab(IDataBag row, ITab tab) {
			if (tab == tabSummary) {

				tabSummaryCompound.setBag(row);
				tabSummaryCompound.load();

			}
			if (tab == tabDetails) {
				tabDetailsCompound.setBag(row);
				tabDetailsCompound.load();
			}
		}

		@Override
		public void onRowSelection(ITable table, int rowNumber) {
			IDataBag selected = table.getRow(rowNumber);
			if (selected != null) {
				IPC selectedPC = (IPC)selected.getDelegate();
				IUser loggedInUser = SessionContext.getInstance().getLoggedInUser();

				SecurityCheckEngine securityCheckEngine = new SecurityCheckEngine();

				PermissionQuery permissionQuery = new PermissionQuery(SessionContext.getPersistenceManager(),
						IPermission.PERMISSION_NAME_UPDATE);
				boolean hasUpdatePermission = false;
				try {
					IPermission updatePermission = permissionQuery.execute();
					if (updatePermission != null) {
						hasUpdatePermission = securityCheckEngine.hasPermission(
								SecurityCheckEngine.APP_CONTEXT_BUTTONBAR, loggedInUser, updatePermission, selectedPC.getResource());
					}
				}
				catch (NonUniqueResultException e) {
					logger.error("Could not load 'update' permission from database", e);
				}

				summaryButtonBar.setDisabled(!hasUpdatePermission, new String[] { IButtonBar.BUTTON_EDIT });

				boolean hasDeletePermission = false;
				try {
					IPermission deletePermission = permissionQuery.execute();
					if (deletePermission != null) {
						hasDeletePermission = securityCheckEngine.hasPermission(
								SecurityCheckEngine.APP_CONTEXT_BUTTONBAR, loggedInUser, deletePermission, selectedPC.getResource());
					}
				}
				catch (NonUniqueResultException e) {
					logger.error("Could not load 'delete' permission from database", e);
				}

				summaryButtonBar.setDisabled(!hasDeletePermission, new String[] { IButtonBar.BUTTON_DELETE });
			}
			super.onRowSelection(table, rowNumber);
		}

		@Override
		public boolean leaveTab(IDataBag row, ITab tab) {
			return true;
		}
	}

}
