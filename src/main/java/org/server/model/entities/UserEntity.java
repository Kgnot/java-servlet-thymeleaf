package org.server.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id;

    @Column(name = "username")
    private String username;

    @Column(name = "password_hash")
    private String password_hash;

    @Column(name = "role_id")
    private int role_id;

    @Column(name = "created_at")
    private Timestamp created_at;

    @Column(name = "is_active")
    private boolean is_active;
        
    
}
