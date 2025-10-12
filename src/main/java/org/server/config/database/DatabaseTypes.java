package org.server.config.database;

import lombok.Getter;

public enum DatabaseTypes {
    WRITE("CRM_WRITE"),
    READ("CRM_READ");

    @Getter
    private final String type;

    DatabaseTypes(String type) {
        this.type = type;
    }
}
