package org.server.model.bd;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserService {

    private final DataBasesConnection bdc;
    private final UserDao userDao;

    private final ExecutorService virtualThreadExecutor = Executors.newVirtualThreadPerTaskExecutor();

    public UserService() throws SQLException {
        bdc = new DataBasesConnection();
        userDao = new UserDao(bdc);
    }

    public CompletableFuture<List<User>> getUsers() {
        CompletableFuture<List<User>> future = new CompletableFuture<>();

        virtualThreadExecutor.submit(() -> {
            List<User> userList = userDao.getUsers();
            future.complete(userList);
        });

        return future;
    }
}


/*
*
public class UserService {
    private final UserDao userDao;

    public UserService() throws SQLException {
        DataBasesConnection dbc = new DataBasesConnection();
        this.userDao = new UserDao(dbc);
    }

    public CompletableFuture<List<User>> getUsers() {
        return CompletableFuture.supplyAsync(
                userDao::getUsers,
                Executors.newVirtualThreadPerTaskExecutor()
        );
    }
}
* */