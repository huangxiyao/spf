package com.epicentric.common.website;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.epicentric.user.User;

public class I18nUtils {

	static Locale _locale = Locale.ENGLISH;
	public static Locale getUserLocale(User user){
		return _locale;
	}
	
	public static Localizer getLocalizer(HttpSession session, HttpServletRequest request){
		return new Localizer(session, request);
	}
	
	public static void setUserLocale(User user, Locale locale){
		_locale = locale;
	}
	
	public static Collection getRegisteredLocales(){
		return Collections.singleton(Locale.ENGLISH);
	}
}
