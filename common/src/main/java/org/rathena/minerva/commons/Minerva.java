package org.rathena.minerva.commons;


import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.appender.rolling.*;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;

public class Minerva {

    private static final String MINERVA_VERSION = "1.0-SNAPSHOT";
    private static String serverType = "Unknown";

    private static MMOServer instance;

    private static Logger logger = LogManager.getLogger();

    public static void setInstance(MMOServer server) {
        if(instance == null)
            instance = server;
    }

    public static MMOServer getMinerva() {
        return instance;
    }

    public static String getMinervaVersion() {
        return MINERVA_VERSION;
    }

    public static String getServerType() {
        return serverType;
    }

    public static void setServerType(String serverType) {
        Minerva.serverType = serverType;
    }

    public static Logger getLogger() { return logger; }
}
