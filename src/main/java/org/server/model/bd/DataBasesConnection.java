package org.server.model.bd;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBasesConnection {

    private final String url = "jdbc:postgresql://localhost:5432/fec";
    private final String username = "kgnot";
    private final String password = "1234";
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
        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
            Integer id = rs.getInt("user_id");
            String username = rs.getString("username");
            String password_hash = rs.getString("password_hash");
            results.add(new Object[]{id, username, password_hash});
        }

        return results;
    }


}
