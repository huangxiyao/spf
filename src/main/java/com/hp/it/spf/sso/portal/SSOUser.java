/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 *
 */
package com.hp.it.spf.sso.portal;

import java.io.Serializable;
import java.util.Set;

class SSOUser implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userName = null;

    private String firstName = null;

    private String lastName = null;

    private String email = null;

    private String country = null;

    private String language = null;

    private String timeZone = null;

    private Set groups = null;

    private String mailStop = null;

    private String street = null;

    private String city = null;

    private String zipCode = null;

    private String state = null;

    private String phone = null;

    Set getGroups() {
        return groups;
    }

    void setGroups(Set groups) {
        this.groups = groups;
    }

    /**
     * Get the property timeZone
     * 
     * @return the timeZone
     */
    String getTimeZone() {
        return timeZone;
    }

    /**
     * Set the property timeZone
     * 
     * @param timeZone
     *            the timeZone to set
     */
    void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    /**
     * Get the property country
     * 
     * @return the country
     */
    String getCountry() {
        return country;
    }

    /**
     * Set the property country
     * 
     * @param country
     *            the country to set
     */
    void setCountry(String country) {
        this.country = country;
    }

    /**
     * Get the property email
     * 
     * @return the email
     */
    String getEmail() {
        return email;
    }

    /**
     * Set the property email
     * 
     * @param email
     *            the email to set
     */
    void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get the property firstName
     * 
     * @return the firstName
     */
    String getFirstName() {
        return firstName;
    }

    /**
     * Set the property firstName
     * 
     * @param firstName
     *            the firstName to set
     */
    void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Get the property userName
     * 
     * @return the userName
     */
    String getUserName() {
        return userName;
    }

    /**
     * Set the property userName
     * 
     * @param userName
     *            the userName to set
     */
    void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Get the property lastName
     * 
     * @return the lastName
     */
    String getLastName() {
        return lastName;
    }

    /**
     * Set the property lastName
     * 
     * @param lastName
     *            the lastName to set
     */
    void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Get the property language
     * 
     * @return the language
     */
    String getLanguage() {
        return language;
    }

    /**
     * Set the property language
     * 
     * @param language
     *            the language to set
     */
    void setLanguage(String language) {
        this.language = language;
    }

    String getCity() {
        return city;
    }

    void setCity(String city) {
        this.city = city;
    }

    String getMailStop() {
        return mailStop;
    }

    void setMailStop(String mailStop) {
        this.mailStop = mailStop;
    }

    String getState() {
        return state;
    }

    void setState(String state) {
        this.state = state;
    }

    String getStreet() {
        return street;
    }

    void setStreet(String street) {
        this.street = street;
    }

    String getZipCode() {
        return zipCode;
    }

    void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    /**
     * Get the property phone
     * 
     * @return the phone
     */
    String getPhone() {
        return phone;
    }

    /**
     * Set the property phone
     * 
     * @param phone
     *            the phone to set
     */
    void setPhone(String phone) {
        this.phone = phone;
    }

}
