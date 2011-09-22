package com.hp.it.spf.misc.portal;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Command line tool to execute SQL scripts. Run this class without any parameters to see
 * its usage.
 *
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class SqlScriptRunner
{
    private static final Pattern ARG_PATTERN = Pattern.compile("^--(.+?)=(.*)$");

    private enum CmdArg {
        driver, jdbcUrl, username, password, delimiter, isFullLineDelimiter, scriptPath;
    }

    private static void printUsageAndExit() {
        System.out.printf("java [options] %s --%s={class name} --%s={url} --%s={user} --%s={pwd} --%s={path} [--%s={value:=;}] [--%s={true|false:=false}]%n",
                SqlScriptRunner.class.getName(),
                CmdArg.driver, CmdArg.jdbcUrl, CmdArg.username, CmdArg.password, CmdArg.scriptPath,
                CmdArg.delimiter, CmdArg.isFullLineDelimiter);
        System.exit(0);
    }

    public static void main(String[] args) throws Exception
    {
        if (args.length < 5) {
            printUsageAndExit();
        }

        String driverClassName = null;
        String jdbcUrl = null;
        String username = null;
        String password = null;
        String scriptPath = null;

        String delimiter = ";";
        boolean isFullLineDelimiter = false;

        for (String arg : args) {
            String[] argNameValue = getArgNameValue(arg);

            CmdArg cmdArg = CmdArg.valueOf(argNameValue[0]);
            String argValue = argNameValue[1];

            switch(cmdArg) {
                case driver: driverClassName = argValue; break;
                case username: username = argValue; break;
                case jdbcUrl: jdbcUrl = argValue; break;
                case password: password = argValue; break;
                case delimiter: delimiter = argValue; break;
                case isFullLineDelimiter: isFullLineDelimiter = Boolean.valueOf(argValue); break;
                case scriptPath: scriptPath = argValue; break;
            }
        }

        if (isEmpty(driverClassName) || isEmpty(jdbcUrl)
                || isEmpty(username) || isEmpty(password)
                || isEmpty(delimiter) || isEmpty(scriptPath))
        {
            printUsageAndExit();
        }

        Class.forName(driverClassName);
        Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

        runScriptAndCloseConnection(connection, scriptPath, delimiter, isFullLineDelimiter);
    }

    static String[] getArgNameValue(String argumentWithValue) {
        Matcher m = ARG_PATTERN.matcher(argumentWithValue);

        if (!m.matches()) {
            throw new IllegalArgumentException("Cannot parse argument: [" + argumentWithValue + "]");
        }

        return new String[] {m.group(1), m.group(2)};
    }

    static boolean isEmpty(String s) {
        return s == null || s.trim().equals("");
    }

    static void runScriptAndCloseConnection(Connection connection, String scriptPath, String delimiter, boolean isFullLineDelimiter) throws IOException
    {
        IBatisScriptRunner scriptRunner = new IBatisScriptRunner(connection);
        try {
            scriptRunner.setAutoCommit(true);
            scriptRunner.setDelimiter(delimiter);
            scriptRunner.setFullLineDelimiter(isFullLineDelimiter);
            scriptRunner.setLogWriter(new PrintWriter(System.out));
            scriptRunner.setErrorLogWriter(new PrintWriter(System.err));
            scriptRunner.setSendFullScript(false);
            scriptRunner.setStopOnError(true);

            Reader script = new FileReader(scriptPath);
            try {
                scriptRunner.runScript(script);
            }
            finally {
                script.close();
            }
        }
        finally {
            scriptRunner.closeConnection();
        }
    }
}
