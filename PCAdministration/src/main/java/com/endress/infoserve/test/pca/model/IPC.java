package com.endress.infoserve.test.pca.model;

import com.endress.infoserve.persistence.model.IPersistable;

public interface IPC extends IPersistable {
	public static final String PROPERTY_NAME = "name";
	public static final String PROPERTY_GRAPHICS = "graphics";
	public static final String PROPERTY_KEYBOARD = "keyboard";
	public static final String PROPERTY_MOUSE = "mouse";
	public static final String PROPERTY_PROCESSOR = "processor";
	public static final String PROPERTY_NETWORKCARD = "networks";
	public static final String PROPERTY_DESCRIPTION = "description";

	public String getDescription();

	public void setDescription(String description);

	public String getMouse();

	public void setMouse(String mouse);

	public String getProcessor();

	public void setProcessor(String processor);

	public String getKeyboard();

	public void setKeyboard(String keyboard);

	public String getNetworks();

	public void setNetworks(String networks);

	public String getName();

	public void setName(String name);

	public String getGraphics();

	public void setGraphics(String graphics);

	public IResource getResource();

	public void setResource(IResource newResource);
}
