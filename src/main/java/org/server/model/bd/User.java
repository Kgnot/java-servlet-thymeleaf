package org.server.model.bd;

public record User(
        Integer id,
        String username,
        String password_hash
) {
}
