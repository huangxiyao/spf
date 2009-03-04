/*
 * Project: Shared Portal Framework 
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.sso.portal;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;

import com.epicentric.authentication.Realm;
import com.epicentric.entity.EntityPersistenceException;
import com.epicentric.entity.EntityType;
import com.epicentric.entity.UniquePropertyValueConflictException;
import com.epicentric.entity.implementations.generic.ChildEntityWrapper;
import com.epicentric.site.Site;
import com.epicentric.user.User;
import com.epicentric.user.UserGroup;
import com.epicentric.user.UserProvider;
import com.hp.it.cas.persona.user.service.IUserService;
import com.hp.it.spf.persona.PersonaUserServiceFilter;

/**
 * This is a utils class to create mockery context and create needed mock
 * objects
 * 
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 * @version 1.0
 */
public class MockeryUtils {
    /**
     * Create Mockery context and init it
     * 
     * @return Mockery context
     */
    public static Mockery createMockery() {
        Mockery context = new Mockery();
        // set imposteriser to instance and CGLIB will be used,
        // If this value is not assigned, dynamic Proxy will be used.
        context.setImposteriser(ClassImposteriser.INSTANCE);

        context.setDefaultResultForType(Object.class, null);
        return context;
    }

    /**
     * Create HttpServletRequest mock object with no name specified.
     * 
     * @param context mockery context
     * @return mocked HttpServletRequest object
     */
    public static HttpServletRequest mockHttpServletRequest(Mockery context) {
        return mockHttpServletRequest(context, null);
    }
    
    /**
     * Create HttpServletRequest mock object and set expectations to it.
     * 
     * @param context mockery context
     * @return mocked HttpServletRequest object
     */
    public static HttpServletRequest mockHttpServletRequest(Mockery context, String name) {
        // mock HttpServletRequest
        final HttpServletRequest request = context.mock(HttpServletRequest.class, name);
        context.checking(new Expectations() {
            {
                allowing(request).getSession();
                allowing(request).getSession(with(any(Boolean.class)));                

                allowing(request).getAttribute(AuthenticationConsts.SSO_USER_LOCALE);
                will(returnValue(new Locale("en", "US")));
                
                allowing(request).setAttribute(with(any(String.class)), with(any(Object.class)));
                
                allowing(request).setAttribute(with(any(String.class)), with(any(null)));
                
                allowing(request).getParameter("initSession");
                will(returnValue("false"));
                
                allowing(request).getServletPath();
                will(returnValue("/site"));
            }
        });
        return request;
    }
    
    /**
     * Create atHP HttpServletRequest mock object and set expectations to it.
     * 
     * @param context mockery context
     * @return mocked HttpServletRequest object
     */
    public static HttpServletRequest mockHttpServletRequestForAtHP(Mockery context) {
        // mock HttpServletRequest
        final HttpServletRequest request = mockHttpServletRequest(context, "atHP");        
        context.checking(new Expectations() {
            {
                allowing(request).getPathInfo();will(returnValue("/acme"));
                
                allowing(request).getHeader("SM_AUTHDIRNAME");will(returnValue("ED5_employees"));
                allowing(request).getHeader("SM_USER");will(returnValue("test@hp.com"));
                allowing(request).getHeader("SM_UNIVERSALID");will(returnValue("test@hp.com"));
                allowing(request).getHeader("timeZoneName");will(returnValue("Greenwich Mean Time : Dublin, Edinburgh, Lisbon, London"));
                allowing(request).getHeader("LAST_CHANGE_DATE");will(returnValue("20070308110124Z"));
                allowing(request).getHeader("givenname");will(returnValue("Doe"));
                allowing(request).getHeader("preferredlanguage");will(returnValue("en"));
                allowing(request).getHeader("email");will(returnValue("test@hp.com"));
                allowing(request).getHeader("country_code");will(returnValue("cn"));
                allowing(request).getHeader("sn");will(returnValue("John"));
                allowing(request).getHeader("city");will(returnValue("Shanghai"));
                allowing(request).getHeader("mailStop");will(returnValue(""));
                allowing(request).getHeader("state");will(returnValue(""));
                allowing(request).getHeader("street");will(returnValue("No. 2555, Building C $ Jin Ke Road $ Zhang Jiang Hi-Tech Development Zone"));
                allowing(request).getHeader("zipcode");will(returnValue("201203"));
                allowing(request).getHeader("phone_number");will(returnValue("+86-21-38890000"));
                allowing(request).getHeader("SECURITYLEVEL");will(returnValue("15"));
                allowing(request).getHeader("sm_usergroups");will(returnValue("cn=HP_IT_Employees_Shanghai,ou=Groups,o=hp.com^cn=ORG-GIT,ou=Groups,o=hp.com^cn=SP_BOI,ou=Groups,o=hp.com^cn=SP_HPAGENT,ou=Groups,o=hp.com^cn=SP_TRIATON,ou=Groups,o=hp.com^cn=USERS-CHINA-MOBILE,ou=Groups,o=hp.com^cn=USERS-RemoteAccess,ou=groups,o=hp.com^cn=USERS-SQUIRRELMAIL-RETIRE-ALL7,ou=Groups,o=hp.com^cn=sp.build,ou=Groups,o=hp.com"));
                allowing(request).getHeader("AuthSource");will(returnValue("ATHP"));
            }
        });
        return request;
    }

    /**
     * Create HPP HttpServletRequest mock object and set expectations to it.
     * 
     * @param context mockery context
     * @return mocked HttpServletRequest object
     */
    public static HttpServletRequest mockHttpServletRequestForHPP(Mockery context) {
        // mock HttpServletRequest
        final HttpServletRequest request = mockHttpServletRequest(context, "HPP");        
        context.checking(new Expectations() {
            {
                allowing(request).getPathInfo();will(returnValue("/acme"));
                
                allowing(request).getHeader("Accept");will(returnValue("image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/x-shockwave-flash, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*"));
                allowing(request).getHeader("Accept-Language");will(returnValue("zh-cn"));
                allowing(request).getHeader("Accept-Encoding");will(returnValue("gzip, deflate"));
                allowing(request).getHeader("User-Agent");will(returnValue("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 1.0.3705; .NET CLR 1.1.4322; .NET CLR 2.0.50727)"));
                allowing(request).getHeader("Host");will(returnValue("bollo22.serviceportal.hp.com"));
                allowing(request).getHeader("hppgroups");will(returnValue("USERS-CHINA-MOBILE|ADMIN_PERSONA"));
                allowing(request).getHeader("SM_TRANSACTIONID");will(returnValue("=?UTF-8?B?NDUzOGY2MGYtMDcwNC00NjI4NTEwZS0xNWU4LTAwYTQyNDll?="));
                allowing(request).getHeader("SM_SDOMAIN");will(returnValue("=?UTF-8?B?LmhwLmNvbQ==?="));
                allowing(request).getHeader("SM_REALM");will(returnValue("=?UTF-8?B?MDYtMDAwNTBlZDItMGZiMi0xNTZmLTlhY2ItNDZkNjNhMDQwMDAw?="));
                allowing(request).getHeader("SM_AUTHTYPE");will(returnValue("=?UTF-8?B?QW5vbnltb3Vz?="));
                allowing(request).getHeader("SM_SESSIONDRIFT");will(returnValue("=?UTF-8?B?MA==?="));
                allowing(request).getHeader("SM_UNIVERSALID");will(returnValue("=?UTF-8?B?ZDMxNDBhNTAyODkyNTg1YTk0YTNlNjYxM2QzZWM3ZWU=?="));
                allowing(request).getHeader("SM_AUTHDIROID");will(returnValue("=?UTF-8?B?MGUtOWIzYTA2MGMtMmY3NS00ZGZjLWIwOTEtMmNhZGYxNmFhYTIw?="));
                allowing(request).getHeader("SM_AUTHDIRNAME");will(returnValue("=?UTF-8?B?VURfUFJJTUFSWQ==?="));
                allowing(request).getHeader("SM_AUTHDIRSERVER");will(returnValue("=?UTF-8?B?SFBQX1BSSU1BUlk=?="));
                allowing(request).getHeader("SM_AUTHDIRNAMESPACE");will(returnValue("=?UTF-8?B?T0RCQzo=?="));
                allowing(request).getHeader("bustelnr");will(returnValue("88388834"));
                allowing(request).getHeader("bustelxtn");will(returnValue("010"));
                allowing(request).getHeader("bustelcntry");will(returnValue("86"));
                allowing(request).getHeader("bustelcity");will(returnValue("0571"));
                allowing(request).getHeader("cookie");will(returnValue("ATHP_COOKIE=; HP_SP_ENTER_USERID=vignetteAdmin; SMIDENTITY=1TXNYBq9Kh6NjrnSnc3xdi/MnlLg48m6gLCYDqZQKQZUllDdaBstT+QoalALkzLe8ZDDUP+cCWHQ0jOvcmWuBlEcD0w7UchZMnPTWgDXsgt8a6G/tMApDM3KvVZs995u+Go+UIivmRBRHGEpxDXdtty7A3wKfLhz+SB9n26fWAV7FT6M06awc+05a8wBo44tmXX/mMOErYhoiSMS9MM5P5tp5vfqZTxLezYrXNY8TuJSvu2dc1ksgUVlxN4mxd8QOXsy7UlRH04GVVhn9o0YDMCh/zXuVNuIn8qbmjvVsnxmEPejR4TnNw3EY/sDNVRNz2gHSavSzgELs1//h1h1j5INxHEf3UkW/zTaW6TDNrTs82MApp94I4Xdt0upfkxXOrPvqX7MeyqEgJKkxjWrM96NES24LTrWtGSYydIjzoB0md4du2LXwc8jbzsr7u1BBt3QE2Zbur249yazC3QO1Hr8VHuqWU/D7znNMQBzdwIC5i7G+oo22jPq9KDHVHLDgiaKfRAjhkXJN5uIeCgbokkHXELfGXDqon+8F8PZ+Z+aL+vi9hGqcQ==; c_lang==?UTF-8?B?ZW4=?=; c_u==?UTF-8?B?bWFuc2Fuc3JtYW5zYW5zcnw3YmZkMDYyNTllMmQzNDc4ZjkzZjgxOWI4ZWY5MjVlZQ==?=; ; JSESSIONID=bsL4GyQQBQlxngRhdS8cFfgJYbDnD9vRGY24vpccn4RLvhp2Wwlk!195315785; SMSESSION=JJquaN4UDF6OZtW51bkO3jY7MfQJbzlSc6FfAWEOTch0VDFk2n6uZYiQUDobSbhNxsMnv4wiQJShDz0BjZrnX/X+GoFUR/PGh/iA0xNIvmqpw90TiK6C42vJB2CL5RUTF1KBpat9KVWxy7COmnMS7fpEH3CbiwbnKtqXsbpNSQNcP8GEkR1ydPZryDrGcPjcNJLzRPHwIJ1t5OWUiaeoUZakXwvASPPiW/ZO4nxzQGtxqJZo72ropLVMVj4v0rjUep7DMD41c5clh9YluA37SQ1zAPiguKdcWzRKPGRu5BPj8x3AYH0WeZSJceAJjYf7y2i+Bg0Df0F+BEpMXLZOwTy1/dKoYJlEXkgiK1K1TqdKksdzQ10xLH79XwcyueRUBt2Q1NZzZ9ak2YclFY/iJNpzXlr5/0MfRybwHJyTxx5gMhgtZXtvkWe9KRJ8iOb4AEFjzXiTHsminXHAoedMQZTpZzLPpjun+uDRnmrpF3MJ6UWCiUlUPTdnj+8ZmBPxZsUBtVESWYfg33iWoDwjW8AkFq21CRn/x3I7GswzvYK+kmcAXQEw4vbqqjMqm22YXzANi/FIi7IjR5wjarzOmbLH9I8kBudBd+vsC4lT+PCp4QxBX5ftwMniL1qLon4BINqMoIL9L3iCAgWqaiTc20/JUpbPMy3NhUcM9LCjEM5U3bYr4sRGNKoNepb0uoBZs/LSM88uC/Hq+M4XpAQPxmg9v/kqul7qy2aWccrVorGys0LO0dSsyfw+XNWGkYHJHz2in8h5Ep81DHZtCtUIamW6DhBtMNzc5wE8jLo202vPuV/y/ZHwsJ/lwgszze/3nwyIKllEIYvVA/4wn9pEwbgDTAuOly7jAIg5UWtw4daYnvZ6eSU14WLxAHyid4wlEKv78v/1hk8HS3yNImMLecjaBQR6VrmBbhBvwOgh8es="));
                allowing(request).getHeader("SM_USER");will(returnValue("=?UTF-8?B?dmlnbmV0dGVBZG1pbg==?="));
                allowing(request).getHeader("SM_USERDN");will(returnValue("=?UTF-8?B?dmlnbmV0dGVBZG1pbg==?="));
                allowing(request).getHeader("SM_SERVERSESSIONID");will(returnValue("=?UTF-8?B?eEg3emRiOG1nMmRIYTZ3UGRMQW9WQXpabE5jPQ==?="));
                allowing(request).getHeader("SM_SERVERSESSIONSPEC");will(returnValue("=?UTF-8?B?SnhhMzB2STREZXY3ZVJFd1pYY0ZCMHhqcmIyaWlSN2x4cjFSSmtRSDZ5Y0wyYmxQUmliMDhmeHlyZENIMEFUVGZkdDJXVkQ2MUZQZnBWM09ja0xPdXhQYXVDV203UUVyeWVqbWpDRDU2R09XNTUyYlJPU1MyZnVVc0U5WmhDbmcvMlNFbGZPeDREeXdQdzJ2TmxaTUk3ekl3RFNWUWMxQzZSY1FDUXZPTlB1bi82Zkp5NHk2ZGpUdi9PRWpMWndLdk95TThIb3NYQ2NRaHFzY2lYeSs1UFJDMHlOcTg3VmNicWdWTWNYcHg2eGVMRHdidEFzam5ZOXBsZXF5cjVTNnZmUFE5WnZrYk9JNTBycEIwb0tvMVJ6ck85YTlBSXo3MGl5RHd2dVBVRWs0OWtaR3prZ0xGOS9sZEViNmhBd24xdGV1STgrRUFKZm9VTzMvSytHYVhUQi9SazB5Nlh4VlZ2c1hxMll0UERXOHNCZnlyWno1UE5TUjA0cGlOYUQyUkFOQU50K3BZOEU9?="));
                allowing(request).getHeader("SM_SERVERIDENTITYSPEC");will(returnValue("=?UTF-8?B?RU93NTJqUFA2Qk5Jbm1VdmJJSXFVU1F4dHVteFZ2cm9qL3Y2NW4yVTRMdzhQOUdkTStGMndZZStmdkx5Ykg5Tm9kUmdRaTNSUytBOTJxb3g5SVUxUWRxNVpubzFJUyt0TzJGbldlQUQ2K1U0cE9YUVAwV3UwUlNKUThQNGVuQ0ZzVGxlc3BSZ1BrMDk0SUZGdDJWcDJWN0FJTUZ3bm9Pa1VZMEswcWM1RWtEaUFnOTRCZGF3dTdaeWQ5R0FiczdFN1VOOEorb2hReTJ4ckFEUHpsU05GQytvQ01sdW1VK1RROXk3RFIrdXVMRnFhRmhqYlNhVkl2QVJuNzdMRTRiYzRxcDYxMC90SlFqODJ3eGVpRVBSdjVxNVRxZGY5VTh0cTFuVUZZYmsvLzg9?="));
                allowing(request).getHeader("SM_AUTHREASON");will(returnValue("=?UTF-8?B?MA==?="));
                allowing(request).getHeader("CL_Header");will(returnValue("=?UTF-8?B?cHJlZmVycmVkbGFuZ3VhZ2U9MTN8aHBjbG5hbWU9b3dlbmJlYXJ8aHBjbGlkbnVtYmVyPWFjYTI0OTcyMGM5YjkxYjRkMGMwZDI2YzFjNzUxZjIxfGhwcmVzaWRlbnRjb3VudHJ5Y29kZT1VU3xzbj1MaXV8Z2l2ZW5uYW1lPVlhbmd8ZW1haWw9eWFuZy5saXUyQGhwLmNvbXxjcmVhdGV0aW1lc3RhbXA9MjAwNi0xMS0xNCAwMDoxMTo0OXxtb2RpZnl0aW1lc3RhbXA9MjAwNy0wNC0yMCAwMDoyNjo1OHxjbGFuZz1VUy0xMw==?="));
                allowing(request).getHeader("prefeml");will(returnValue("=?UTF-8?B?WQ==?="));
                allowing(request).getHeader("prefphn");will(returnValue("=?UTF-8?B?Tg==?="));
                allowing(request).getHeader("prefpst");will(returnValue("=?UTF-8?B?Tg==?="));
                allowing(request).getHeader("slvl");will(returnValue("=?UTF-8?B?Mw==?="));
                allowing(request).getHeader("tz");will(returnValue("=?UTF-8?B?V1NULTg=?="));
                allowing(request).getHeader("AccessType");will(returnValue("Internet"));
                allowing(request).getHeader("Connection");will(returnValue("Keep-Alive"));
                allowing(request).getHeader("WL-Proxy-SSL");will(returnValue("true"));
                allowing(request).getHeader("WL-Proxy-Client-IP");will(returnValue("16.158.81.82"));
                allowing(request).getHeader("Proxy-Client-IP");will(returnValue("16.158.81.82"));
                allowing(request).getHeader("X-Forwarded-For");will(returnValue("16.158.81.82"));
                allowing(request).getHeader("X-WebLogic-Force-JVMID");will(returnValue("195315785"));                
                allowing(request).getHeader("AuthSource");will(returnValue("HPP"));
                
                allowing(request).getCookies();
                will(returnValue(new Cookie[] {new Cookie("SMSESSION", "NOT")}));
            }
        });
        return request;
    }
    
    /**
     * Create HPP HttpServletRequest mock object and set expectations to it.
     * 
     * @param context mockery context
     * @return mocked HttpServletRequest object
     */
    public static HttpServletRequest mockHttpServletRequestForFed(Mockery context) {
        // mock HttpServletRequest
        final HttpServletRequest request = mockHttpServletRequest(context, "FED");        
        context.checking(new Expectations() {
            {
                allowing(request).getPathInfo();will(returnValue("/acmefed"));
                
                allowing(request).getHeader("Accept");will(returnValue("image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/x-shockwave-flash, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*"));
                allowing(request).getHeader("Accept-Language");will(returnValue("zh-cn"));
                allowing(request).getHeader("Accept-Encoding");will(returnValue("gzip, deflate"));
                allowing(request).getHeader("User-Agent");will(returnValue("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 1.0.3705; .NET CLR 1.1.4322; .NET CLR 2.0.50727)"));
                allowing(request).getHeader("Host");will(returnValue("bollo22.serviceportal.hp.com"));
                allowing(request).getHeader("hppgroups");will(returnValue("USERS-CHINA-MOBILE|ADMIN_PERSONA"));
                allowing(request).getHeader("SM_TRANSACTIONID");will(returnValue("=?UTF-8?B?NDUzOGY2MGYtMDcwNC00NjI4NTEwZS0xNWU4LTAwYTQyNDll?="));
                allowing(request).getHeader("SM_SDOMAIN");will(returnValue("=?UTF-8?B?LmhwLmNvbQ==?="));
                allowing(request).getHeader("SM_REALM");will(returnValue("=?UTF-8?B?MDYtMDAwNTBlZDItMGZiMi0xNTZmLTlhY2ItNDZkNjNhMDQwMDAw?="));
                allowing(request).getHeader("SM_AUTHTYPE");will(returnValue("=?UTF-8?B?QW5vbnltb3Vz?="));
                allowing(request).getHeader("SM_SESSIONDRIFT");will(returnValue("=?UTF-8?B?MA==?="));
                allowing(request).getHeader("SM_UNIVERSALID");will(returnValue("=?UTF-8?B?ZDMxNDBhNTAyODkyNTg1YTk0YTNlNjYxM2QzZWM3ZWU=?="));
                allowing(request).getHeader("SM_AUTHDIROID");will(returnValue("=?UTF-8?B?MGUtOWIzYTA2MGMtMmY3NS00ZGZjLWIwOTEtMmNhZGYxNmFhYTIw?="));
                allowing(request).getHeader("SM_AUTHDIRNAME");will(returnValue("=?UTF-8?B?VURfUFJJTUFSWQ==?="));
                allowing(request).getHeader("SM_AUTHDIRSERVER");will(returnValue("=?UTF-8?B?SFBQX1BSSU1BUlk=?="));
                allowing(request).getHeader("SM_AUTHDIRNAMESPACE");will(returnValue("=?UTF-8?B?T0RCQzo=?="));
                allowing(request).getHeader("cookie");will(returnValue("ATHP_COOKIE=; HP_SP_ENTER_USERID=vignetteAdmin; SMIDENTITY=1TXNYBq9Kh6NjrnSnc3xdi/MnlLg48m6gLCYDqZQKQZUllDdaBstT+QoalALkzLe8ZDDUP+cCWHQ0jOvcmWuBlEcD0w7UchZMnPTWgDXsgt8a6G/tMApDM3KvVZs995u+Go+UIivmRBRHGEpxDXdtty7A3wKfLhz+SB9n26fWAV7FT6M06awc+05a8wBo44tmXX/mMOErYhoiSMS9MM5P5tp5vfqZTxLezYrXNY8TuJSvu2dc1ksgUVlxN4mxd8QOXsy7UlRH04GVVhn9o0YDMCh/zXuVNuIn8qbmjvVsnxmEPejR4TnNw3EY/sDNVRNz2gHSavSzgELs1//h1h1j5INxHEf3UkW/zTaW6TDNrTs82MApp94I4Xdt0upfkxXOrPvqX7MeyqEgJKkxjWrM96NES24LTrWtGSYydIjzoB0md4du2LXwc8jbzsr7u1BBt3QE2Zbur249yazC3QO1Hr8VHuqWU/D7znNMQBzdwIC5i7G+oo22jPq9KDHVHLDgiaKfRAjhkXJN5uIeCgbokkHXELfGXDqon+8F8PZ+Z+aL+vi9hGqcQ==; c_lang==?UTF-8?B?ZW4=?=; c_u==?UTF-8?B?bWFuc2Fuc3JtYW5zYW5zcnw3YmZkMDYyNTllMmQzNDc4ZjkzZjgxOWI4ZWY5MjVlZQ==?=; ; JSESSIONID=bsL4GyQQBQlxngRhdS8cFfgJYbDnD9vRGY24vpccn4RLvhp2Wwlk!195315785; SMSESSION=JJquaN4UDF6OZtW51bkO3jY7MfQJbzlSc6FfAWEOTch0VDFk2n6uZYiQUDobSbhNxsMnv4wiQJShDz0BjZrnX/X+GoFUR/PGh/iA0xNIvmqpw90TiK6C42vJB2CL5RUTF1KBpat9KVWxy7COmnMS7fpEH3CbiwbnKtqXsbpNSQNcP8GEkR1ydPZryDrGcPjcNJLzRPHwIJ1t5OWUiaeoUZakXwvASPPiW/ZO4nxzQGtxqJZo72ropLVMVj4v0rjUep7DMD41c5clh9YluA37SQ1zAPiguKdcWzRKPGRu5BPj8x3AYH0WeZSJceAJjYf7y2i+Bg0Df0F+BEpMXLZOwTy1/dKoYJlEXkgiK1K1TqdKksdzQ10xLH79XwcyueRUBt2Q1NZzZ9ak2YclFY/iJNpzXlr5/0MfRybwHJyTxx5gMhgtZXtvkWe9KRJ8iOb4AEFjzXiTHsminXHAoedMQZTpZzLPpjun+uDRnmrpF3MJ6UWCiUlUPTdnj+8ZmBPxZsUBtVESWYfg33iWoDwjW8AkFq21CRn/x3I7GswzvYK+kmcAXQEw4vbqqjMqm22YXzANi/FIi7IjR5wjarzOmbLH9I8kBudBd+vsC4lT+PCp4QxBX5ftwMniL1qLon4BINqMoIL9L3iCAgWqaiTc20/JUpbPMy3NhUcM9LCjEM5U3bYr4sRGNKoNepb0uoBZs/LSM88uC/Hq+M4XpAQPxmg9v/kqul7qy2aWccrVorGys0LO0dSsyfw+XNWGkYHJHz2in8h5Ep81DHZtCtUIamW6DhBtMNzc5wE8jLo202vPuV/y/ZHwsJ/lwgszze/3nwyIKllEIYvVA/4wn9pEwbgDTAuOly7jAIg5UWtw4daYnvZ6eSU14WLxAHyid4wlEKv78v/1hk8HS3yNImMLecjaBQR6VrmBbhBvwOgh8es="));
                allowing(request).getHeader("bustelnr");will(returnValue("88388834"));
                allowing(request).getHeader("bustelxtn");will(returnValue("010"));
                allowing(request).getHeader("bustelcntry");will(returnValue("86"));
                allowing(request).getHeader("bustelcity");will(returnValue("0571"));
                allowing(request).getHeader("SM_USER");will(returnValue("=?UTF-8?B?dmlnbmV0dGVBZG1pbg==?="));
                allowing(request).getHeader("SM_USERDN");will(returnValue("=?UTF-8?B?dmlnbmV0dGVBZG1pbg==?="));
                allowing(request).getHeader("SM_SERVERSESSIONID");will(returnValue("=?UTF-8?B?eEg3emRiOG1nMmRIYTZ3UGRMQW9WQXpabE5jPQ==?="));
                allowing(request).getHeader("SM_SERVERSESSIONSPEC");will(returnValue("=?UTF-8?B?SnhhMzB2STREZXY3ZVJFd1pYY0ZCMHhqcmIyaWlSN2x4cjFSSmtRSDZ5Y0wyYmxQUmliMDhmeHlyZENIMEFUVGZkdDJXVkQ2MUZQZnBWM09ja0xPdXhQYXVDV203UUVyeWVqbWpDRDU2R09XNTUyYlJPU1MyZnVVc0U5WmhDbmcvMlNFbGZPeDREeXdQdzJ2TmxaTUk3ekl3RFNWUWMxQzZSY1FDUXZPTlB1bi82Zkp5NHk2ZGpUdi9PRWpMWndLdk95TThIb3NYQ2NRaHFzY2lYeSs1UFJDMHlOcTg3VmNicWdWTWNYcHg2eGVMRHdidEFzam5ZOXBsZXF5cjVTNnZmUFE5WnZrYk9JNTBycEIwb0tvMVJ6ck85YTlBSXo3MGl5RHd2dVBVRWs0OWtaR3prZ0xGOS9sZEViNmhBd24xdGV1STgrRUFKZm9VTzMvSytHYVhUQi9SazB5Nlh4VlZ2c1hxMll0UERXOHNCZnlyWno1UE5TUjA0cGlOYUQyUkFOQU50K3BZOEU9?="));
                allowing(request).getHeader("SM_SERVERIDENTITYSPEC");will(returnValue("=?UTF-8?B?RU93NTJqUFA2Qk5Jbm1VdmJJSXFVU1F4dHVteFZ2cm9qL3Y2NW4yVTRMdzhQOUdkTStGMndZZStmdkx5Ykg5Tm9kUmdRaTNSUytBOTJxb3g5SVUxUWRxNVpubzFJUyt0TzJGbldlQUQ2K1U0cE9YUVAwV3UwUlNKUThQNGVuQ0ZzVGxlc3BSZ1BrMDk0SUZGdDJWcDJWN0FJTUZ3bm9Pa1VZMEswcWM1RWtEaUFnOTRCZGF3dTdaeWQ5R0FiczdFN1VOOEorb2hReTJ4ckFEUHpsU05GQytvQ01sdW1VK1RROXk3RFIrdXVMRnFhRmhqYlNhVkl2QVJuNzdMRTRiYzRxcDYxMC90SlFqODJ3eGVpRVBSdjVxNVRxZGY5VTh0cTFuVUZZYmsvLzg9?="));
                allowing(request).getHeader("SM_AUTHREASON");will(returnValue("=?UTF-8?B?MA==?="));
                allowing(request).getHeader("CL_Header");will(returnValue("=?UTF-8?B?cHJlZmVycmVkbGFuZ3VhZ2U9MTN8aHBjbG5hbWU9b3dlbmJlYXJ8aHBjbGlkbnVtYmVyPWFjYTI0OTcyMGM5YjkxYjRkMGMwZDI2YzFjNzUxZjIxfGhwcmVzaWRlbnRjb3VudHJ5Y29kZT1VU3xzbj1MaXV8Z2l2ZW5uYW1lPVlhbmd8ZW1haWw9eWFuZy5saXUyQGhwLmNvbXxjcmVhdGV0aW1lc3RhbXA9MjAwNi0xMS0xNCAwMDoxMTo0OXxtb2RpZnl0aW1lc3RhbXA9MjAwNy0wNC0yMCAwMDoyNjo1OHxjbGFuZz1VUy0xMw==?="));
                allowing(request).getHeader("prefeml");will(returnValue("=?UTF-8?B?WQ==?="));
                allowing(request).getHeader("prefphn");will(returnValue("=?UTF-8?B?Tg==?="));
                allowing(request).getHeader("prefpst");will(returnValue("=?UTF-8?B?Tg==?="));
                allowing(request).getHeader("slvl");will(returnValue("=?UTF-8?B?Mw==?="));
                allowing(request).getHeader("tz");will(returnValue("=?UTF-8?B?V1NULTg=?="));
                allowing(request).getHeader("AccessType");will(returnValue("Internet"));
                allowing(request).getHeader("Connection");will(returnValue("Keep-Alive"));
                allowing(request).getHeader("WL-Proxy-SSL");will(returnValue("true"));
                allowing(request).getHeader("WL-Proxy-Client-IP");will(returnValue("16.158.81.82"));
                allowing(request).getHeader("Proxy-Client-IP");will(returnValue("16.158.81.82"));
                allowing(request).getHeader("X-Forwarded-For");will(returnValue("16.158.81.82"));
                allowing(request).getHeader("X-WebLogic-Force-JVMID");will(returnValue("195315785"));
                allowing(request).getHeader("AuthSource");will(returnValue("HPP"));
                
                allowing(request).getCookies();
                will(returnValue(new Cookie[] {new Cookie("SMSESSION", "NOT")}));
            }
        });
        return request;
    }
    
    /**
     * Create ANON HttpServletRequest mock object and set expectations to it.
     * 
     * @param context mockery context
     * @return mocked HttpServletRequest object
     */
    public static HttpServletRequest mockHttpServletRequestForANON(Mockery context) {
        // mock HttpServletRequest
        final HttpServletRequest request = mockHttpServletRequest(context, "ANON");        
        context.checking(new Expectations() {
            {
                allowing(request).getPathInfo();will(returnValue("/acme"));
                
                allowing(request).getHeader("SM_AUTHDIRNAME");will(returnValue(""));  
                allowing(request).getHeader("AuthSource");will(returnValue(""));
                
                allowing(request).getCookies();
                will(returnValue(new Cookie[] {new Cookie("SMSESSION", "LOGGEDOFF")}));
            }
        });
        return request;
    }
    
    /**
     * Create SandBox HttpServletRequest mock object and set expectations to it.
     * 
     * @param context mockery context
     * @return mocked HttpServletRequest object
     */
    public static HttpServletRequest mockHttpServletRequestForSandBox(Mockery context) {
        // mock HttpServletRequest
        final HttpServletRequest request = mockHttpServletRequest(context, "SandBox");        
        context.checking(new Expectations() {
            {
                allowing(request).getPathInfo();will(returnValue("/acme"));
                
                allowing(request).getParameter("guestMode");
                will(returnValue("false"));
            }
        });
        return request;
    }
    
    /**
     * Create Vignette user mock object and set expectations to it.
     * 
     * @param context mockery context
     * @return mocked User object
     * @throws EntityPersistenceException 
     * @throws UniquePropertyValueConflictException 
     * @see com.epicentric.user.User
     */
    public static User mockUser(Mockery context) throws UniquePropertyValueConflictException, EntityPersistenceException {
        // mock User Entity, EntityType
        final ChildEntityWrapper entity = context.mock(ChildEntityWrapper.class);
        final EntityType entityType = context.mock(EntityType.class);
        final UserGroup userGroup = context.mock(UserGroup.class);
        final UserGroup userGroup2 = context.mock(UserGroup.class, "GROUP2");
        context.checking(new Expectations() {
            {
                atLeast(1).of(entity).getUID();
                will(returnValue("epi:user.standard;22e9b53cd770499db193561024836ff0"));

                allowing(entity).getEntityType();
                will(returnValue(entityType));

                allowing(userGroup).getProperty(AuthenticationConsts.GROUP_TITLE);
                will(returnValue("LOCAL_TEST_GROUP"));
                
                allowing(userGroup2).getProperty(AuthenticationConsts.GROUP_TITLE);
                will(returnValue("LOCAL_PORTAL_LANG_EN"));

                try {
                    Set<UserGroup> set = new HashSet<UserGroup>();
                    set.add(userGroup);
                    set.add(userGroup2);
                    allowing(entity).getParents(with(any(EntityType.class)),
                                                with(any(Boolean.class)));
                    will(returnValue(set));
                } catch (EntityPersistenceException ex) {
                    will(throwException(ex));
                }

                allowing(entityType).getID();
                will(returnValue("epi:user.standard"));

                allowing(entity).getProperty(AuthenticationConsts.PROPERTY_PROFILE_ID);
                will(returnValue(UserProfile.get(AuthenticationConsts.KEY_PROFILE_ID)));

                allowing(entity).getProperty(AuthenticationConsts.PROPERTY_USER_NAME_ID);
                will(returnValue(UserProfile.get(AuthenticationConsts.KEY_USER_NAME)));

                allowing(entity).getProperty(AuthenticationConsts.PROPERTY_EMAIL_ID);
                will(returnValue(UserProfile.get(AuthenticationConsts.KEY_EMAIL)));

                allowing(entity).getProperty(AuthenticationConsts.PROPERTY_FIRSTNAME_ID);
                will(returnValue(UserProfile.get(AuthenticationConsts.KEY_FIRST_NAME)));

                allowing(entity).getProperty(AuthenticationConsts.PROPERTY_LASTNAME_ID);
                will(returnValue(UserProfile.get(AuthenticationConsts.KEY_LAST_NAME)));

                allowing(entity).getProperty(AuthenticationConsts.PROPERTY_COUNTRY_ID);
                will(returnValue(UserProfile.get(AuthenticationConsts.KEY_COUNTRY)));

                allowing(entity).getProperty(AuthenticationConsts.PROPERTY_LANGUAGE_ID);
                will(returnValue(UserProfile.get(AuthenticationConsts.KEY_LANGUAGE)));

                allowing(entity).getProperty(AuthenticationConsts.PROPERTY_LAST_CHANGE_DATE_ID);
                will(returnValue(UserProfile.get(AuthenticationConsts.KEY_LAST_CHANGE_DATE)));

                allowing(entity).getProperty(AuthenticationConsts.PROPERTY_SPF_TIMEZONE_ID);
                will(returnValue(UserProfile.get(AuthenticationConsts.KEY_TIMEZONE)));
                
                allowing(entity).getProperty(AuthenticationConsts.PROPERTY_PRIMARY_SITE_ID);
                will(returnValue(UserProfile.get(AuthenticationConsts.PROPERTY_PRIMARY_SITE_ID)));
                
                allowing(entity).setProperty(with(any(String.class)), with(any(Object.class)));
                
                allowing(entity).save();
            }
        });
        User user = UserProvider.getUser(entity);
        return user;
    }

    /**
     * Create Vignette guest user mock object and set expectations to it.
     * 
     * @param context mockery context
     * @return mocked User object
     * @see com.epicentric.user.User
     */
    public static User mockGuestUser(Mockery context) {
        // mock User Entity, EntityType
        final ChildEntityWrapper entity = context.mock(ChildEntityWrapper.class, "GUESTENTITY");
        final EntityType entityType = context.mock(EntityType.class, "GUESTEINTITYTYPT");
        final UserGroup userGroup = context.mock(UserGroup.class, "GUESTGROUP");
        final UserGroup userGroup2 = context.mock(UserGroup.class, "GUESTGROUP2");
        context.checking(new Expectations() {
            {
                atLeast(1).of(entity).getUID();
                will(returnValue("epi:user.guest"));

                allowing(entity).getEntityType();
                will(returnValue(entityType));

                allowing(userGroup).getProperty(AuthenticationConsts.GROUP_TITLE);
                will(returnValue("LOCAL_TEST_GROUP"));
                
                allowing(userGroup2).getProperty(AuthenticationConsts.GROUP_TITLE);
                will(returnValue("LOCAL_PORTAL_LANG_EN"));

                try {
                    Set<UserGroup> set = new HashSet<UserGroup>();
                    set.add(userGroup);
                    set.add(userGroup);
                    allowing(entity).getParents(with(any(EntityType.class)),
                                                with(any(Boolean.class)));
                    will(returnValue(set));
                } catch (EntityPersistenceException ex) {
                    will(throwException(ex));
                }

                allowing(entityType).getID();
                will(returnValue("epi:user.guest"));
            }
        });
        User guestUser = UserProvider.getUser(entity);
        return guestUser;
    }
    
    /**
     * Create HttpServletResponse mock object with <code>null</code> name.
     * 
     * @param context mockery context
     * @return mocked HttpServletResponse object
     */
    public static HttpServletResponse mockHttpServletResponse(Mockery context) {
        return mockHttpServletResponse(context, null);
    }
    
    /**
     * Create HttpServletResponse mock object and set expectations to it.
     * 
     * @param context mockery context
     * @param mocked object name
     * @return mocked HttpServletResponse object
     */
    public static HttpServletResponse mockHttpServletResponse(Mockery context, String name) {
        // mock HttpServletResponse
        final HttpServletResponse response = context.mock(HttpServletResponse.class, name);
        context.checking(new Expectations() {
            {
                atLeast(1).of(response).getLocale();
                will(returnValue(new Locale("en", "US")));                
            }
        });
        return response;
    }
    
    /**
     * Create Site mock object and set expectations to it.
     * 
     * @param context mockery context
     * @param mocked object name
     * @return mocked Site object
     */
    public static Site mockSite(Mockery context, final String name) {
     // mock Site
        final Site site = context.mock(Site.class, name);
        context.checking(new Expectations() {
            {
                allowing(site).getUID();
                will(returnValue(UserProfile.get(AuthenticationConsts.PROPERTY_PRIMARY_SITE_ID)));
                
                allowing(site).getDNSName();
                will(returnValue(name));
            }
        });
        return site;
    }
    
    /**
     * Create Realm mock object and set expectations to it.
     * 
     * @param context mockery context
     * @param mocked object name
     * @return mocked Realm object
     */
    public static Realm mockRealm(Mockery context, final String name) {
     // mock Site
        final Realm realm = context.mock(Realm.class, name);
        context.checking(new Expectations() {
            {
                one(realm).getID();
                will(returnValue("realmid"));
            }
        });
        return realm;
    }
}
