package com.endress.infoserve.test.pca.impl;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DiscriminatorOptions;

import com.endress.infoserve.persistence.model.Persistable;
import com.endress.infoserve.test.pca.model.IPC;
import com.endress.infoserve.test.pca.model.IResource;

@Entity
@Table(name = "pcs")
@DiscriminatorColumn(name = "class_id", discriminatorType = DiscriminatorType.INTEGER)
@DiscriminatorOptions(force = true)
@DiscriminatorValue(value = "1001")
@AttributeOverrides(value = { @AttributeOverride(name = "id", column = @Column(name = "obj_id")) })
public class PC extends Persistable implements IPC {

	@Column(nullable = false)
	private String name;
	@Column
	private String graphics;
	@Column
	private String keyboard;
	@Column
	private String mouse;
	@Column
	private String processor;
	@Column
	private String networks;
	@Column
	private String description;

	/**
	 * 
	 */
	@ManyToOne(targetEntity = Resource.class, cascade = CascadeType.ALL)
	private IResource resource;

	protected PC() {
		// default constructor required by PFW/Hibernate
		super();
	}

	// Standard Konstruktor der aufgerufen wird um später die einzelnen sachen zu editieren
	public PC(String newName) {
		this();
		setName(newName);
	}

	public PC(String newName, String newGraphics, String newKeyboard, String newMouse, String newProcessor, String newDetails) {
		setName(newName);
		setGraphics(newGraphics);
		setKeyboard(newKeyboard);
		setMouse(newMouse);
		setProcessor(newProcessor);
		setDescription(newDetails);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMouse() {
		return mouse;
	}

	public void setMouse(String mouse) {
		this.mouse = mouse;
	}

	public String getProcessor() {
		return processor;
	}

	public void setProcessor(String processor) {
		this.processor = processor;
	}

	public String getKeyboard() {
		return keyboard;
	}

	public void setKeyboard(String keyboard) {
		this.keyboard = keyboard;
	}

	public String getNetworks() {
		return networks;
	}

	public void setNetworks(String networks) {
		this.networks = networks;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGraphics() {
		return graphics;
	}

	public void setGraphics(String graphics) {
		this.graphics = graphics;
	}

	@Override
	public void markDeleted() {
		if (getResource() != null) {
			getResource().markDeleted();
		}
		super.markDeleted();
	}

	/**
	 * @return the resource
	 */
	public IResource getResource() {
		return resource;
	}

	/**
	 * @param newResource the resource to set
	 */
	public void setResource(IResource newResource) {
		this.resource = newResource;
	}
}
