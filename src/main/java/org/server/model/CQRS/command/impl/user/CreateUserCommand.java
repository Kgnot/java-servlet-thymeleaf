package org.server.model.CQRS.command.impl.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.server.model.CQRS.command.Command;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserCommand implements Command, Serializable {
    private String name;
    private String email;
    private String password;
    private String rol; // TODO luego ver si esto tiene que ver con el modelo
}
