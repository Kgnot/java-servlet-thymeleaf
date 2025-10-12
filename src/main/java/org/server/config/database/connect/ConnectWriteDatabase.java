package org.server.config.database.connect;

import org.server.config.database.DatabaseTypes;
import org.server.config.properties.AppProperties;
import org.server.config.shared.Configuration;

@Configuration
public class ConnectWriteDatabase extends Connect {


    public ConnectWriteDatabase() {
        super();
        DatabaseTypes type = DatabaseTypes.WRITE;
        // creamos las propiedades
        String url = AppProperties.get("DATABASE_WRITE_URL");
        String user = AppProperties.get("DATABASE_WRITE_USER");
        String password = AppProperties.get("DATABASE_WRITE_PASSWORD");
        // Set a los atributos
        super.setAttributes(url, user, password, type);
        super.establishConnection(); // establecemos la conexión
    }
}
