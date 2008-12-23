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
@Table(name = "PORTLET_WINDOW_PROPERTY_META",
	   uniqueConstraints={@UniqueConstraint(columnNames={"PORTLET_WINDOW_ID", "NAME"})}
      )
public class PortletWindowPropertyMeta implements Serializable {
	private static final long serialVersionUID = -5423143741881064128L;

	private Long id;
	private String name;
	private String value;
	private PortletWindow portletWindow;
	
	public PortletWindowPropertyMeta() {
	}

	@Id
	@TableGenerator(name="PortletWindowPropertyMetaID", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.TABLE, generator="PortletWindowPropertyMetaID")
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
	@JoinColumn(name="PORTLET_WINDOW_ID", nullable=false)
	public PortletWindow getPortletWindow() {
		return portletWindow;
	}

	public void setPortletWindow(PortletWindow portletWindow) {
		this.portletWindow = portletWindow;
	}	
}
