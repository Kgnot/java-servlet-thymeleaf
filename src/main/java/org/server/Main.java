package org.server;

import org.server.bd.DataBasesConnection;
import org.server.bd.User;
import org.server.bd.UserDao;
import org.server.bd.UserService;
import org.server.web.AppServer;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) {

        // iniciamos el server:
        AppServer appServer = new AppServer();
        try {
            appServer.start();

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            DataBasesConnection dbc = new DataBasesConnection();
            UserDao userDao = new UserDao(dbc);

            // --- PRIMERA FORMA: directa ---
            long start1 = System.nanoTime();
            var result = userDao.getUsers();
            long end1 = System.nanoTime();
            System.out.println("Directo: " + result);
            System.out.println("Tiempo directo: " + (end1 - start1) / 1_000_000.0 + " ms");

            // --- SEGUNDA FORMA: con UserService (futuro) ---
            UserService userService = new UserService();

            long start2 = System.nanoTime();
            CompletableFuture<List<User>> result2 = userService.getUsers();
            List<User> users = result2.get(); // bloquea hasta que termina
            long end2 = System.nanoTime();

            System.out.println("Con Future: " + users);
            System.out.println("Tiempo con Future: " + (end2 - start2) / 1_000_000.0 + " ms");

        } catch (SQLException | ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
