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
@Table(name="PORTLET_APP_PROPERTIES" ,
	   uniqueConstraints={@UniqueConstraint(columnNames={"PORTLET_APP_ID", "PROPERTYNAME", "SUBELEMENTNAME", "SUBELEMENTVALUE"})}
      )
public class PortletAppProperties implements Serializable {
	private static final long serialVersionUID = 1600035287322087852L;
	
	private Long id;
	private String propertyName;
	private String subElementName;
	private String subElementValue;
	private PortletApp portletApp;
	
	public PortletAppProperties() {
	}

	@Id
	@TableGenerator(name="PortletAppPropertiesID", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.TABLE, generator="PortletAppPropertiesID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}	
	
	@Column(nullable=false)
	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	@Column(nullable=false)
	public String getSubElementName() {
		return subElementName;
	}

	public void setSubElementName(String subElementName) {
		this.subElementName = subElementName;
	}

	public String getSubElementValue() {
		return subElementValue;
	}

	public void setSubElementValue(String subElementValue) {
		this.subElementValue = subElementValue;
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
