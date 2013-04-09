package com.endress.infoserve.test.pca.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.webguitoolkit.ui.base.DataBag;
import org.webguitoolkit.ui.base.IDataBag;
import org.webguitoolkit.ui.base.WebGuiFactory;
import org.webguitoolkit.ui.controls.AbstractPopup;
import org.webguitoolkit.ui.controls.AbstractView;
import org.webguitoolkit.ui.controls.container.ICanvas;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.form.AbstractButtonBarListener;
import org.webguitoolkit.ui.controls.form.Compound;
import org.webguitoolkit.ui.controls.form.DefaultSelectModel;
import org.webguitoolkit.ui.controls.form.IButtonBar;
import org.webguitoolkit.ui.controls.form.ICompound;
import org.webguitoolkit.ui.controls.form.ILabel;
import org.webguitoolkit.ui.controls.form.IRadioGroup;
import org.webguitoolkit.ui.controls.form.ISelect;
import org.webguitoolkit.ui.controls.form.IText;
import org.webguitoolkit.ui.controls.layout.ITableLayout;
import org.webguitoolkit.ui.controls.tab.ITab;
import org.webguitoolkit.ui.controls.tab.ITabStrip;
import org.webguitoolkit.ui.controls.util.validation.MinMaxLengthValidator;
import org.webguitoolkit.ui.controls.util.validation.ValidatorUtil;

import com.endress.infoserve.persistence.PersistenceContext;
import com.endress.infoserve.persistence.PersistenceException;
import com.endress.infoserve.persistence.query.NonUniqueResultException;
import com.endress.infoserve.test.pca.impl.User;
import com.endress.infoserve.test.pca.model.IRole;
import com.endress.infoserve.test.pca.model.IUser;
import com.endress.infoserve.test.pca.query.RoleQuery;
import com.endress.infoserve.test.pca.util.SessionContext;
import com.endress.infoserve.test.pca.view.validation.UniqueUsernameValidator;

public class UserView extends AbstractView {

	private static final long serialVersionUID = 1L;
	private static final Log logger = LogFactory.getLog(UserView.class);
	private static final int textsize = 64;
	private ICompound compound;
	private ITab tabUserData;
	private final boolean isInitialRegistration;
	private final AbstractPopup popup;
	private final boolean isAdminView;
	private ISelect selectRole;

	public UserView(WebGuiFactory factory, ICanvas viewConnector) {
		this(factory, viewConnector, null);
	}

	public UserView(WebGuiFactory factory, ICanvas viewConnector, AbstractPopup popup) {
		super(factory, viewConnector);
		this.isInitialRegistration = popup != null;
		this.popup = popup;
		this.isAdminView = false;
	}

	public UserView(WebGuiFactory factory, ICanvas viewConnector, boolean admin) {
		super(factory, viewConnector);
		this.isInitialRegistration = admin;
		this.isAdminView = admin;
		popup = null;
	}

	@Override
	protected void createControls(WebGuiFactory factory, ICanvas viewConnector) {
		compound = factory.createCompound(viewConnector);
		ITableLayout layout = factory.createTableLayout(compound);
		ICompound detailCompound = compound;
		if (!isAdminView) {

			if (isInitialRegistration) {
				ILabel label = factory.createLabel(layout, "Anmeldeformular");
				label.addCssClass("wgtHeadline");
			}
			else {
				ILabel label = factory.createLabel(layout, "Benutzerdaten");
				label.addCssClass("wgtHeadline");
			}

			layout.newRow();
			ITableLayout innerLayout = factory.createTableLayout(layout);
			innerLayout.newRow();
			// Tab anlegen
			ITabStrip tabStrip = factory.createTabStrip(innerLayout);
			tabUserData = factory.createTab(tabStrip, "userview.tab.userdata");

			detailCompound = factory.createCompound(tabUserData);
			detailCompound.setBag(new DataBag(""));

			String buttonBarButtons = IButtonBar.BUTTON_EDIT_CANCEL_SAVE;
			if (isInitialRegistration) {
				buttonBarButtons = "new,cancel,save";
			}
			factory.createButtonBar(detailCompound, buttonBarButtons, new ButtonBarListener(detailCompound));
		}
		/*
		 * Username Passwort Email, Vorname, und Nachname anlegen.
		 */
		ITableLayout tabLayout = factory.createTableLayout(detailCompound);
		ILabel labelUsername = factory.createLabel(tabLayout, "Username");
		IText userText = factory.createText(tabLayout, "name", labelUsername);
		userText.setFinal(!isInitialRegistration);

		// Da noch keine Daten vorhanden kein pflichtfeld--> sonst error
		userText.setMaxlength(textsize);
		userText.addValidator(ValidatorUtil.MANDATORY_VALIDATOR);
		userText.addValidator(new UniqueUsernameValidator("Error: Username already exist", detailCompound));
		tabLayout.newRow();

		ILabel labelPassword = factory.createLabel(tabLayout, "Passwort");
		IText passwordText = factory.createText(tabLayout, "password", labelPassword);
		passwordText.setPassword(true);
		passwordText.addValidator(ValidatorUtil.MANDATORY_VALIDATOR);
		passwordText.addValidator(new MinMaxLengthValidator(3, 50, "Password to short", "Password to long"));
		ILabel info = factory.createLabel(tabLayout, "(Min.length got to be 3)");
		info.addCssClass("helpCanvasInnerText");
		tabLayout.newRow();

		ILabel labelEmail = factory.createLabel(tabLayout, "Email");
		IText emailText = factory.createText(tabLayout, "email", labelEmail);
		emailText.addValidator(ValidatorUtil.EMAIL_VALIDATOR);
		emailText.addValidator(ValidatorUtil.MANDATORY_VALIDATOR);
		emailText.setMaxlength(textsize);
		tabLayout.newRow();

		ILabel labelFirstname = factory.createLabel(tabLayout, "Vorname");
		IText firstnameText = factory.createText(tabLayout, "firstname", labelFirstname);
		firstnameText.addValidator(ValidatorUtil.MANDATORY_VALIDATOR);
		firstnameText.setMaxlength(textsize);
		tabLayout.newRow();

		ILabel labelLastname = factory.createLabel(tabLayout, "Nachname");
		IText lastnameText = factory.createText(tabLayout, "lastname", labelLastname);
		lastnameText.addValidator(ValidatorUtil.MANDATORY_VALIDATOR);
		lastnameText.setMaxlength(textsize);
		tabLayout.newRow();

		// Radiobutton - Geschlecht

		ILabel labelGender = factory.createLabel(tabLayout, "Geschlecht");
		labelGender.addCssClass("wgtLabelFor");
		IRadioGroup rg = factory.createRadioGroup(tabLayout, detailCompound, "gender");
		List<String[]> radioGroupData = new ArrayList<String[]>();
		radioGroupData.add(new String[] { "0", "Männlich" });
		radioGroupData.add(new String[] { "1", "Weiblich" });
		rg.loadList(radioGroupData);
		rg.setValue("0");
		tabLayout.newRow();

		// Selectbox Country
		ILabel labelCountry = factory.createLabel(tabLayout, "Land");
		ISelect select = factory.createSelect(tabLayout, "country");
		selectBoxFill(select);
		select.setDescribingLabel(labelCountry);
		tabLayout.newRow();

		// Adresse Wohnort und PLZ

		ILabel labelAdress = factory.createLabel(tabLayout, "Adresse");
		IText adressText = factory.createText(tabLayout, "adress", labelAdress);
		adressText.addValidator(ValidatorUtil.MANDATORY_VALIDATOR);
		adressText.setMaxlength(textsize);
		tabLayout.newRow();

		ILabel labelLocation = factory.createLabel(tabLayout, "Wohnort");
		IText locationText = factory.createText(tabLayout, "location", labelLocation);
		locationText.addValidator(ValidatorUtil.MANDATORY_VALIDATOR);
		locationText.setMaxlength(textsize);

		final ILabel labelPLZ = factory.createLabel(tabLayout, "PLZ");
		final IText plzText = factory.createText(tabLayout, "plz", labelPLZ);
		plzText.addValidator(ValidatorUtil.MANDATORY_VALIDATOR);
		plzText.setMaxlength(textsize);
		tabLayout.newRow();

		// Beschreibung (große Textarea)

		ILabel labelDescription = factory.createLabel(tabLayout, "Beschreibung");
		factory.createTextarea(tabLayout, "description").setDescribingLabel(labelDescription);
		tabLayout.newRow();
		tabLayout.addEmptyCell();
		if (!isInitialRegistration) {
			fillView(detailCompound);
		}
		else {
			selectRole = factory.createSelect(tabLayout, "role");
			Collection<String[]> roleCollection = new ArrayList<String[]>();
			roleCollection.add(new String[] { "0", IRole.ROLENAME_ADMIN });
			roleCollection.add(new String[] { "1", IRole.ROLENAME_STANDARD });
			selectRole.loadList(roleCollection);
		}
	}

	// View ausfüllen, funktioniert
	private void fillView(ICompound compound) {
		IUser loggedInUser = SessionContext.getInstance().getLoggedInUser();
		IDataBag bag = new DataBag(loggedInUser);
		compound.setBag(bag);
		compound.load();
	}

	// Selexctbox füllen
	private void selectBoxFill(ISelect select) {
		List<String[]> selectBoxData = new ArrayList<String[]>();
		selectBoxData.add(new String[] { "0", "Deutschland" });
		selectBoxData.add(new String[] { "1", "England" });
		selectBoxData.add(new String[] { "2", "USA" });
		selectBoxData.add(new String[] { "3", "Österreich" });
		selectBoxData.add(new String[] { "4", "Frankreich" });

		DefaultSelectModel selectBoxModel = new DefaultSelectModel();
		selectBoxModel.setOptions(selectBoxData);
		select.setModel(selectBoxModel);
		select.loadList();
	}

	class ButtonBarListener extends AbstractButtonBarListener {
		private static final long serialVersionUID = 1L;

		public ButtonBarListener(ICompound compound) {
			super(compound);

		}

		@Override
		public void onCancel(ClientEvent event) {
			if (isInitialRegistration) {
				hidePopup();
			}
			else {
				// fillView(compound);
				compound.changeElementMode(Compound.MODE_READONLY);
			}
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
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Object newDelegate() {
			IDataBag bag = compound.getBag();
			User newUser = new User(bag.getString("name"));
			// Role zuweisen
			String role = selectRole.getValue();
			if ("0".equals(role)) {
				role = IRole.ROLENAME_ADMIN;
			}
			else {
				role = IRole.ROLENAME_STANDARD;
			}
			try {
				RoleQuery query = new RoleQuery(PersistenceContext.getPersistenceManager(), role);
				IRole foundRole = query.execute();
				if (foundRole != null) {
					newUser.addRole(foundRole);
				}
			}
			catch (NonUniqueResultException e) {
				logger.error("Could not load role from database", e);
			}
			return newUser;
		}

		@Override
		public void postSave() {
			hidePopup();

		}

		private void hidePopup() {
			if (isInitialRegistration) {
				popup.getDialog().getWindow().setVisible(false);
			}
		}
	}
}