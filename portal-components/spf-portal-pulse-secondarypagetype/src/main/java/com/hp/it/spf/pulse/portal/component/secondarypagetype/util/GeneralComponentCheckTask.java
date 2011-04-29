package com.hp.it.spf.pulse.portal.component.secondarypagetype.util;


import java.io.Serializable;

/**
 * This abstract class is compact - it just returns the value of the stored
 * variables. Note: the values of status and responseTime will be set in the
 * subclass as method is invoked.
 * 
 * @author <link href="huakun.gao@hp.com"> Gao, Hua-Kun </link>
 * @version TBD
 */
public abstract class GeneralComponentCheckTask implements IComponentCheckTask, Serializable {
    /*
     * the checktask's name
     */
    private String name;

    /**
     * the checktask's current status
     */
    protected int status = STATUS_FAIL;

    /**
     * the spent time of a run operation. It is counted in ms.
     */
    protected long responseTime;

    /**
     * Construction method for GeneralComponentCheckTask
     * 
     * @param name used for getName() when necessary
     * @see #getName()
     */
    public GeneralComponentCheckTask(String name) {
        this.name = name;
    }

    /**
     * Empty implement of super interface's method of init.
     * @see IComponentCheckTask#init()
     */
    public void init() {
    }

    /**
     * Default implement of IComponentCheckTask#getName()
     * @return String
     * @see IComponentCheckTask#getName()
     */
    public final String getName() {
        return name;
    }

    /**
     * Default implement of IComponentCheckTask#getStatus()
     * @return STATUS_PASS or STATUS_FAIL
     * @see IComponentCheckTask#getStatus()
     */
    public final int getStatus() {
        return status;
    }

    /**
     * Default implement of IComponentCheckTask#getResponseTime()
     * @return long
     * @see IComponentCheckTask#getResponseTime()
     */
    public final long getResponseTime() {
        return responseTime;
    }

}
