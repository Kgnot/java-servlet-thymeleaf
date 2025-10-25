package org.server.model.CQRS.command.impl.user;

import org.server.config.shared.Component;
import org.server.config.shared.Inject;
import org.server.model.CQRS.command.CommandHandler;
import org.server.model.CQRS.command.result.CommandResult;
import org.server.model.repository.user.UserCommandRepository;

@Component
public class CreateUserCommandHandler implements CommandHandler<CreateUserCommand> {

    @Inject("NeonSQL")
    private UserCommandRepository userCommandRepository;

    @Override
    public CommandResult handle(CreateUserCommand command) {
        boolean isSuccessful = userCommandRepository.save(command);
        String message = isSuccessful ? "Usuario creado correctamente" : "Usuario no fue creado";

        return new CommandResult(message, true);
    }
}
