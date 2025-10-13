package org.server.config.database.factory;

import org.server.config.database.connect.Connect;
import org.server.config.database.connect.ConnectWriteDatabase;
import org.server.config.shared.Configuration;
import java.util.logging.Logger;

@Configuration("WriteDatabase")
public class WriteDatabaseConnectionFactory extends ConnectFactory {
    private static final Logger logger = Logger.getLogger(WriteDatabaseConnectionFactory.class.getName());

    private final Connect instance;

    public WriteDatabaseConnectionFactory() {
        this.instance = new ConnectWriteDatabase();
    }

    @Override
    public Connect getConnectInstance() {
        return instance;
    }
}
