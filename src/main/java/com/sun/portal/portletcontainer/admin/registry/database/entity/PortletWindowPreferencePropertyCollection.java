package com.sun.portal.portletcontainer.admin.registry.database.entity;

import java.io.Serializable;

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
@Table(name="PORTLET_PREFERENCE_PROPERTYCOL",
       uniqueConstraints={@UniqueConstraint(columnNames={"NAME", "ELEMENTNAME", "PORTLET_WINDOW_PREFERENCE_ID"})}
      )
public class PortletWindowPreferencePropertyCollection implements Serializable {
	private static final long serialVersionUID = -6950659142568027996L;

	private Long id;
	private String name;
	private String elementName;
	private String elementValue;
	private PortletWindowPreference portletWindowPreference;
	
	public PortletWindowPreferencePropertyCollection() {
	}

	@Id
	@TableGenerator(name="PortletWindowPreferencePropertyCollectionID", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.TABLE, generator="PortletWindowPreferencePropertyCollectionID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getElementName() {
		return elementName;
	}

	public void setElementName(String elementName) {
		this.elementName = elementName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getElementValue() {
		return elementValue;
	}

	public void setElementValue(String elementValue) {
		this.elementValue = elementValue;
	}	
	
	@ManyToOne
	@JoinColumn(name="PORTLET_WINDOW_PREFERENCE_ID", nullable=false)	
	public PortletWindowPreference getPortletWindowPreference() {
		return portletWindowPreference;
	}

	public void setPortletWindowPreference(
			PortletWindowPreference portletWindowPreference) {
		this.portletWindowPreference = portletWindowPreference;
	}
}
