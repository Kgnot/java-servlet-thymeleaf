package org.server.config.database.factory;

import org.server.config.database.connect.Connect;
import org.server.config.database.connect.ConnectWriteDatabase;
import org.server.config.shared.Configuration;

@Configuration("WriteDatabase")
public class WriteDatabaseConnectionFactory extends ConnectFactory {

    private final Connect instance;

    public WriteDatabaseConnectionFactory() {
        // Inicializa la conexión al crear el bean
        this.instance = new ConnectWriteDatabase();
    }

    @Override
    public Connect getConnectInstance() {
        return instance;
    }
}
