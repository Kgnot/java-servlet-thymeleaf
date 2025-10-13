package org.server.config.web.connector;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.ssl.SslContextFactory;

/**
 * {@code ConnectorFactory} se encarga de crear conectores para el servidor Jetty.
 * <p>
 * Proporciona métodos para generar conectores HTTP y HTTPS configurados con el servidor.
 */
public class ConnectorFactory {

    /** Servidor Jetty asociado al que se agregarán los conectores. */
    private final Server server;

    /**
     * Crea una nueva fábrica de conectores para un servidor específico.
     *
     * @param server el servidor Jetty al que se asociarán los conectores
     */
    public ConnectorFactory(Server server) {
        this.server = server;
    }

    /**
     * Crea un conector HTTP simple en el puerto especificado.
     *
     * @param port puerto en el que escuchará el servidor
     * @return un {@link Connector} configurado para HTTP
     */
    public Connector createHttpConnector(int port) {
        ServerConnector serverConnector = new ServerConnector(server);
        serverConnector.setPort(port);
        return serverConnector;
    }

    /**
     * Crea un conector HTTPS en el puerto especificado usando un keystore.
     *
     * @param port             puerto en el que escuchará el servidor
     * @param keystorePath     ruta al archivo del keystore
     * @param keystorePassword contraseña del keystore
     * @return un {@link Connector} configurado para HTTPS
     */
    public Connector createHttpsConnector(int port, String keystorePath, String keystorePassword) {
        SslContextFactory.Server sslContextFactory = new SslContextFactory.Server();
        sslContextFactory.setKeyStorePath(keystorePath);
        sslContextFactory.setKeyStorePassword(keystorePassword);

        ServerConnector sslConnector = new ServerConnector(server, sslContextFactory);
        sslConnector.setPort(port);
        return sslConnector;
    }
}
