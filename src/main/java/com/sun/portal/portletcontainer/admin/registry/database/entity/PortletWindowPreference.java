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
@Table(name="PORTLET_PREFERENCE",
	   uniqueConstraints={@UniqueConstraint(columnNames={"NAME", "USERNAME"})}
      )
public class PortletWindowPreference implements Serializable {
	private static final long serialVersionUID = 2463149022830645283L;
	
	private Long id;
	private String name;
	private String portletName;
	private String remote;
	private String lang;
	private String userName;
	private String version;
	private Set<PortletWindowPreferencePropertyCollection> pwPreferencePropertyCollections = new HashSet<PortletWindowPreferencePropertyCollection>();;
	
	public PortletWindowPreference() {
	}
	
	@Id
	@TableGenerator(name="PortletWindowPreferenceID", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.TABLE, generator="PortletWindowPreferenceID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	@OneToMany(mappedBy="portletWindowPreference", cascade=CascadeType.ALL)
	public Set<PortletWindowPreferencePropertyCollection> getPwPreferencePropertyCollections() {
		return pwPreferencePropertyCollections;
	}

	public void setPwPreferencePropertyCollections(
			Set<PortletWindowPreferencePropertyCollection> pwPreferencePropertyCollections) {
		this.pwPreferencePropertyCollections = pwPreferencePropertyCollections;
	}

	public String getRemote() {
		return remote;
	}

	public void setRemote(String remote) {
		this.remote = remote;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}	
}
