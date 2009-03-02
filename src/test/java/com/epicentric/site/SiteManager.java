/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.epicentric.site;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;

import org.jmock.Mockery;

import com.epicentric.common.listgenerator.ListGenerator;
import com.epicentric.common.listgenerator.ListGeneratorException;
import com.epicentric.common.listgenerator.PagedIterator;
import com.epicentric.common.listgenerator.SortSpecification;
import com.epicentric.common.listgenerator.SortSpecificationGenerator;
import com.epicentric.common.listgenerator.filter.SearchFilter;
import com.epicentric.common.listgenerator.filter.SearchFilterGenerator;
import com.epicentric.uid.UIDException;
import com.epicentric.uid.UniquelyIdentifiable;
import com.epicentric.uid.internal.UIDRegistrationEvent;
import com.epicentric.uid.internal.UIDRegistrationListener;
import com.epicentric.uid.internal.UniquelyIdentifiableManager;
import com.hp.it.spf.sso.portal.MockeryUtils;

/**
 * <p>
 * This is a surrogate class used by JUnit test to instead of Vignette's
 * <tt>SiteManager</tt>
 * </p>
 * <p>
 * In the source code need to be test by JUnit, there are many static methods
 * invoked driectly from the classes of the third party projects, such as
 * Vigentte.
 * </p>
 * <p>
 * To avoid invoking that static methods mentioned above, custom classes will be
 * added to instand of that refered classes in the tested code at runtime.
 * </p>
 * 
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 * @version 1.0
 */
public class SiteManager implements UniquelyIdentifiableManager, UIDRegistrationListener, PropertyChangeListener, ListGenerator, SearchFilterGenerator, SortSpecificationGenerator {
    
    public static SiteManager getInstance() {        
        return new SiteManager();
    }

    public Site getSiteFromDNSName(String name) throws SiteException {
        Mockery context = MockeryUtils.createMockery();
        Site site = MockeryUtils.mockSite(context, name);
        return site;
    }

    public Site getDefaultSite() {
        Mockery context = MockeryUtils.createMockery();
        Site site = MockeryUtils.mockSite(context, null);
        return site;
    }

    public void deleteObjectFromUID(String s) throws UIDException {
        // TODO Auto-generated method stub
        
    }

    public UniquelyIdentifiable getObjectFromUID(String s) throws UIDException {
        // TODO Auto-generated method stub
        return null;
    }

    public Collection getSupportedUIDTypes() {
        // TODO Auto-generated method stub
        return null;
    }

    public void uidDeregistered(UIDRegistrationEvent uidregistrationevent) {
        // TODO Auto-generated method stub
        
    }

    public void uidRegistered(UIDRegistrationEvent uidregistrationevent) {
        // TODO Auto-generated method stub
        
    }

    public void propertyChange(PropertyChangeEvent evt) {
        // TODO Auto-generated method stub
        
    }

    public PagedIterator getList(Collection collection,
                                 Collection collection1,
                                 int i) throws ListGeneratorException {
        // TODO Auto-generated method stub
        return null;
    }

    public PagedIterator getList(String s,
                                 boolean flag,
                                 Collection collection,
                                 int i) throws ListGeneratorException {
        // TODO Auto-generated method stub
        return null;
    }

    public SearchFilter getSearchFilter(String s, String s1) {
        // TODO Auto-generated method stub
        return null;
    }

    public SortSpecification getSortSpecification(String s, boolean flag) {
        // TODO Auto-generated method stub
        return null;
    }
}
