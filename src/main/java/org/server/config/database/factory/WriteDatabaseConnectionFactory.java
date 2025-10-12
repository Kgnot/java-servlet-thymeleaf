package org.server.config.database.factory;

import org.server.config.database.connect.Connect;
import org.server.config.database.connect.ConnectWriteDatabase;
import org.server.config.shared.Configuration;

@Configuration("WriteDatabase")
public class WriteDatabaseConnectionFactory extends ConnectFactory {

    @Override
    public Connect getConnectInstance() {
        return new ConnectWriteDatabase();
    }
}
