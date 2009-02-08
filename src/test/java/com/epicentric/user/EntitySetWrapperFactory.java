package com.epicentric.user;

import java.util.Set;

import com.epicentric.entity.EntityType;

public class EntitySetWrapperFactory {
    public static EntitySetWrapperFactory getInstance() {
        return new EntitySetWrapperFactory();
    }
    
    Set getWrappedSet(EntityType elementType, Set setToWrap) {
        return setToWrap;
    }
}
