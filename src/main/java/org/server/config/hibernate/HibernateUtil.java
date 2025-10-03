package org.server.config.hibernate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.server.config.shared.Configuration;

/**
 * {@code HibernateUtil} es una clase de utilidad que inicializa y expone un
 * {@link EntityManagerFactory} configurado mediante la unidad de persistencia "CRM".
 * <p>
 * Está anotada con {@link Configuration}, lo que permite que el contenedor de inyección
 * de dependencias detecte e instancie esta clase automáticamente como un bean de configuración.
 * </p>
 *
 * <p>Responsabilidades principales:</p>
 * <ul>
 *   <li>Inicializar el {@link EntityManagerFactory} usando la configuración de JPA/Hibernate.</li>
 *   <li>Manejar errores durante la creación de la factoría y loggear las causas subyacentes.</li>
 *   <li>Exponer un método {@link #getEntityManager()} para obtener instancias de {@link EntityManager}
 *   cuando se necesiten operaciones de persistencia.</li>
 * </ul>
 *
 * <p><b>Uso típico:</b></p>
 * <pre>{@code
 * HibernateUtil hibernateUtil = new HibernateUtil();
 * EntityManager em = hibernateUtil.getEntityManager();
 * try {
 *     em.getTransaction().begin();
 *     // operaciones con entidades
 *     em.getTransaction().commit();
 * } finally {
 *     em.close();
 * }
 * }</pre>
 *
 * @author Henry Ricaurte Mora
 * @since 1.0
 */
@Configuration
public class HibernateUtil {

    private static final Logger logger = Logger.getLogger(HibernateUtil.class.getName());

    private final EntityManagerFactory emf;

    /**
     * Crea una instancia de {@code HibernateUtil} inicializando la unidad de persistencia
     * "CRM". En caso de error, registra todas las causas subyacentes y lanza una
     * {@link RuntimeException}.
     *
     * @throws RuntimeException si ocurre un error al crear la factoría de entidades.
     */
    public HibernateUtil() {
        try {
            this.emf = Persistence.createEntityManagerFactory("CRM");
        } catch (Exception e) {
            Throwable cause = e;
            while (cause.getCause() != null) {
                cause = cause.getCause();
                // Nota: Logger de java.util no soporta placeholders como "{}".
                // Debes usar concatenación de cadenas o String.format
                logger.log(Level.SEVERE, "Caused by: " + cause.getMessage());
            }
            throw new RuntimeException("Failed to create EntityManagerFactory", e);
        }
    }

    /**
     * Devuelve una nueva instancia de {@link EntityManager} a partir del
     * {@link EntityManagerFactory} configurado.
     *
     * @return un nuevo {@link EntityManager}.
     */
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}