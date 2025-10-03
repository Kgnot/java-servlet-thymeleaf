package org.server.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * Representa la entidad {@code UserEntity}, la cual está mapeada a la tabla {@code users}.
 *
 * <p>Esta clase utiliza anotaciones de JPA para mapear los campos de la clase
 * con las columnas correspondientes en la base de datos.</p>
 *
 * <p>Incluye información sobre las credenciales del usuario, su rol,
 * la fecha de creación y si la cuenta está activa.</p>
 *
 * <p>Ejemplo de uso:</p>
 * <pre>{@code
 *   UserEntity user = new UserEntity();
 *   user.setUsername("juan");
 *   user.setPassword_hash("hashSeguro123");
 *   user.setRole_id(2);
 *   user.setCreated_at(new Timestamp(System.currentTimeMillis()));
 *   user.setIs_active(true);
 * }</pre>
 *
 * @author TuNombre
 * @version 1.0
 */
@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    /**
     * Identificador único del usuario (Primary Key).
     * Se genera automáticamente en la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id;

    /**
     * Nombre de usuario asociado a la cuenta.
     */
    @Column(name = "username")
    private String username;

    /**
     * Hash de la contraseña almacenado de manera segura.
     */
    @Column(name = "password_hash")
    private String password_hash;

    /**
     * Identificador del rol asignado al usuario.
     * Normalmente hace referencia a otra tabla (roles).
     */
    @Column(name = "role_id")
    private int role_id;

    /**
     * Fecha y hora de creación del registro de usuario.
     */
    @Column(name = "created_at")
    private Timestamp created_at;

    /**
     * Estado del usuario.
     * {@code true} si la cuenta está activa,
     * {@code false} si la cuenta está deshabilitada o inactiva.
     */
    @Column(name = "is_active")
    private boolean is_active;
}
