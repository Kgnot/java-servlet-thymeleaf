package org.server.model.repository.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.server.config.database.factory.ConnectFactory;
import org.server.config.shared.Component;
import org.server.config.shared.Inject;
import org.server.model.CQRS.command.impl.user.CreateUserCommand;
import org.server.model.entities.UserEntity;

import java.sql.Timestamp;

@Component("NeonSQL")
public class UserCommandRepositoryImpl implements UserCommandRepository{

    @Inject("WriteDatabase")
    private ConnectFactory connect;

    @Override
    public boolean save(CreateUserCommand createUserCommand) {
        try(EntityManager em = connect.getConnectInstance().getEntityManager()){

            EntityTransaction tx = em.getTransaction();
            tx.begin();

            UserEntity user = new UserEntity();
            user.setUsername(createUserCommand.getName());
            user.setPasswordHash(createUserCommand.getPassword());
            user.setRoleId(1/*createUserCommand.getRol()*/);
            user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            user.setActive(true);

            em.persist(user);

            tx.commit();
            return true;

        }
        catch (RuntimeException e){
            return false;
        }
    }
}
