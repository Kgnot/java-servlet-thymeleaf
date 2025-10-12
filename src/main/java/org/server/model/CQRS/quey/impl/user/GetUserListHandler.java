package org.server.model.CQRS.quey.impl.user;

import org.server.config.shared.Component;
import org.server.config.shared.Inject;
import org.server.model.CQRS.quey.QueryHandler;
import org.server.model.dto.UserDto;
import org.server.model.repository.UserRepository;

import java.util.List;

@Component
public class GetUserListHandler implements QueryHandler<GetUserListQuery, List<UserDto>> {

    @Inject
    private UserRepository repository;

    @Override
    public List<UserDto> handle(GetUserListQuery query) {
        return repository.findAll(query);
    }
}
