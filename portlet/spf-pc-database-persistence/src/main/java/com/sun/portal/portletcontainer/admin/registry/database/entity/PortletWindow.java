package com.sun.portal.portletcontainer.admin.registry.database.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name="PORTLET_WINDOW")
public class PortletWindow implements Serializable {
	private static final long serialVersionUID = 3970508135775283028L;
	
	private Long id;
	private String name;
	private String lang;
	private String portletName;
	private String remote;
	
	private String width;	
	private String title;
	private String visible;
	private String entityIDPrefix;
	private String portletHandle;
	private String consumerId;
	private String producerEntityID;
	private String portletID;
	
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

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getVisible() {
		return visible;
	}

	public void setVisible(String visible) {
		this.visible = visible;
	}

	public String getEntityIDPrefix() {
		return entityIDPrefix;
	}

	public void setEntityIDPrefix(String entityIDPrefix) {
		this.entityIDPrefix = entityIDPrefix;
	}

	public String getPortletHandle() {
		return portletHandle;
	}

	public void setPortletHandle(String portletHandle) {
		this.portletHandle = portletHandle;
	}

	public String getConsumerId() {
		return consumerId;
	}

	public void setConsumerId(String consumerId) {
		this.consumerId = consumerId;
	}

	public String getProducerEntityID() {
		return producerEntityID;
	}

	public void setProducerEntityID(String producerEntityID) {
		this.producerEntityID = producerEntityID;
	}

	public String getPortletID() {
		return portletID;
	}

	public void setPortletID(String portletID) {
		this.portletID = portletID;
	}	
}
