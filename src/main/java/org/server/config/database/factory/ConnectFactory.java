package org.server.config.database.factory;

import jakarta.persistence.EntityManagerFactory;
import org.server.config.database.connect.Connect;

public abstract class ConnectFactory {
    protected EntityManagerFactory emf;

    public abstract Connect getConnectInstance();

}
