package org.server.model.dao;

import jakarta.persistence.EntityManager;
import org.server.config.database.factory.ConnectFactory;
import org.server.config.shared.Component;
import org.server.config.shared.Inject;
import org.server.model.entities.UserEntity;

import java.util.List;


@Component
public class UserDao {


//    @Inject("WriteDatabase")
    @Inject("ReadDatabase")
    private ConnectFactory connectFactory;

    public List<UserEntity> getUsers() {
        try (EntityManager em = connectFactory.getConnectInstance().getEntityManager()) {
            return em.createQuery("SELECT u FROM UserEntity u", UserEntity.class)
                    .getResultList();
        }

    }

    public UserEntity getUserById(int id) {
        try (EntityManager em = connectFactory.getConnectInstance().getEntityManager()) {
            return em.createQuery("SELECT u FROM UserEntity u WHERE u.id = :id", UserEntity.class)
                    .setParameter("id", id)
                    .getSingleResult();
        }
    }
}
