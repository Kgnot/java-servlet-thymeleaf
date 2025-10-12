package org.server.config.database.factory;

import org.server.config.database.connect.Connect;
import org.server.config.database.connect.ConnectReadDatabase;
import org.server.config.shared.Configuration;

@Configuration("ReadDatabase")
public class ReadDatabaseConnectionFactory extends ConnectFactory {

    @Override
    public Connect getConnectInstance() {
        return new ConnectReadDatabase();
    }
}
