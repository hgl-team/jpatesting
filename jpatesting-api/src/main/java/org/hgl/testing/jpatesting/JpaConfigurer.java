package org.hgl.testing.jpatesting;

import javax.persistence.EntityManagerFactory;

public interface JpaConfigurer<
        E extends JpaConfigurer<E, ?>,
        PC extends JpaPropertyConfigurer<?, E>> {
    E persistenceUnitName(String name);
    E datasourceProvider(DatasourceProvider provider);
    E persistenceProviderClassName(String className);
    PC properties();

    E withEntity(String entityClassName);
    E withEntities(String... entityClassNames);
    E withEntity(Class<?> entityClass);
    E withEntities(Class<?>... entityClasses);
    E withMapping(String url);
    E withMappings(String... urls);

    EntityManagerFactory buildFactory();
}
