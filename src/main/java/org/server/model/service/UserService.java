package org.server.model.service;

import org.server.config.shared.Inject;
import org.server.config.shared.Service;
import org.server.model.entities.UserEntity;
import org.server.model.dao.UserDao;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class UserService {

    private final UserDao userDao;

    private final ExecutorService virtualThreadExecutor = Executors.newVirtualThreadPerTaskExecutor();

    @Inject
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public CompletableFuture<List<UserEntity>> getUsers() {
        return CompletableFuture.supplyAsync(
                userDao::getUsers,
                virtualThreadExecutor
        );
    }
}