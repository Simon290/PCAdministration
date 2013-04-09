package com.endress.infoserve.test.pca.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.webguitoolkit.components.assortmenttables.AssortmentTablesWidget;
import org.webguitoolkit.ui.base.DataBag;
import org.webguitoolkit.ui.base.IDataBag;
import org.webguitoolkit.ui.base.WebGuiFactory;
import org.webguitoolkit.ui.controls.AbstractView;
import org.webguitoolkit.ui.controls.container.ICanvas;
import org.webguitoolkit.ui.controls.form.ICompound;
import org.webguitoolkit.ui.controls.form.ILabel;
import org.webguitoolkit.ui.controls.layout.ITableLayout;
import org.webguitoolkit.ui.controls.tab.ITab;
import org.webguitoolkit.ui.controls.tab.ITabStrip;
import org.webguitoolkit.ui.controls.table.AbstractTableListener;
import org.webguitoolkit.ui.controls.table.ITable;
import org.webguitoolkit.ui.controls.table.ITableColumn;

import com.endress.infoserve.persistence.PersistenceContext;
import com.endress.infoserve.test.pca.listener.ResourceAssortmentTablesListener;
import com.endress.infoserve.test.pca.model.IResource;
import com.endress.infoserve.test.pca.model.IUser;
import com.endress.infoserve.test.pca.model.IUserResourcePermission;
import com.endress.infoserve.test.pca.query.AllResourcesQuery;
import com.endress.infoserve.test.pca.query.UserQuery;

public class AdminView extends AbstractView {

	private static final String P_RESOURCE_NAME = "propertyResourceName";
	private static final long serialVersionUID = 1L;
	private final static Log log = LogFactory.getLog(UserView.class);
	private ITable table;
	private static final int textsize = 25;
	private ICompound compound;
	private AssortmentTablesWidget atwResources;

	public AdminView(WebGuiFactory factory, ICanvas viewConnector) {
		super(factory, viewConnector);
	}

	@Override
	protected void createControls(WebGuiFactory factory, ICanvas viewConnector) {
		compound = factory.createCompound(viewConnector);
		ITableLayout layout = factory.createTableLayout(compound);
		ILabel titel = factory.createLabel(layout, "Adminbereich");
		titel.addCssClass("wgtHeadline");
		layout.newRow();

		ITableLayout innerLayout = factory.createTableLayout(layout);
		table = factory.createTable(innerLayout, "Angemeldetete User", 5);
		table.setListener(new UserTableListener());
		// Tabellenspalten anlegen
		ITableColumn col = factory.createTableColumn(table, "Username", "name", true);
		col = factory.createTableColumn(table, "Password", "password", true);
		col = factory.createTableColumn(table, "Email", "email", true);
		col = factory.createTableColumn(table, "Vorname", "firstname", true);
		col.setIsDisplayed(false);
		col = factory.createTableColumn(table, "Nachname", "lastname", true);
		col.setIsDisplayed(false);
		col = factory.createTableColumn(table, "Geschlecht", "gender", true);
		col.setIsDisplayed(false);
		col = factory.createTableColumn(table, "Land", "country", true);
		col.setIsDisplayed(false);
		col = factory.createTableColumn(table, "Adresse", "adress", true);
		col.setIsDisplayed(false);
		col = factory.createTableColumn(table, "Wohnort", "location", true);
		col.setIsDisplayed(false);
		col = factory.createTableColumn(table, "PLZ", "plz", false);
		col.setIsDisplayed(false);
		col = factory.createTableColumn(table, "Rolle", "roles", true);

		layout.newRow();
		ITabStrip tabStrip = factory.createTabStrip(layout);
		ITab resourceTab = factory.createTab(tabStrip, "Resources");
		ITableLayout resourceLayout = factory.createTableLayout(resourceTab);
		ICanvas resourceCanvas = factory.createCanvas(resourceLayout);

		atwResources = new AssortmentTablesWidget(factory, resourceCanvas, "atwResources");
		atwResources.setAllTitleKey("All Resources");
		atwResources.setAssignedTitleKey("Assigned Resources");
		atwResources.show();
		ITableColumn colAllResources = factory.createTableColumn(atwResources.getAllTable(), "Resourcename", P_RESOURCE_NAME, true);
		colAllResources.setMandatory(true);

		ITableColumn colAssignedResources = factory.createTableColumn(atwResources.getAssignedTable(), "Resourcename",
				P_RESOURCE_NAME, true);
		colAssignedResources.setMandatory(true);
		fillTable();
	}

	private void fillTable() {
		// Daten aus der Datenbank holen zum füllen
		// Erst nach User suchen... dann seine PCs anzeigen lassen,
		//
		IDataBag bag = compound.getBag();
		UserQuery query = new UserQuery(PersistenceContext.getPersistenceManager());

		Collection<IUser> allUser = query.execute();
		List<IDataBag> tableData = new ArrayList<IDataBag>();
		for (IUser currentUser : allUser) {
			bag = new DataBag(currentUser);
			tableData.add(bag);
		}

		table.getDefaultModel().setTableData(tableData);

		table.reload();
	}

	private class UserTableListener extends AbstractTableListener {

		private static final long serialVersionUID = 1L;

		@Override
		public void onRowSelection(ITable table, int row) {
			IDataBag bag = table.getRow(row);
			IUser user = (IUser)bag.getDelegate();

			atwResources.getAddButton().setActionListener(
					new ResourceAssortmentTablesListener(AssortmentTablesWidget.ASSORTMENT_ACTION_ADD, atwResources, user));
			atwResources.getRemoveButton().setActionListener(
					new ResourceAssortmentTablesListener(AssortmentTablesWidget.ASSORTMENT_ACTION_REMOVE, atwResources, user));

			Collection<IResource> allResources = new AllResourcesQuery(PersistenceContext.getPersistenceManager()).execute();
			List<IDataBag> allResourcesWrapped = new LinkedList<IDataBag>();
			for (IResource currentResource : allResources) {
				// search if user has already any permission on the resource
				boolean resourceFound = false;
				for (IUserResourcePermission currentUserResourcePermission : user.getUserResourcePermissions()) {
					if (currentUserResourcePermission.getResource().equals(currentResource)) {
						resourceFound = true;
					}
				}
				// if not then add the resource to the list of available resources
				if (!resourceFound) {
					IDataBag resBag = new DataBag(currentResource);
					resBag.addProperty(P_RESOURCE_NAME, currentResource.getResourceName());
					allResourcesWrapped.add(resBag);
				}
			}
			prepareRoleList(allResourcesWrapped);
			atwResources.getAllTable().getDefaultModel().setTableData(allResourcesWrapped);
			atwResources.getAllTable().reload();

			List<IDataBag> ownResourcesWrapped = new LinkedList<IDataBag>();
			for (IUserResourcePermission currentUserResourcePermission : user.getUserResourcePermissions()) {
				IDataBag resPermBag = new DataBag(currentUserResourcePermission);
				resPermBag.addProperty(P_RESOURCE_NAME, currentUserResourcePermission.getResource().getResourceName());
				ownResourcesWrapped.add(resPermBag);
			}
			atwResources.getAssignedTable().getDefaultModel().setTableData(ownResourcesWrapped);
			atwResources.getAssignedTable().reload();
		}

		private void prepareRoleList(List<IDataBag> appBags) {
			for (IDataBag bag : appBags) {
				bag.setObject("checked", Boolean.valueOf(false));
			}
		}
	}
}
