package com.sun.portal.portletcontainer.admin.registry.database.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name="PORTLET_DEPLOYMENT_DESCRIPTOR")
public class PortletDeploymentDescriptor implements Serializable {
	private static final long serialVersionUID = 4822447175343994374L;
	
	private String descriptorName;
	private byte[] content;
	
	@Id
	public String getDescriptorName() {
		return descriptorName;
	}
	public void setDescriptorName(String descriptorName) {
		this.descriptorName = descriptorName;
	}
	
	@Lob
	public byte[] getContent() {
		return content;
	}
	public void setContent(byte[] content) {
		this.content = content;
	}
	
}
