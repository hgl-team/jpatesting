package org.hglteam.testing.jpatesting.providers.test.h2;

import org.hglteam.testing.jpatesting.JpaPropertyConfigurer;
import org.hglteam.testing.jpatesting.core.JpaConfigurerExtension;
import org.hglteam.testing.jpatesting.providers.h2.H2JpaConfigurer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class H2JpaConfigurerByStaticExtensionTest {

    @RegisterExtension
    static JpaConfigurerExtension jpa = new JpaConfigurerExtension(
            H2JpaConfigurer.start()
            .properties()
                .schemaGenerationDatabaseAction(JpaPropertyConfigurer.DatabaseAction.DROP_AND_CREATE)
                .url("jdbc:h2:mem:db1;DB_CLOSE_DELAY=-1")
                .username("sa")
                .password("sa")
                .and()
            .persistenceUnitName("emptyEntityManagerSuccess")
            .withMapping("/META-INF/mappings/mapping.xml"));

    @Test
    void mappedEntityManagerInsertSuccess() {
        jpa.getEntityManager().getTransaction().begin();
        jpa.getEntityManager().persist(MappedTestEntity.builder()
                .nombre("ENTIDAD1")
                .build());
        jpa.getEntityManager().getTransaction().commit();

        MappedTestEntity stored = jpa.getEntityManager().createQuery("SELECT m from MappedTestEntity m WHERE m.nombre=:pNombre", MappedTestEntity.class)
                .setParameter("pNombre", "ENTIDAD1")
                .getSingleResult();

        assertNotNull(stored.getId());
        assertEquals("ENTIDAD1", stored.getNombre());
    }

    @Test
    void mappedEntityManagerSecondInsertSuccess() {
        jpa.getEntityManager().getTransaction().begin();
        jpa.getEntityManager().persist(MappedTestEntity.builder()
                .nombre("ENTIDAD2")
                .build());
        jpa.getEntityManager().getTransaction().commit();

        MappedTestEntity stored = jpa.getEntityManager().createQuery("SELECT m from MappedTestEntity m WHERE m.nombre=:pNombre", MappedTestEntity.class)
                .setParameter("pNombre", "ENTIDAD2")
                .getSingleResult();

        assertNotNull(stored.getId());
        assertEquals("ENTIDAD2", stored.getNombre());
    }
}
