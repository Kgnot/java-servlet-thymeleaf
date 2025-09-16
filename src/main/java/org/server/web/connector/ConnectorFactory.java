package org.server.web.connector;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.ssl.SslContextFactory;

public class ConnectorFactory {

    private final Server server;

    public ConnectorFactory(Server server) {
        this.server = server;
    }

    public Connector createHttpConnector(int port) {
        ServerConnector serverConnector = new ServerConnector(server);
        serverConnector.setPort(port);
        return serverConnector;
    }

    public Connector createHttpsConnector(int port, String keystorePath, String keystorePassword) {
        SslContextFactory.Server sslContextFactory = new SslContextFactory.Server();
        sslContextFactory.setKeyStorePath(keystorePath);
        sslContextFactory.setKeyStorePassword(keystorePassword);

        ServerConnector sslConnector = new ServerConnector(server, sslContextFactory);
        sslConnector.setPort(port);
        return sslConnector;
    }


}
