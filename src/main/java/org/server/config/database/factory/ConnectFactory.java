package org.server.config.database.factory;

import org.server.config.database.connect.Connect;

public abstract class ConnectFactory {


    public abstract Connect getConnectInstance();
}
