package org.server.model.mapper;

import org.server.model.entities.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    public static User entityToDomain(Object[] entity) {
        return new User(
                (Integer) entity[0],
                (String) entity[1],
                (String) entity[2]);
    }

    public static List<User> listEntityToDomain(List<Object[]> entity) {
        return entity.stream()
                .map(UserMapper::entityToDomain)
                .collect(Collectors.toList());
    }
}
