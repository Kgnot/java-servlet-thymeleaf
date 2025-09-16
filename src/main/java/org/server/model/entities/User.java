package org.server.model.entities;

public record User(
        Integer id,
        String username,
        String password_hash
) {
}
