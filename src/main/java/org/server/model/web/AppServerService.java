package org.server.model.web;

import org.server.model.web.connector.ConnectorFactory;
import org.server.model.web.handler.HandlerServerFactory;

import java.util.List;

/**
 * {@code AppServerService} actúa como una capa de servicio encargada de
 * inicializar, configurar y controlar el ciclo de vida del servidor de aplicaciones.
 * <p>
 * Se encarga de:
 * <ul>
 *   <li>Crear la instancia del {@link AppServer}.</li>
 *   <li>Configurar conectores de red mediante {@link ConnectorFactory}.</li>
 *   <li>Configurar manejadores de peticiones HTTP mediante {@link HandlerServerFactory}.</li>
 *   <li>Levantar y detener el servidor.</li>
 * </ul>
 *
 * <p>Actualmente, el servidor se configura para escuchar en el puerto {@code 8080},
 * aunque este valor podría extraerse en el futuro de un archivo de propiedades.</p>
 *
 * <p><b>Ejemplo de uso:</b></p>
 * <pre>{@code
 * AppServerService serverService = new AppServerService();
 * try {
 *     serverService.start();
 *     System.out.println("Servidor iniciado en http://localhost:8080");
 *     // mantener aplicación corriendo
 * } catch (Exception e) {
 *     e.printStackTrace();
 * } finally {
 *     serverService.stop();
 * }
 * }</pre>
 *
 * @author Henry
 * @since 1.0
 */
public class AppServerService {

    private final AppServer appServer;

    /**
     * Puerto en el cual el servidor HTTP escuchará las peticiones entrantes.
     * Actualmente fijo en {@code 8080}.
     */
    private final int port = 8080;

    /**
     * Crea una nueva instancia de {@code AppServerService}, inicializando
     * el {@link AppServer}, configurando el conector HTTP en el puerto
     * definido y registrando los manejadores de servlets.
     */
    public AppServerService() {
        appServer = new AppServer();
        HandlerServerFactory handlerFactory = new HandlerServerFactory();
        ConnectorFactory connectorFactory = new ConnectorFactory(appServer.getServer());

        // Configurar conector HTTP
        appServer.setConnector(connectorFactory.createHttpConnector(port));

        // Configurar manejadores (por ahora, un context servlet handler)
        appServer.setHandler(List.of(handlerFactory.createContextServletHandler()));

        // Crear instancia del servidor embebido
        appServer.createServer();
    }

    /**
     * Inicia el servidor de aplicaciones.
     *
     * @throws Exception si ocurre un error al iniciar el servidor.
     */
    public void start() throws Exception {
        this.appServer.getServer().start();
        this.appServer.getServer().join(); // esto lo mantiene vivo
    }

    /**
     * Detiene el servidor de aplicaciones.
     *
     * @throws Exception si ocurre un error al detener el servidor.
     */
    public void stop() throws Exception {
        this.appServer.getServer().stop();
    }
}
