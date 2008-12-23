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
@Table(name = "PORTLET_WINDOW")
public class PortletWindow implements Serializable {
	private static final long serialVersionUID = 3970508135775283028L;
	
	private Long id;
	private String name;
	private String lang;
	private String portletName;
	private String remote;
	private String version;
	private String userName;
	private Set<PortletWindowPropertyMeta> portletWindowPropertyMetas = new HashSet<PortletWindowPropertyMeta>();
	
	public PortletWindow() {
		remote = Boolean.FALSE.toString();
	}	

	@Id
	@TableGenerator(name="PortletWindowID", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.TABLE, generator="PortletWindowID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(nullable=false, unique=true)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	
	@Column(nullable = false)
	public String getPortletName() {
		return portletName;
	}
	public void setPortletName(String portletName) {
		this.portletName = portletName;
	}
	
	@Column(nullable = false)
	public String getRemote() {
		return remote;
	}
	public void setRemote(String remote) {
		this.remote = remote;
	}	

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@OneToMany(mappedBy = "portletWindow", cascade=CascadeType.ALL)
	public Set<PortletWindowPropertyMeta> getPortletWindowPropertyMetas() {
		return portletWindowPropertyMetas;
	}

	public void setPortletWindowPropertyMetas(
			Set<PortletWindowPropertyMeta> portletWindowPropertyMetas) {
		this.portletWindowPropertyMetas = portletWindowPropertyMetas;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
