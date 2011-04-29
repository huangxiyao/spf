package com.sun.portal.portletcontainer.admin.registry.database.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name="PORTLET_USER_WINDOW",
	   uniqueConstraints={@UniqueConstraint(columnNames={"WINDOWNAME", "USERNAME"})}
      )
public class PortletUserWindow implements Serializable {
	private static final long serialVersionUID = 2463149022830645283L;
	
	private Long id;
	private String windowName;
	private String portletName;
	private String userName;
	private Set<PortletUserWindowPreference> portletUserWindowPreference = new HashSet<PortletUserWindowPreference>();;
	
	public PortletUserWindow() {
	}
	
	@Id
	@TableGenerator(name="PortletUserWindowID", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.TABLE, generator="PortletUserWindowID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(nullable = false)
	public String getWindowName() {
		return windowName;
	}

	public void setWindowName(String windowName) {
		this.windowName = windowName;
	}
	
	@Column(nullable = false)
	public String getPortletName() {
		return portletName;
	}

	public void setPortletName(String portletName) {
		this.portletName = portletName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@OneToMany(mappedBy="portletUserWindow", cascade=CascadeType.ALL)
	public Set<PortletUserWindowPreference> getPortletWindowPreference() {
		return portletUserWindowPreference;
	}

	public void setPortletWindowPreference(
			Set<PortletUserWindowPreference> portletUserWindowPreference) {
		this.portletUserWindowPreference = portletUserWindowPreference;
	}
}
