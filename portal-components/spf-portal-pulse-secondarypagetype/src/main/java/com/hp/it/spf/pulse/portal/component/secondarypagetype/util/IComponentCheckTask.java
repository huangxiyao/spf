package com.hp.it.spf.pulse.portal.component.secondarypagetype.util;


/**
 * An interface providing some base methods to get a component's information.
 * 
 * For example: You can call getStatus to retrieve the value for the current
 * status, if the return value is STATUS_PASS, that means OK, else if the value
 * is STATUS_FAIL, it means fail.
 * 
 * @author <link href="huakun.gao@hp.com"> Gao, Hua-Kun </link>
 * @version TBD
 * @see GeneralComponentCheckTask
 */
public interface IComponentCheckTask {
    /**
     * This constant value means service runs well.
     */
    public static final int STATUS_PASS = 0; // task is passed

    /**
     * This constant value means service runs in a bad situation.
     */
    public static final int STATUS_FAIL = 1; // task is failed

    /**
     * This method is invoked by health check page after a subclass is
     * instanced. If subclasses need to do some works once before run method,
     * those codes can be placed here. GeneralComponent class will provide an
     * empty method.
     * 
     * @see GeneralComponentCheckTask#init()
     */
    public void init();

    /**
     * This method is invoked by health check page whenever end-user wants to
     * have a look at the status of this component. In the implementation method
     * it will check the health of destination component, then update the status
     * and calculate the response time according to the result. Notice:
     * resources need to be released finally.
     */
    public void run();

    /**
     * This method is used to get the name of the destination component. It is
     * mainly used for getting the name of the component and displays it on the
     * Heath Check Page.
     * 
     * @return String
     */
    public String getName();

    /**
     * This method is used to get the status of the destination component. If
     * the return value is 0 then "PASS" will be displayed, else FAIL will be
     * displayed on the Heath Check Page.
     * 
     * @return STATUS_PASS or STATUS_FAIL
     */
    public int getStatus();

    /**
     * This method is used to get the response time of the whole operation on
     * the destination. Notice: It is counted in ms.
     * 
     * @return long
     */
    public long getResponseTime();
}
