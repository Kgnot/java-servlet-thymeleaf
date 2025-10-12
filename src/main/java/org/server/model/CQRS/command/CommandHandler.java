package org.server.model.CQRS.command;

import org.server.model.CQRS.command.result.CommandResult;

public interface CommandHandler<T extends Command> {

    CommandResult handle(T command);

}
