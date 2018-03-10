package org.rathena.minerva.commons.util.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rathena.minerva.commons.Minerva;

public class ServerConfiguration {
    private boolean valid = false;

    /**
     * Protocol version for Minerva
     */
    private int packetVersion = 20171213;

    /**
     * Port number that this server listens to
     */
    private int port;

    /**
     * Create ServerConfiguration from parsing command line argument that is common for all servers.
     *
     * @param args Command line arguments
     * @return ServerConfiguration object
     */
    public static ServerConfiguration parseCommonCommandLineArgs(String... args) {
        Logger logger = LogManager.getLogger();

        ServerConfiguration configuration = new ServerConfiguration();

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            if (arg.isEmpty() || arg.charAt(0) != '-') {
                logger.fatal("Invalid command line argument: " + arg);
                continue;
            }

            arg = arg.toLowerCase();

            // Parameterless options

            switch(arg) {
                case "-v":
                case "--version":
                    logger.info("Minerva version: " + Minerva.getMinervaVersion());
                    logger.info("Server type: " + Minerva.getServerType());
                    return configuration;
            }

            if(args.length == i + 1) {
                logger.fatal("Command line argument `" + args[i] + "` was passed without value.");
                return configuration;
            }

            switch(arg) {
                case "-p":
                case "--port":
                    int port = Integer.valueOf(args[++i]);
                    if(port < 0 || port > 0xFFFF) {
                        System.err.println("Invalid port " + port);
                        return configuration;
                    }
                    configuration.port = port;
                    break;
                case "-pv":
                case "--packetver":
                    configuration.packetVersion = Integer.valueOf(args[++i]);
                    break;
            }
        }

        configuration.valid = true;
        return configuration;
    }

    /**
     * Indicates if this object should be used.
     * Usual case that this return false would be after the server was launched with --version.
     *
     * @return
     */
    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public int getPacketVersion() {
        return packetVersion;
    }

    public int getPort() {
        return port;
    }
}
