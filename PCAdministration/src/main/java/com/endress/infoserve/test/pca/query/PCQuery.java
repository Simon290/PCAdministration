/*
 * Created on 24.09.2004
 * 
 * Copyright 2004-2009, Endress&Hauser InfoServe
 */
package com.endress.infoserve.test.pca.query;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.endress.infoserve.persistence.PersistenceManager;
import com.endress.infoserve.persistence.model.IPersistable;
import com.endress.infoserve.persistence.query.hibernate.BaseHibernateQuery;
import com.endress.infoserve.test.pca.impl.PC;
import com.endress.infoserve.test.pca.model.IPC;

public class PCQuery extends BaseHibernateQuery<IPersistable> {

	private String pcName;
	private Long pcOid;
	private String pcGraphics;
	private String pcKeyboard;
	private String pcMouse;
	private String pcProcessor;
	private String pcNetworkcard;
	private String pcDescription;

	public PCQuery(PersistenceManager pm) {
		super(pm);
	}

	public List<IPC> execute() {
		Criteria criteria = getDBSession().createCriteria(getSearchClass());

		if (pcOid != null) {
			criteria.add(Restrictions.eq("OBJ_ID", pcOid));
		}
		if (pcName != null) {
			criteria.add(Restrictions.eq("name", pcName));
		}
		if (pcGraphics != null) {
			criteria.add(Restrictions.eq("graphics", pcGraphics));
		}
		if (pcKeyboard != null) {
			criteria.add(Restrictions.eq("keyboard", pcKeyboard));
		}
		if (pcMouse != null) {
			criteria.add(Restrictions.eq("mouse", pcMouse));
		}
		if (pcProcessor != null) {
			criteria.add(Restrictions.eq("processor", pcProcessor));
		}
		if (pcNetworkcard != null) {
			criteria.add(Restrictions.eq("networks", pcNetworkcard));
		}
		if (pcDescription != null) {
			criteria.add(Restrictions.eq("description", pcDescription));
		}

		return criteria.list();
	}

	@Override
	protected Class<? extends IPersistable> getSearchClass() {
		return PC.class;
	}

	/**
	 * @return the pcName
	 */
	public String getPcName() {
		return pcName;
	}

	/**
	 * @param pcName the pcName to set
	 */
	public void setPcName(String pcName) {
		this.pcName = pcName;
	}

	/**
	 * @return the pcOid
	 */
	public Long getPcOid() {
		return pcOid;
	}

	/**
	 * @param pcOid the pcOid to set
	 */
	public void setPcOid(Long pcOid) {
		this.pcOid = pcOid;
	}

	/**
	 * @return the pcGraphics
	 */
	public String getPcGraphics() {
		return pcGraphics;
	}

	/**
	 * @param pcGraphics the pcGraphics to set
	 */
	public void setPcGraphics(String pcGraphics) {
		this.pcGraphics = pcGraphics;
	}

	/**
	 * @return the pcKeyboard
	 */
	public String getPcKeyboard() {
		return pcKeyboard;
	}

	/**
	 * @param pcKeyboard the pcKeyboard to set
	 */
	public void setPcKeyboard(String pcKeyboard) {
		this.pcKeyboard = pcKeyboard;
	}

	/**
	 * @return the pcMouse
	 */
	public String getPcMouse() {
		return pcMouse;
	}

	/**
	 * @param pcMouse the pcMouse to set
	 */
	public void setPcMouse(String pcMouse) {
		this.pcMouse = pcMouse;
	}

	/**
	 * @return the pcProccesor
	 */
	public String getPcProcessor() {
		return pcProcessor;
	}

	/**
	 * @param pcProccesor the pcProccesor to set
	 */
	public void setPcProcessor(String pcProccesor) {
		this.pcProcessor = pcProccesor;
	}

	/**
	 * @return the pcNetworkcard
	 */
	public String getPcNetworkcard() {
		return pcNetworkcard;
	}

	/**
	 * @param pcNetworkcard the pcNetworkcard to set
	 */
	public void setPcNetworkcard(String pcNetworkcard) {
		this.pcNetworkcard = pcNetworkcard;
	}

	/**
	 * @return the pcDescription
	 */
	public String getPcDescription() {
		return pcDescription;
	}

	/**
	 * @param pcDescription the pcDescription to set
	 */
	public void setPcDescription(String pcDescription) {
		this.pcDescription = pcDescription;
	}

}
