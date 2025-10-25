package org.server.model.repository.user;

import org.server.model.CQRS.command.impl.user.CreateUserCommand;

public interface UserCommandRepository {

    boolean save(CreateUserCommand createUserCommand);
}
