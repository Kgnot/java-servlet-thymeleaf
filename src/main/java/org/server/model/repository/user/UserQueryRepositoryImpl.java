package org.server.model.repository.user;

import jakarta.persistence.EntityManager;
import org.server.config.database.factory.ConnectFactory;
import org.server.config.shared.Component;
import org.server.config.shared.Inject;
import org.server.model.CQRS.quey.impl.user.GetUserListQuery;
import org.server.model.dto.UserDto;
import org.server.model.entities.UserEntity;

import java.util.List;

@Component("NeonSQL")
public class UserQueryRepositoryImpl implements UserQueryRepository {

    @Inject("ReadDatabase")
    private ConnectFactory connect;

    public List<UserDto> findAll(GetUserListQuery query) {
        try (EntityManager em = connect.getConnectInstance().getEntityManager()) {

            var cb = em.getCriteriaBuilder();
            var cq = cb.createQuery(UserDto.class);
            var root = cq.from(UserEntity.class);

            cq.select(cb.construct(UserDto.class,
                    root.get("id"),
                    root.get("username"),
                    root.get("roleId"),
                    root.get("isActive")
            ));


            var typedQuery = em.createQuery(cq);

            return typedQuery.getResultList();
        }
    }


}
