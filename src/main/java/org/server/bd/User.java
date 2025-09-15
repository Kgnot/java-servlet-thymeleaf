package org.server.bd;

public record User(
        Integer id,
        String username,
        String password_hash
) {
}
