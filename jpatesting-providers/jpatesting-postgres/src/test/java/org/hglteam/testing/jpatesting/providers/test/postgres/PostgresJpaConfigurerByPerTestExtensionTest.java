package org.hglteam.testing.jpatesting.providers.test.postgres;

import io.zonky.test.db.postgres.junit5.EmbeddedPostgresExtension;
import io.zonky.test.db.postgres.junit5.PreparedDbExtension;
import org.hglteam.testing.jpatesting.JpaPropertyConfigurer;
import org.hglteam.testing.jpatesting.core.JpaConfigurerExtension;
import org.hglteam.testing.jpatesting.providers.postgres.PostgresJpaConfigurer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PostgresJpaConfigurerByPerTestExtensionTest {

    @RegisterExtension
    public static PreparedDbExtension extension = EmbeddedPostgresExtension.preparedDatabase(e -> { });
    @RegisterExtension
    static JpaConfigurerExtension jpa = new JpaConfigurerExtension(PostgresJpaConfigurer.begin(extension::getTestDatabase)
            .properties()
            .schemaGenerationDatabaseAction(JpaPropertyConfigurer.DatabaseAction.DROP_AND_CREATE)
            .and()
            .persistenceUnitName("emptyEntityManagerSuccess")
            .withEntity(ManagedTestEntity.class), true);

    @Test
    void emptyEntityManagerInsertSuccess() {
        jpa.getEntityManager().getTransaction().begin();
        jpa.getEntityManager().persist(ManagedTestEntity.builder()
                .nombre("ENTIDAD1")
                .build());
        jpa.getEntityManager().getTransaction().commit();

        ManagedTestEntity stored = jpa.getEntityManager().createQuery("SELECT m from ManagedTestEntity m WHERE m.nombre=:pNombre", ManagedTestEntity.class)
                .setParameter("pNombre", "ENTIDAD1")
                .getSingleResult();

        assertNotNull(stored.getId());
        assertEquals("ENTIDAD1", stored.getNombre());
    }

    @Test
    void emptyEntityManagerSecondInsertSuccess() {
        jpa.getEntityManager().getTransaction().begin();
        jpa.getEntityManager().persist(ManagedTestEntity.builder()
                .nombre("ENTIDAD2")
                .build());
        jpa.getEntityManager().getTransaction().commit();

        ManagedTestEntity stored = jpa.getEntityManager().createQuery("SELECT m from ManagedTestEntity m WHERE m.nombre=:pNombre", ManagedTestEntity.class)
                .setParameter("pNombre", "ENTIDAD2")
                .getSingleResult();

        assertNotNull(stored.getId());
        assertEquals("ENTIDAD2", stored.getNombre());
    }
}
