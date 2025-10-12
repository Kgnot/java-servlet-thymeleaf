package org.server.config.database.connect;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.server.config.database.DatabaseTypes;
import org.server.config.database.error.EstablishConnectionError;

import java.util.HashMap;
import java.util.Map;

public abstract class Connect {

    protected Map<String, Object> overrides = new HashMap<>();
    protected EntityManagerFactory factory;
    // Atributos de conexion:
    private String url, user, password;
    private DatabaseTypes type;

    public Connect() {
    }

    public void setAttributes(
            String url,
            String user,
            String password,
            DatabaseTypes type
    ) {

        this.url = url;
        this.user = user;
        this.password = password;
        this.type = type;
    }

    public void establishConnection() {
        if (url == null || user == null || password == null || type == null) {
            throw new EstablishConnectionError("Existe un error en la conexión, verificar");
        }
        overrides.put("jakarta.persistence.jdbc.url", url);
        overrides.put("jakarta.persistence.jdbc.user", user);
        overrides.put("jakarta.persistence.jdbc.password", password);
        factory = Persistence.createEntityManagerFactory(type.getType(), overrides);
    }


    public EntityManager getEntityManager() {
        return factory.createEntityManager();
    }

}
