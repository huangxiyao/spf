package org.springframework.web.context.support;

import javax.servlet.ServletContext;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.springframework.web.context.WebApplicationContext;

import com.hp.it.cas.persona.uav.service.EUserIdentifierType;
import com.hp.it.cas.persona.user.service.IUserService;

public class WebApplicationContextUtils {
    public static WebApplicationContext getWebApplicationContext(ServletContext sc)
    {
        Mockery context = new Mockery();
        final WebApplicationContext wac = context.mock(WebApplicationContext.class);
        final IUserService userService = context.mock(IUserService.class);
        context.checking(new Expectations() {
            {
                allowing(wac).getBean(with(any(String.class)));
                will(returnValue(userService));
                
                allowing(userService).createUser(with(any(EUserIdentifierType.class)), with(any(String.class)));
            }
        });
        return wac;
    }
}
