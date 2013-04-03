package com.endress.infoserve.test.pca.page;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.annotations.DiscriminatorOptions;
import org.webguitoolkit.ui.base.WebGuiFactory;
import org.webguitoolkit.ui.controls.AbstractPopup;
import org.webguitoolkit.ui.controls.Page;
import org.webguitoolkit.ui.controls.container.ICanvas;
import org.webguitoolkit.ui.controls.container.IHtmlElement;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.event.IActionListener;
import org.webguitoolkit.ui.controls.form.IFormControl;
import org.webguitoolkit.ui.controls.form.ILabel;
import org.webguitoolkit.ui.controls.form.IText;
import org.webguitoolkit.ui.controls.layout.ITableLayout;
import org.webguitoolkit.ui.controls.util.TextService;
import org.webguitoolkit.ui.controls.util.validation.ValidationException;
import org.webguitoolkit.ui.controls.util.validation.ValidatorUtil;

import com.endress.infoserve.persistence.PersistenceContext;
import com.endress.infoserve.test.pca.model.IUser;
import com.endress.infoserve.test.pca.query.UserQuery;
import com.endress.infoserve.test.pca.view.UserView;

@Entity
@Table(name = "um_users")
@DiscriminatorColumn(name = "class_id", discriminatorType = DiscriminatorType.INTEGER)
@DiscriminatorOptions(force = true)
@DiscriminatorValue(value = "1000")
@AttributeOverrides(value = { @AttributeOverride(name = "id", column = @Column(name = "obj_id")) })
public class PCALogin extends org.webguitoolkit.ui.login.Login {

	private static final long serialVersionUID = 1L;
	public static final String BUTTON_ID = "LOGIN_BUTTON_ID";
	public static final String PASSWORD_ID = "LOGIN_PASSWORD_ID";
	public static final String USERNAME_ID = "LOGIN_USERNAME_ID";

	private static final Log log = LogFactory.getLog(PCALogin.class);
	private final WebGuiFactory factory = WebGuiFactory.getInstance();;
	private IText usernameText;
	private IText passwordText;

	private final String cssLink = "standard_theme.css";
	private final String favIcon = "./images/wgt/iconw.gif";
	private final String teaserImg = "./images/wgt/logo300w.gif";
	private ICanvas canvasNewUserAccount;

	@Override
	protected void pageInit() {
		// WGTFilter parameter <param-name>textservice.resource.bundle</param-name> is not considered
		// --> this will not work with not default resource bundle names
		TextService.setLocale(getServletRequest().getLocale());

		addWgtCSS(getCssLink());
		addFavicon(getFavIcon());
		IActionListener loginListener = new LoginListener();

		ITableLayout headLayout = factory.createTableLayout(this);
		headLayout.getStyle().addWidth("100%");

		factory.createLabel(headLayout, "");
		headLayout.getCurrentCell().setColSpan(2);
		headLayout.getCurrentCell().setHeight("50px");

		headLayout.newRow();

		ITableLayout frameLayout = factory.createTableLayout(headLayout);
		headLayout.getCurrentCell().setColSpan(2);
		headLayout.getCurrentCell().setAlign("center");
		headLayout.getCurrentCell().setVAlign("Top");

		frameLayout.getEcsTable().setBgColor("#FFFFFF");
		frameLayout.getEcsTable().setCellSpacing(0);
		frameLayout.getEcsTable().setCellPadding(0);
		frameLayout.getStyle().add("border-color", "#BEBEBE");

		ITableLayout formLayout = factory.createTableLayout(frameLayout);
		frameLayout.getCurrentCell().setStyle("padding-top:10px; padding-left:10px; border-color:#FFFFFF");
		frameLayout.getCurrentCell().setAlign("left");
		frameLayout.getCurrentCell().setVAlign("top");
		frameLayout.getCurrentCell().setBgColor("#E9E9E9");

		formLayout.getEcsTable().setBorder(0);
		formLayout.getEcsTable().setWidth("301");
		formLayout.getEcsTable().setHeight("100");
		formLayout.getEcsTable().setAlign("left");
		formLayout.getEcsTable().setCellSpacing(0);

		ILabel headerLabel = factory.createLabel(formLayout, "formLogin.headerText@Please enter your login information!");
		headerLabel.getStyle().add("font-size", "12pt");
		headerLabel.getStyle().add("color", "#0099FF");
		headerLabel.getStyle().add("font-weight", "bold");
		formLayout.getCurrentCell().setColSpan(2);
		formLayout.getCurrentCell().setVAlign("Top");
		formLayout.getCurrentCell().setWidth(300);
		formLayout.getCurrentCell().setHeight(24);
		formLayout.getCurrentCell().setStyle("padding-bottom: 30px; padding-top: 25px; vertical-align: top;");

		formLayout.newRow();

		ILabel usernameLabel = factory.createLabel(formLayout, "formLogin.username@Username");
		formLayout.getCurrentCell().setWidth(161);
		formLayout.getCurrentCell().setHeight(20);

		usernameText = factory.createText(formLayout, "j_username", usernameLabel, USERNAME_ID);
		usernameText.getStyle().addWidth("21ex");
		formLayout.getCurrentCell().setWidth(183);
		formLayout.getCurrentCell().setHeight(20);
		usernameText.setActionListener(new TextReturnListener());
		usernameText.setHTMLName("j_username");
		usernameText.addValidator(ValidatorUtil.MANDATORY_VALIDATOR);

		formLayout.newRow();

		ILabel passwordLabel = factory.createLabel(formLayout, "formLogin.password@Password");
		formLayout.getCurrentCell().setWidth(161);
		formLayout.getCurrentCell().setHeight(20);

		passwordText = factory.createText(formLayout, "j_password", passwordLabel, PASSWORD_ID);
		passwordText.getStyle().addWidth("21ex");
		formLayout.getCurrentCell().setWidth(183);
		formLayout.getCurrentCell().setHeight(20);
		passwordText.setPassword(true);
		passwordText.setActionListener(loginListener);
		passwordText.setHTMLName("j_password");
		passwordText.addValidator(ValidatorUtil.MANDATORY_VALIDATOR);

		formLayout.newRow();

		factory.createLabel(formLayout, "");
		formLayout.getCurrentCell().setColSpan(2);
		formLayout.getCurrentCell().setHeight("2");

		formLayout.newRow();
		formLayout.addEmptyCell();
		factory.createButton(formLayout, null, "Registrieren", "", new Popup(getFactory(), getPage(), "Registrieren", 900, 500), "Button");

		factory.createButton(formLayout, null, "formLogin.submit@Login", "", loginListener, BUTTON_ID);
		formLayout.getCurrentCell().setColSpan(2);

		ITableLayout imageTable = factory.createTableLayout(frameLayout);
		imageTable.getEcsTable().setBorder(0);
		imageTable.getEcsTable().setCellSpacing(0);
		imageTable.getEcsTable().setCellPadding(0);
		frameLayout.getCurrentCell().setStyle("border-color:#FFFFFF");

		IHtmlElement image = factory.createHtmlElement(imageTable, "img");
		image.setAttribute("src", getTeaserImg());
		image.setAttribute("border", "0");

		if ("false".equalsIgnoreCase(getServletRequest().getParameter("success"))) {
			sendError(TextService.getString("formLogin.loginFailed@Your login information are not correct."));
		}
		else {
			usernameText.focus();
		}
	}

	private class TextReturnListener implements IActionListener {
		private static final long serialVersionUID = 1L;

		public void onAction(ClientEvent event) {
			passwordText.focus();
		}
	}

	private class LoginListener implements IActionListener {
		private static final long serialVersionUID = 1L;

		public void onAction(ClientEvent event) {
			String errorMsg = validateFormControl(usernameText);
			errorMsg += validateFormControl(passwordText);
			if (!StringUtils.isBlank(errorMsg)) {
				sendError(errorMsg);
				return;
			}
			// nach User suchen
			UserQuery queryByName = new UserQuery(PersistenceContext.getPersistenceManager());
			String userNameSearch = usernameText.getValue();
			queryByName.setUserName(userNameSearch);
			Collection<IUser> resultUser = queryByName.execute();
			String passwordControl = passwordText.getValue();
			boolean passwordVerified = false;
			if (resultUser.isEmpty()) {
				log.warn("No User found with name = " + userNameSearch);
			}
			else {
				for (IUser currentUser : resultUser) {
					if (StringUtils.equals(currentUser.getPassword(), passwordControl)) {
						passwordVerified = true;
					}
				}
			}

			if (passwordVerified) {
				Map<String, String> par = new HashMap<String, String>();
				par.put("j_password", passwordText.getValue());
				par.put("j_username", usernameText.getValue());
				getContext().doPost("j_security_check", par, "_parent");
			}
			else {
				getPage().sendError("Login failed. Please try again.");
			}
		}

		private String validateFormControl(IFormControl fc) {
			String errorMsg = "";
			try {
				fc.validate();
			}
			catch (ValidationException e) {
				errorMsg = "<i>" + ((fc.getDescribingLabel() == null) ? "" : fc.getDescribingLabel().getText()) + "</i> "
						+ TextService.getString(e.getMessage()) + "<br>";
			}
			return errorMsg;
		}
	}

	private static class Popup extends AbstractPopup {
		private static final long serialVersionUID = 1L;

		public Popup(WebGuiFactory factory, Page page, String titel, int width, int height) {
			super(factory, page, titel, width, height);
		}

		@Override
		protected void createControls(WebGuiFactory factory, ICanvas viewConnector) {

			ITableLayout layout = getFactory().createTableLayout(viewConnector);
			// Userview aufrufen
			ICanvas viewConnector2 = getFactory().createCanvas(layout);
			UserView view = new UserView(getFactory(), viewConnector2, this);
			view.show();
		}
	}
}
