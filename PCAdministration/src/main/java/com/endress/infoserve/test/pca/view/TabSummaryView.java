package com.endress.infoserve.test.pca.view;

import org.webguitoolkit.ui.base.WebGuiFactory;
import org.webguitoolkit.ui.controls.AbstractView;
import org.webguitoolkit.ui.controls.container.ICanvas;
import org.webguitoolkit.ui.controls.form.ILabel;
import org.webguitoolkit.ui.controls.form.IText;
import org.webguitoolkit.ui.controls.layout.ITableLayout;
import org.webguitoolkit.ui.controls.util.validation.ValidatorUtil;

import com.endress.infoserve.test.pca.impl.PC;

public class TabSummaryView extends AbstractView {

	private static final long serialVersionUID = 1L;
	private static final int textsize = 25;

	public TabSummaryView(WebGuiFactory factory, ICanvas viewConnector) {
		super(factory, viewConnector);
	}

	@Override
	protected void createControls(WebGuiFactory factory, ICanvas viewConnector) {
		ITableLayout layout = factory.createTableLayout(viewConnector);

		ILabel lblName = factory.createLabel(layout, "pcview.tab.summary.name");
		IText txtName = factory.createText(layout, PC.PROPERTY_NAME);
		txtName.setDescribingLabel(lblName);
		txtName.addValidator(ValidatorUtil.MANDATORY_VALIDATOR);
		txtName.setMaxlength(textsize);
		layout.newRow();
		ILabel graphicsName = factory.createLabel(layout, "pcview.tab.summary.graphics");
		IText graphicsText = factory.createText(layout, PC.PROPERTY_GRAPHICS, graphicsName);
		graphicsText.setMaxlength(textsize);
		layout.newRow();
		ILabel keyboardName = factory.createLabel(layout, "pcview.tab.summary.keyboard");
		IText keyboardText = factory.createText(layout, PC.PROPERTY_KEYBOARD, keyboardName);
		keyboardText.setMaxlength(textsize);
		layout.newRow();
		ILabel mouseName = factory.createLabel(layout, "pcview.tab.summary.mouse");
		IText mouseText = factory.createText(layout, PC.PROPERTY_MOUSE, mouseName);
		mouseText.setMaxlength(textsize);
		layout.newRow();

		ILabel networkName = factory.createLabel(layout, "pcview.tab.summary.network");
		IText networkText = factory.createText(layout, PC.PROPERTY_NETWORKCARD, networkName);
		networkText.setMaxlength(textsize);
		layout.newRow();

		ILabel proName = factory.createLabel(layout, "pcview.tab.summary.processor");
		IText proText = factory.createText(layout, PC.PROPERTY_PROCESSOR, proName);
		proText.setMaxlength(textsize);
		layout.newRow();

	}
}
