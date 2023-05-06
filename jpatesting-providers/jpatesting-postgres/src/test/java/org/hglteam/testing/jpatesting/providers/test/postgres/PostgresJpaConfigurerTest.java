package org.hglteam.testing.jpatesting.providers.test.postgres;

import io.zonky.test.db.postgres.junit5.EmbeddedPostgresExtension;
import io.zonky.test.db.postgres.junit5.PreparedDbExtension;
import org.hglteam.testing.jpatesting.JpaPropertyConfigurer;
import org.hglteam.testing.jpatesting.providers.postgres.PostgresJpaConfigurer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import static org.junit.jupiter.api.Assertions.*;

class PostgresJpaConfigurerTest {
    private PostgresJpaConfigurer configurer;
    private EntityManagerFactory emf;
    private EntityManager em;

    @RegisterExtension
    public static PreparedDbExtension extension = EmbeddedPostgresExtension.preparedDatabase(e -> { });

    @Test
    void emptyEntityManagerSuccess() {
        configurer = PostgresJpaConfigurer.begin(extension::getTestDatabase)
                .properties()
                    .schemaGenerationDatabaseAction(JpaPropertyConfigurer.DatabaseAction.DROP_AND_CREATE)
                    .and()
                .persistenceUnitName("emptyEntityManagerSuccess")
                .withEntity(ManagedTestEntity.class);

        assertDoesNotThrow(() -> { emf = configurer.buildFactory(); });
        assertDoesNotThrow(() -> { em = emf.createEntityManager(); });

        em.close();
        emf.close();
    }

    @Test
    void emptyEntityManagerInsertSuccess() {
        configurer = PostgresJpaConfigurer.begin(extension::getTestDatabase)
                .properties()
                    .schemaGenerationDatabaseAction(JpaPropertyConfigurer.DatabaseAction.DROP_AND_CREATE)
                .and()
                .persistenceUnitName("emptyEntityManagerSuccess")
                .withEntity(ManagedTestEntity.class);

        emf = configurer.buildFactory();
        em = emf.createEntityManager();

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

        em.close();
        emf.close();
    }

    @Test
    void mappedEntityManagerInsertSuccess() {
        configurer = PostgresJpaConfigurer.begin(extension::getTestDatabase)
                    .properties()
                    .schemaGenerationDatabaseAction(JpaPropertyConfigurer.DatabaseAction.DROP_AND_CREATE)
                    .and()
                .persistenceUnitName("emptyEntityManagerSuccess")
                .withMapping("/META-INF/mappings/mapping.xml");

        emf = configurer.buildFactory();
        em = emf.createEntityManager();

        em.getTransaction().begin();
        em.persist(MappedTestEntity.builder()
                .nombre("ENTIDAD1")
                .build());
        em.getTransaction().commit();

        MappedTestEntity stored = em.createQuery("SELECT m from MappedTestEntity m WHERE m.nombre=:pNombre", MappedTestEntity.class)
                .setParameter("pNombre", "ENTIDAD1")
                .getSingleResult();

        assertNotNull(stored.getId());
        assertEquals("ENTIDAD1", stored.getNombre());

        em.close();
        emf.close();
    }

    @Test
    void mappedEntityManagerInsertSuccessWithDefault() {
        configurer = PostgresJpaConfigurer.begin()
                .properties()
                    .schemaGenerationDatabaseAction(JpaPropertyConfigurer.DatabaseAction.DROP_AND_CREATE)
                    .url("jdbc:postgresql://localhost:" + extension.getConnectionInfo().getPort() + "/" + extension.getConnectionInfo().getDbName())
                    .username("postgres")
                    .password("postgres")
                .and()
                .persistenceUnitName("emptyEntityManagerSuccess")
                .withMapping("/META-INF/mappings/mapping.xml");

        emf = configurer.buildFactory();
        em = emf.createEntityManager();

        em.getTransaction().begin();
        em.persist(MappedTestEntity.builder()
                .nombre("ENTIDAD1")
                .build());
        em.getTransaction().commit();

        MappedTestEntity stored = em.createQuery("SELECT m from MappedTestEntity m WHERE m.nombre=:pNombre", MappedTestEntity.class)
                .setParameter("pNombre", "ENTIDAD1")
                .getSingleResult();

        assertNotNull(stored.getId());
        assertEquals("ENTIDAD1", stored.getNombre());

        em.close();
        emf.close();
    }
}
