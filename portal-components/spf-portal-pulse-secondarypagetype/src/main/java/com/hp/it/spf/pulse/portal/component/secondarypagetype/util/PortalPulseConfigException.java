package com.hp.it.spf.pulse.portal.component.secondarypagetype.util;


/**
 * A class extending Exception for PortalPulseConfig.
 * 
 * @author <link href="huakun.gao@hp.com"> Gao, Hua-Kun </link>
 * @version TBD
 */
public class PortalPulseConfigException extends Exception {
    
    /**
     * serialVersionUID
     * long
     */
    private static final long serialVersionUID = 1L;

    /**
     * constructor for PortalPulseConfigException
     */
    public PortalPulseConfigException() {
        super();
    }
    
    /**
     * constructor for PortalPulseConfigException
     * 
     * @param message exception message
     */
    public PortalPulseConfigException(String message) {
        super(message);
    }

    /**
     * constructor for PortalPulseConfigException
     * 
     * @param cause the cause of PortalPulseConfigException
     */
    public PortalPulseConfigException(Throwable cause) {
        super(cause);
    }
    
    /**
     * constructor for PortalPulseConfigException
     * 
     * @param message exception message
     * @param cause the cause of PortalPulseConfigException
     */
    public PortalPulseConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}
