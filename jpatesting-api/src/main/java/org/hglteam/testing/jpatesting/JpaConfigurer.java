package org.hglteam.testing.jpatesting;

import jakarta.persistence.EntityManagerFactory;

public interface JpaConfigurer<PC extends JpaPropertyConfigurer<PC>> {
    JpaConfigurer<PC> persistenceUnitName(String name);
    JpaConfigurer<PC> dataSourceProvider(DatasourceProvider provider);
    JpaConfigurer<PC> persistenceProviderClassName(String className);
    PC properties();

    JpaConfigurer<PC> withEntity(String entityClassName);
    JpaConfigurer<PC> withEntities(String... entityClassNames);
    JpaConfigurer<PC> withEntity(Class<?> entityClass);
    JpaConfigurer<PC> withEntities(Class<?>... entityClasses);
    JpaConfigurer<PC> withMapping(String url);
    JpaConfigurer<PC> withMappings(String... urls);

    EntityManagerFactory buildFactory() throws Exception;
}
