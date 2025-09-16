package org.server.model.service;

import org.server.config.shared.Inject;
import org.server.config.shared.Service;
import org.server.model.bd.DataBasesConnection;
import org.server.model.dto.UserDao;
import org.server.model.entities.User;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class UserService {

    private final UserDao userDao;

    private final ExecutorService virtualThreadExecutor = Executors.newVirtualThreadPerTaskExecutor();

    @Inject
    public UserService(DataBasesConnection bdc){
        this.userDao = new UserDao(bdc);
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