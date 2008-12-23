package com.sun.portal.portletcontainer.admin.registry.database.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name="PORTLET_APP_PROPERTY_COL" ,
	   uniqueConstraints={@UniqueConstraint(columnNames={"PORTLET_APP_ID", "NAME", "ELEMENTNAME", "ELEMENTVALUE"})}
      )
public class PortletAppPropertyCollection implements Serializable {
	private static final long serialVersionUID = 1600035287322087852L;
	
	private Long id;
	private String name;
	private String elementName;
	private String elementValue;
	private PortletApp portletApp;
	
	public PortletAppPropertyCollection() {
	}

	@Id
	@TableGenerator(name="PortletAppPropertyCollectionID", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.TABLE, generator="PortletAppPropertyCollectionID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(nullable=false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getElementName() {
		return elementName;
	}

	public void setElementName(String elementName) {
		this.elementName = elementName;
	}

	public String getElementValue() {
		return elementValue;
	}

	public void setElementValue(String elementValue) {
		this.elementValue = elementValue;
	}

	@ManyToOne
	@JoinColumn(name="PORTLET_APP_ID", nullable=false)
	public PortletApp getPortletApp() {
		return portletApp;
	}

	public void setPortletApp(PortletApp portletApp) {
		this.portletApp = portletApp;
	}

}
