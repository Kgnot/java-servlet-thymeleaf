package org.server.model.dao;

import jakarta.persistence.EntityManager;
import org.server.config.hibernate.HibernateUtil;
import org.server.config.shared.Component;
import org.server.config.shared.Inject;
import org.server.model.entities.UserEntity;

import java.util.List;

/**
 * DAO (Data Access Object) para la entidad {@link UserEntity}.
 * <p>
 * Esta clase se encarga de manejar la interacción con la base de datos
 * para operaciones relacionadas con usuarios, utilizando JPA (Hibernate como proveedor).
 * </p>
 *
 * <p>Está marcada como {@code @Component} para que pueda ser gestionada
 * por el contenedor de inyección de dependencias personalizado.</p>
 */
@Component
public class UserDao {

    /**
     * Utilidad de Hibernate que provee instancias de {@link EntityManager}.
     * Se inyecta automáticamente por el contenedor de dependencias.
     */
    @Inject
    private HibernateUtil hibernate;

    /**
     * Obtiene todos los usuarios de la base de datos.
     *
     * @return una lista de objetos {@link UserEntity} que representan
     *         todos los registros de la tabla de usuarios.
     */
    public List<UserEntity> getUsers() {
        try (EntityManager em = hibernate.getEntityManager()) {
            return em.createQuery("SELECT u FROM UserEntity u", UserEntity.class)
                    .getResultList();
        }
    }

    /**
     * Busca un usuario específico por su identificador único.
     *
     * @param id el identificador del usuario a buscar.
     * @return un objeto {@link UserEntity} correspondiente al usuario con el ID dado.
     * @throws jakarta.persistence.NoResultException si no se encuentra ningún usuario con el ID proporcionado.
     */
    public UserEntity getUserById(int id) {
        try (EntityManager em = hibernate.getEntityManager()) {
            return em.createQuery("SELECT u FROM UserEntity u WHERE u.id = :id", UserEntity.class)
                    .setParameter("id", id)
                    .getSingleResult();
        }
    }
}
