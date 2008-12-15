package com.hp.it.spf.xa.common.portal.surrogate;

import java.util.Map;
import com.vignette.portal.log.LogWrapper;

public class SurrogateLogWrapper extends LogWrapper {
    
    public SurrogateLogWrapper(Class cls) {
        super(cls);
    }
    
    public SurrogateLogWrapper(Class cls, String subSystem) {
        super(cls, subSystem);
    }
    
    public void    debug(Object message) { }
    public void    debug(Object message, Map map) { }
    public void    debug(Object message, Map map, Throwable throwable) { }
    public void    debug(Object message, Throwable throwable) { }
    public void    debug(Throwable error) { }
    public void    error(Object message) { }
    public void    error(Object message, Map map) { }
    public void    error(Object message, Map map, Throwable throwable) { }
    public void    error(Object message, Throwable throwable) { }
    public void    error(Throwable error) { }
    public void    fatal(Object message) { }
    public void    fatal(Object message, Map map) { }
    public void    fatal(Object message, Map map, Throwable throwable) { }
    public void    fatal(Object message, Throwable throwable) { }
    public void    fatal(Throwable error) { }
    public void    info(Object message) { }
    public void    info(Object message, Map map) { }

    public void info(Object message, Map map, Throwable throwable) { }
    public void    info(Object message, Throwable throwable) { }
    public void    info(Throwable error) { }
    public void    warning(Object message) { }
    public void    warning(Object message, Map map) { }
    public void    warning(Object message, Map map, Throwable throwable) { }
    public void    warning(Object message, Throwable throwable) { }
    public void    warning(Throwable error) { }
    public boolean     willLogAtLevel(int level) {
        return true;
    }
}
