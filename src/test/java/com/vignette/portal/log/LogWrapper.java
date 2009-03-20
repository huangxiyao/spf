package com.vignette.portal.log;

import java.util.Map;

public class LogWrapper
{

    public LogWrapper(Class clazz) throws IllegalArgumentException {
        this(clazz, null);
    }

    public LogWrapper(Class clazz, String subsystem) throws IllegalArgumentException {
        if (clazz == null)
            throw new IllegalArgumentException("Class can not be null");
        this.clazz = clazz;
        if (subsystem == null) {
            String name = clazz.getName();
            this.subsystem = name.substring(0, name.lastIndexOf('.'));
        } else {
            this.subsystem = subsystem;
        }
    }

    public void debug(Object message) {
    }

    public void debug(Throwable error) {
    }

    public void debug(Object message, Throwable throwable) {
    }

    public void debug(Object message, Map map) {
    }

    public void debug(Object message, Map map, Throwable throwable) {
    }

    public void info(Object message) {
    }

    public void info(Throwable error) {
    }

    public void info(Object message, Throwable throwable) {

    }

    public void info(Object message, Map map) {

    }

    public void info(Object message, Map map, Throwable throwable) {

    }

    public void warning(Object message) {

    }

    public void warning(Throwable error) {

    }

    public void warning(Object message, Throwable throwable) {

    }

    public void warning(Object message, Map map) {

    }

    public void warning(Object message, Map map, Throwable throwable) {
    }

    public void error(Object message) {
    }

    public void error(Throwable error) {

    }

    public void error(Object message, Throwable throwable) {

    }

    public void error(Object message, Map map) {

    }

    public void error(Object message, Map map, Throwable throwable) {

    }

    public void fatal(Object message) {

    }

    public void fatal(Throwable error) {
    }

    public void fatal(Object message, Throwable throwable) {

    }

    public void fatal(Object message, Map map) {

    }

    public void fatal(Object message, Map map, Throwable throwable) {

    }

    public boolean willLogAtLevel(int level) {
        return false;
    }

    private String subsystem;
    private Class clazz;
}