package org.server.config.database.connect;

import org.server.config.database.DatabaseTypes;
import org.server.config.properties.AppProperties;


public class ConnectReadDatabase extends Connect {


    public ConnectReadDatabase() {
        super();
        DatabaseTypes type = DatabaseTypes.READ;
        // creamos las propiedades
        String url = AppProperties.get("DATABASE_URL");
        String user = AppProperties.get("DATABASE_USER");
        String password = AppProperties.get("DATABASE_PASSWORD");
        // Set a los atributos
        super.setAttributes(url, user, password, type);
        super.establishConnection(); // establecemos la conexión
    }
}
