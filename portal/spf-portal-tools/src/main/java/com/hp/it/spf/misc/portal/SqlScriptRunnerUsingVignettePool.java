package com.hp.it.spf.misc.portal;

import com.epicentric.jdbc.ConnectionPool;
import com.epicentric.jdbc.ConnectionPoolManager;

import java.sql.Connection;

import static com.hp.it.spf.misc.portal.SqlScriptRunner.getArgNameValue;
import static com.hp.it.spf.misc.portal.SqlScriptRunner.isEmpty;
import static com.hp.it.spf.misc.portal.SqlScriptRunner.runScriptAndCloseConnection;

/**
 * Command line tool to execute SQL scripts using Vignette connection pool definitions.
 * Run this class without any parameters to see its usage.
 *
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class SqlScriptRunnerUsingVignettePool
{
    private enum CmdArg {
        poolName, delimiter, isFullLineDelimiter, scriptPath;
    }

    private static void printUsageAndExit() {
        System.out.printf("java [options] %s --%s={path} [--%s={name:=portalserver}] [--%s={value:=;}] [--%s={true|false:=false}]%n",
                SqlScriptRunner.class.getName(),
                CmdArg.scriptPath, CmdArg.poolName,
                CmdArg.delimiter, CmdArg.isFullLineDelimiter);
        System.exit(0);
    }


    public static void main(String[] args) throws Exception
    {
        if (args.length < 1) {
            printUsageAndExit();
        }

        String scriptPath = null;

        String poolName = "portalserver";
        String delimiter = ";";
        boolean isFullLineDelimiter = false;

        for (String arg : args) {
            String[] argNameValue = getArgNameValue(arg);

            CmdArg cmdArg = CmdArg.valueOf(argNameValue[0]);
            String argValue = argNameValue[1];

            switch (cmdArg) {
                case scriptPath: scriptPath = argValue; break;
                case poolName: poolName = argValue; break;
                case delimiter: delimiter = argValue; break;
                case isFullLineDelimiter: isFullLineDelimiter = Boolean.valueOf(argValue); break;
            }
        }

        if (isEmpty(scriptPath) || isEmpty(poolName) || isEmpty(delimiter)) {
            printUsageAndExit();
        }

        ConnectionPool pool = ConnectionPoolManager.getPool(poolName);
        Connection connection = pool.getConnection();

        runScriptAndCloseConnection(connection, scriptPath, delimiter, isFullLineDelimiter);
    }
}
