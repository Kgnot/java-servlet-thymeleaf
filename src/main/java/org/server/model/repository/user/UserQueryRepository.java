package org.server.model.repository.user;

import org.server.model.CQRS.quey.impl.user.GetUserListQuery;
import org.server.model.dto.UserDto;

import java.util.List;

public interface UserQueryRepository {

    List<UserDto> findAll(GetUserListQuery query);
}
