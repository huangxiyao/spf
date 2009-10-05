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
@Table(name="PORTLET_USER_WINDOW_PREFERENCE",
       uniqueConstraints={@UniqueConstraint(columnNames={"TYPE", "PREFERENCENAME", "PORTLET_USER_WINDOW_ID"})}
      )
public class PortletUserWindowPreference implements Serializable {
	private static final long serialVersionUID = -6950659142568027996L;

	private Long id;
	private String type;
	private String preferenceName;
	private String preferenceValue;
	private PortletUserWindow portletUserWindow;
	
	public PortletUserWindowPreference() {
	}

	@Id
	@TableGenerator(name="PortletUserWindowPreferenceID", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.TABLE, generator="PortletUserWindowPreferenceID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getPreferenceName() {
		return preferenceName;
	}

	public void setPreferenceName(String preferenceName) {
		this.preferenceName = preferenceName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPreferenceValue() {
		return preferenceValue;
	}

	public void setPreferenceValue(String preferenceValue) {
		this.preferenceValue = preferenceValue;
	}	
	
	@ManyToOne
	@JoinColumn(name="PORTLET_USER_WINDOW_ID", nullable=false)	
	public PortletUserWindow getPortletUserWindow() {
		return portletUserWindow;
	}

	public void setPortletUserWindow(
			PortletUserWindow portletUserWindow) {
		this.portletUserWindow = portletUserWindow;
	}
}
