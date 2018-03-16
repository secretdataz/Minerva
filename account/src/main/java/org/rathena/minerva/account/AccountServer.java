package org.rathena.minerva.account;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rathena.minerva.account.net.packet.LoginPacketListener;
import org.rathena.minerva.commons.DefaultServer;
import org.rathena.minerva.commons.Minerva;
import org.rathena.minerva.commons.MMOServer;
import org.rathena.minerva.commons.net.GameServer;
import org.rathena.minerva.commons.net.PacketDatabase;
import org.rathena.minerva.commons.util.config.ServerConfiguration;

public class AccountServer implements MMOServer {

    public ServerConfiguration config;
    public static Logger logger;

    private PacketDatabase packetDatabase;

    public static void main(String... args) {
        logger = LogManager.getLogger();
        logger.info("Initializing Minerva account server " + Minerva.getMinervaVersion());
        Minerva.setServerType("Account Server");
        MMOServer accountServer = createServerFromArgs(args);
        Minerva.setInstance(accountServer);
        accountServer.start();
    }

    public AccountServer(ServerConfiguration config) {
        this.config = config;
    }

    /**
     * Create a MMOServer instance with options provided in command line arguments.
     *
     * @param args Command line arguments
     * @return An AccountServer instance if the command line arguments did not prevent the server from running.
     * A DefaultServer otherwise.
     */
    private static MMOServer createServerFromArgs(String... args) {
        ServerConfiguration config = parseCommandLineArgs(args);
        if(!config.isValid())
            return new DefaultServer();

        return new AccountServer(config);
    }

    private static ServerConfiguration parseCommandLineArgs(String... args) {
        ServerConfiguration config = ServerConfiguration.parseCommonCommandLineArgs(args);

        return config;
    }

    @Override
    public void start() {
        logger.info("Using packet version: " + config.getPacketVersion());
        try {
            GameServer gameServer = new GameServer(this.config.getPort());
            packetDatabase = gameServer;
            packetDatabase.registerPacketListener("MINERVA", new LoginPacketListener(packetDatabase));
            gameServer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public PacketDatabase getPacketDatabase() {
        return packetDatabase;
    }
}
