package org.server.model.bd;

import org.server.config.shared.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@Component
public class DataBasesConnection {

    private final String url = System.getProperty("DB.URL");
    private final String username = System.getProperty("DB.USERNAME");
    private final String password = System.getProperty("DB.PASSWORD");
    private final Connection connection;


    public DataBasesConnection() throws SQLException {
        this.connection = DriverManager.getConnection(url, username, password);

    }

    public Connection getConnection() {
        return connection;
    }

    public List<Object[]> getQuery(String query) throws SQLException {
        List<Object[]> results = new ArrayList<>();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query); // TODO Manage this problem

        while (rs.next()) {
            Integer id = rs.getInt("user_id");
            String username = rs.getString("username");
            String password_hash = rs.getString("password_hash");
            results.add(new Object[]{id, username, password_hash});
        }

        return results;
    }


}
