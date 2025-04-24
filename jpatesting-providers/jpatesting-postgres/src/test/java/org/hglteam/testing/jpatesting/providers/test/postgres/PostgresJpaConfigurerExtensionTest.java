package org.hglteam.testing.jpatesting.providers.test.postgres;

import io.zonky.test.db.postgres.junit5.EmbeddedPostgresExtension;
import io.zonky.test.db.postgres.junit5.PreparedDbExtension;
import jakarta.persistence.EntityManager;
import org.hglteam.testing.jpatesting.JpaConfigurer;
import org.hglteam.testing.jpatesting.JpaPropertyConfigurer;
import org.hglteam.testing.jpatesting.providers.postgres.PostgresJpaConfigurer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PostgresJpaConfigurerExtensionTest {
    @RegisterExtension
    public static PreparedDbExtension extension = EmbeddedPostgresExtension.preparedDatabase(e -> { });
    @RegisterExtension
    public static JpaConfigurer<?> jpaConfigurer = PostgresJpaConfigurer.begin(extension::getTestDatabase)
            .properties()
            .schemaGenerationDatabaseAction(JpaPropertyConfigurer.DatabaseAction.DROP_AND_CREATE)
            .and()
            .persistenceUnitName("emptyEntityManagerSuccess")
            .withEntity(ManagedTestEntity.class);

    @Test
    void emptyEntityManagerInsertSuccess(EntityManager em) {
        em.getTransaction().begin();
        em.persist(ManagedTestEntity.builder()
            .nombre("ENTIDAD1")
            .build());
        em.getTransaction().commit();

        ManagedTestEntity stored = em.createQuery("SELECT m from ManagedTestEntity m WHERE m.nombre=:pNombre", ManagedTestEntity.class)
                .setParameter("pNombre", "ENTIDAD1")
                .getSingleResult();

        assertNotNull(stored.getId());
        assertEquals("ENTIDAD1", stored.getNombre());
    }
}
