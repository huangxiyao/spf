package com.hp.it.cas.persona.webservice.client;

import com.hp.it.cas.persona.uav.service.IUserAttribute;
import com.hp.it.cas.persona.uav.service.IUserAttributeValue;
import com.hp.it.cas.persona.uav.service.IUserIdentifier;

class UserAttributeValue implements IUserAttributeValue {

    private final IUserIdentifier userIdentifier;
    private final IUserAttribute userAttribute;
    private final String instanceIdentifier;
    private final String value;
    
    UserAttributeValue(IUserIdentifier userIdentifier, IUserAttribute userAttribute, String instanceIdentifier, String value) {
        this.userIdentifier = userIdentifier;
        this.userAttribute = userAttribute;
        this.instanceIdentifier = instanceIdentifier;
        this.value = value;
    }

    public IUserIdentifier getUserIdentifier() {
        return userIdentifier;
    }

    public IUserAttribute getUserAttribute() {
        return userAttribute;
    }

    public String getInstanceIdentifier() {
        return instanceIdentifier;
    }

    public String getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((instanceIdentifier == null) ? 0 : instanceIdentifier.hashCode());
        result = prime * result + ((userAttribute == null) ? 0 : userAttribute.hashCode());
        result = prime * result + ((userIdentifier == null) ? 0 : userIdentifier.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserAttributeValue other = (UserAttributeValue) obj;
        if (instanceIdentifier == null) {
            if (other.instanceIdentifier != null)
                return false;
        } else if (!instanceIdentifier.equals(other.instanceIdentifier))
            return false;
        if (userAttribute == null) {
            if (other.userAttribute != null)
                return false;
        } else if (!userAttribute.equals(other.userAttribute))
            return false;
        if (userIdentifier == null) {
            if (other.userIdentifier != null)
                return false;
        } else if (!userIdentifier.equals(other.userIdentifier))
            return false;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }
    
}
