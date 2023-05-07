package org.hglteam.testing.jpatesting.providers.test.h2;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hglteam.testing.jpatesting.JpaPropertyConfigurer;
import org.hglteam.testing.jpatesting.providers.h2.H2JpaConfigurer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class H2JpaConfigurerTest {
    private H2JpaConfigurer configurer;
    private EntityManagerFactory emf;
    private EntityManager em;

    @Test
    void emptyEntityManagerSuccess() {
        configurer = H2JpaConfigurer.start()
                .properties()
                    .schemaGenerationDatabaseAction(JpaPropertyConfigurer.DatabaseAction.DROP_AND_CREATE)
                    .url("jdbc:h2:mem:db1;DB_CLOSE_DELAY=-1")
                    .username("sa")
                    .password("sa")
                    .and()
                .persistenceUnitName("emptyEntityManagerSuccess")
                .withEntity(ManagedTestEntity.class);

        assertDoesNotThrow(() -> { emf = configurer.buildFactory(); });
        assertDoesNotThrow(() -> { em = emf.createEntityManager(); });

        em.close();
        emf.close();
    }

    @Test
    void emptyEntityManagerInsertSuccess() throws Exception {
        configurer = H2JpaConfigurer.start()
                .properties()
                .schemaGenerationDatabaseAction(JpaPropertyConfigurer.DatabaseAction.DROP_AND_CREATE)
                //.schemaGenerationCreateSource(JpaPropertyConfigurer.GenerationSource.METADATA)
                .url("jdbc:h2:mem:db1;DB_CLOSE_DELAY=-1")
                .username("sa")
                .password("sa")
                .remove(JpaPropertyConfigurer.JAKARTA_PERSISTENCE_JDBC_USER)
                .username("sa")
                .put("hibernate.hbm2ddl.auto","update")
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
    void mappedEntityManagerInsertSuccess() throws Exception {
        configurer = H2JpaConfigurer.start()
                    .properties()
                    .schemaGenerationDatabaseAction(JpaPropertyConfigurer.DatabaseAction.DROP_AND_CREATE)
                    .url("jdbc:h2:mem:db1;DB_CLOSE_DELAY=-1")
                    .username("sa")
                    .password("sa")
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
