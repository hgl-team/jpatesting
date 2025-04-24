package org.hglteam.testing.jpatesting.providers.test.h2;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hglteam.testing.jpatesting.JpaConfigurer;
import org.hglteam.testing.jpatesting.JpaPropertyConfigurer;
import org.hglteam.testing.jpatesting.providers.h2.H2JpaConfigurer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class H2JpaConfigurerExtensionTest {
    @RegisterExtension
    private static final JpaConfigurer<?> configurer = H2JpaConfigurer.start()
            .properties()
            .schemaGenerationDatabaseAction(JpaPropertyConfigurer.DatabaseAction.DROP_AND_CREATE)
            .url("jdbc:h2:mem:db1;DB_CLOSE_DELAY=-1")
            .username("sa")
            .password("sa")
            .and()
            .persistenceUnitName("emptyEntityManagerSuccess")
            .withEntity(ManagedTestEntity.class);

    @BeforeEach
    void setup(EntityManagerFactory factory, EntityManager manager) {
        assertNotNull(factory);
        assertNotNull(manager);
    }

    @Test
    void emptyEntityManagerSuccess(EntityManagerFactory factory, EntityManager manager) {
        assertNotNull(factory);
        assertNotNull(manager);
    }

    @BeforeAll
    public static void initialize(EntityManagerFactory factory, EntityManager entityManager) {
        assertNotNull(factory);
        assertNotNull(entityManager);
    }
}
