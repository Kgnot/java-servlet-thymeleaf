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

    @Inject
    private UserDao userDao;

    private final ExecutorService virtualThreadExecutor = Executors.newVirtualThreadPerTaskExecutor();


    public CompletableFuture<List<UserEntity>> getUsers() {
        return CompletableFuture.supplyAsync(
                userDao::getUsers,
                virtualThreadExecutor
        );
    }

    public CompletableFuture<UserEntity> getUser(int id) {
        return CompletableFuture.supplyAsync(
                () -> userDao.getUserById(id),
                virtualThreadExecutor
        );
    }
}