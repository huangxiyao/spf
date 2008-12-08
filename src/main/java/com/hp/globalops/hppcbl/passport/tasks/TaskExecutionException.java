package com.hp.globalops.hppcbl.passport.tasks;

/**
 * Created by IntelliJ IDEA.
 * User: millerand
 * Date: Aug 23, 2004
 * Time: 1:59:04 PM
 * To change this template use File | Settings | File Templates.
 */
import org.apache.axis.AxisFault;

public class TaskExecutionException extends Exception{

    private AxisFault axisFault;

    public TaskExecutionException(AxisFault axisFault){
        this.axisFault=axisFault;
    }

    public TaskExecutionException(String message) {
        super(message);
    }
    public TaskExecutionException(Exception cause) {
        super(cause.getMessage());
    }
    public AxisFault getAxisFault(){
        return axisFault;
    }

}
