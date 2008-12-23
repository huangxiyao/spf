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

@Entity
@Table(name="PORTLET_APP")
public class PortletApp implements Serializable {
	private static final long serialVersionUID = -6019625875206835801L;
	
	private Long id;
	private String name;
	private String portletName;
	private String userName;
	private String remote;
    private String lang;
	private String version;
	private Set<PortletAppPropertyMeta> portletAppPropertyMetas = new HashSet<PortletAppPropertyMeta>();
	private Set<PortletAppPropertyCollection> portletAppPropertyCollections = new HashSet<PortletAppPropertyCollection>();
	
	public PortletApp(){	
		 remote = Boolean.FALSE.toString();
	}

	@Id
	@TableGenerator(name="PortletAppID", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.TABLE, generator="PortletAppID")
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

	@Column(nullable=false, unique=true)
	public String getPortletName() {
		return portletName;
	}

	public void setPortletName(String portletName) {
		this.portletName = portletName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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
	
	@OneToMany(mappedBy="portletApp", cascade=CascadeType.ALL)
	public Set<PortletAppPropertyMeta> getPortletAppPropertyMetas() {
		return portletAppPropertyMetas;
	}

	public void setPortletAppPropertyMetas(
			Set<PortletAppPropertyMeta> portletAppPropertyMetas) {
		this.portletAppPropertyMetas = portletAppPropertyMetas;
	}

	@OneToMany(mappedBy="portletApp", cascade=CascadeType.ALL)
	public Set<PortletAppPropertyCollection> getPortletAppPropertyCollections() {
		return portletAppPropertyCollections;
	}

	public void setPortletAppPropertyCollections(
			Set<PortletAppPropertyCollection> portletAppPropertyCollections) {
		this.portletAppPropertyCollections = portletAppPropertyCollections;
	}
}
