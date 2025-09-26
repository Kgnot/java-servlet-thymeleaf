package org.server.model.dao;

import jakarta.persistence.EntityManager;
import org.server.config.hibernate.HibernateUtil;
import org.server.config.shared.Component;
import org.server.config.shared.Inject;
import org.server.model.entities.UserEntity;

import java.util.List;

@Component
public class UserDao {

    @Inject
    private HibernateUtil hibernate;


    public List<UserEntity> getUsers() {
        try (EntityManager em = hibernate.getEntityManager()) {
            return em.createQuery("SELECT u FROM UserEntity u", UserEntity.class)
                    .getResultList();
        }
    }

}
