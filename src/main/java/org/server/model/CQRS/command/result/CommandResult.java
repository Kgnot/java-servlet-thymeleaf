package org.server.model.CQRS.command.result;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CommandResult {
    private String message;
    private boolean successful;
}
