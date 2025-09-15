package org.server.bd;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    private DataBasesConnection db;


    public UserDao(DataBasesConnection db) {
        this.db = db;
    }

    public List<User> getUsers() {
        List<User> users;
        try {
            List<Object[]> resultQuery = db.getQuery("select * from users");
            users = UserMapper.listEntityToDomain(resultQuery);


        } catch (SQLException e) {
            System.out.println("Error al obtener los datos del usuario");
            return new ArrayList<>();

        }
        return users;
    }


}
