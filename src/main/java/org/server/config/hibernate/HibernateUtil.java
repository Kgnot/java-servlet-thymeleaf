package org.server.config.hibernate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.server.config.shared.Configuration;

@Configuration
public class HibernateUtil {
	private final static Logger logger = Logger.getLogger(HibernateUtil.class.getName());
    private final EntityManagerFactory emf;

    public HibernateUtil() {
        try {
            this.emf = Persistence.createEntityManagerFactory("CRM");
        } catch (Exception e) {
            Throwable cause = e;
            while (cause.getCause() != null) {
                cause = cause.getCause();
                logger.log(Level.SEVERE,"Caused by: {}", cause.getMessage());
            }
           // Stack trace completo
//            e.printStackTrace();
            throw new RuntimeException("Failed to create EntityManagerFactory", e);
        }
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}
