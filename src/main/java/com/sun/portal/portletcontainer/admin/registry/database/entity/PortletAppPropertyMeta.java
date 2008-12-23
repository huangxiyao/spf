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
@Table(name="PORTLET_APP_PROPERTY_META",
	   uniqueConstraints={@UniqueConstraint(columnNames={"PORTLET_APP_ID", "NAME"})}
      )
public class PortletAppPropertyMeta implements Serializable {
	private static final long serialVersionUID = -5599237596118764478L;
	
	private Long id;
	private String name;
	private String value;	
	private PortletApp portletApp;
	
	public PortletAppPropertyMeta(){}

	@Id
	@TableGenerator(name="PortletAppPropertyMetaID", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.TABLE, generator="PortletAppPropertyMetaID")
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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
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
