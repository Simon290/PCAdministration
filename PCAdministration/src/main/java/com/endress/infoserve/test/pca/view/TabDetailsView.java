package com.endress.infoserve.test.pca.view;

import org.webguitoolkit.ui.base.WebGuiFactory;
import org.webguitoolkit.ui.controls.AbstractView;
import org.webguitoolkit.ui.controls.container.ICanvas;
import org.webguitoolkit.ui.controls.form.ILabel;
import org.webguitoolkit.ui.controls.form.ITextarea;
import org.webguitoolkit.ui.controls.layout.ITableLayout;

import com.endress.infoserve.test.pca.impl.PC;

public class TabDetailsView extends AbstractView {

	private static final long serialVersionUID = 1L;

	public TabDetailsView(WebGuiFactory factory, ICanvas viewConnector) {
		super(factory, viewConnector);
	}

	@Override
	protected void createControls(WebGuiFactory factory, ICanvas viewConnector) {
		ITableLayout layout = factory.createTableLayout(viewConnector);

		ILabel lblName = factory.createLabel(layout, "pcview.tab.details.description");
		layout.getCurrentCell().setVAlign("top");
		ITextarea txtName = factory.createTextarea(layout, PC.PROPERTY_DESCRIPTION);
		txtName.setDescribingLabel(lblName);
		layout.newRow();
	}
}
