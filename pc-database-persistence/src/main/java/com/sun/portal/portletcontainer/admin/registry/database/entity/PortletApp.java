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
	
	private String title;
	private String archiveName;
	private String archiveType;
	private String description;
	private String shortTitle;
	private String transportGuarantee;
	
	private Set<PortletAppProperties> portletAppProperties = new HashSet<PortletAppProperties>();
	
	public PortletApp(){		
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getArchiveName() {
		return archiveName;
	}

	public void setArchiveName(String archiveName) {
		this.archiveName = archiveName;
	}

	public String getArchiveType() {
		return archiveType;
	}

	public void setArchiveType(String archiveType) {
		this.archiveType = archiveType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getShortTitle() {
		return shortTitle;
	}

	public void setShortTitle(String shortTitle) {
		this.shortTitle = shortTitle;
	}

	public String getTransportGuarantee() {
		return transportGuarantee;
	}

	public void setTransportGuarantee(String transportGuarantee) {
		this.transportGuarantee = transportGuarantee;
	}

	@OneToMany(mappedBy="portletApp", cascade=CascadeType.ALL)
	public Set<PortletAppProperties> getPortletAppProperties() {
		return portletAppProperties;
	}

	public void setPortletAppProperties(Set<PortletAppProperties> portletAppProperties) {
		this.portletAppProperties = portletAppProperties;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof PortletApp))
			return false;
		PortletApp other = (PortletApp) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
