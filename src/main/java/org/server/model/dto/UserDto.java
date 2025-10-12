package org.server.model.dto;

public record UserDto(
        Integer id,
        String username,
        Integer roleId,
        boolean isActive
) {}
