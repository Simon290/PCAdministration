package com.endress.infoserve.test.pca.page;

import org.webguitoolkit.ui.controls.Page;
import org.webguitoolkit.ui.controls.container.ICanvas;
import org.webguitoolkit.ui.controls.event.ClientEvent;
import org.webguitoolkit.ui.controls.event.IActionListener;
import org.webguitoolkit.ui.controls.layout.ITableLayout;
import org.webguitoolkit.ui.controls.menu.IMenuBar;
import org.webguitoolkit.ui.controls.util.TextService;

import com.endress.infoserve.security.SecurityCheckEngine;
import com.endress.infoserve.test.pca.util.SessionContext;
import com.endress.infoserve.test.pca.view.AdminView;
import com.endress.infoserve.test.pca.view.PCView;
import com.endress.infoserve.test.pca.view.UserView;

public class Index extends Page {

	private static final long serialVersionUID = 1L;

	@Override
	protected void pageInit() {
		addWgtCSS("standard_theme.css");
		addHeaderCSS("styles/pca.css");

		page.getStyle().addStyleAttributes("width:100%;margin:0px;padding:0px;border:0;");

		ITableLayout layout = getFactory().createTableLayout(getPage());

		IMenuBar menu = getFactory().createMenuBar(layout);

		layout.newRow();
		ICanvas viewConnector = getFactory().createCanvas(layout);
		getFactory().createMenuItem(menu, TextService.getString("menu.pc"), new PCView(getFactory(), viewConnector));
		getFactory().createMenuItem(menu, TextService.getString("menu.user"), new UserView(getFactory(), viewConnector));

		boolean hasPermission = new SecurityCheckEngine().hasPermission(SecurityCheckEngine.APP_CONTEXT_MENUBAR,
				SessionContext.getInstance().getLoggedInUser(), null);

		if (hasPermission) {
			getFactory().createMenuItem(menu, TextService.getString("menu.admin"), new AdminView(getFactory(), viewConnector));
		}

		getFactory().createMenuItem(menu, TextService.getString("general.logout@Logout"), new LogoutListener());
		PCView view = new PCView(getFactory(), viewConnector);
		view.show();
	}

	@Override
	protected String title() {
		return TextService.getString("pca.title.main");
	}

	private class LogoutListener implements IActionListener {
		private static final long serialVersionUID = 1L;

		public void onAction(ClientEvent event) {
			gotoApplicationPage("/logout.jsp?logout=true");
		}

	}
}