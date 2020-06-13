package org.hgl.testing.jpatesting.core;

import org.hgl.testing.jpatesting.DatasourceProvider;
import org.hgl.testing.jpatesting.JpaConfigurer;
import org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl;
import org.hibernate.jpa.boot.internal.PersistenceUnitInfoDescriptor;

import javax.persistence.EntityManagerFactory;
import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;
import java.util.*;
import java.util.stream.Stream;

public abstract class JpaConfigurerBase<
        E extends JpaConfigurerBase<E, ?>,
        PC extends JpaPropertyConfigurerBase<?, E>>
        implements JpaConfigurer<E, PC> {
    private static final String DEFAULT_PERSISTENCE_PROVIDER_CLASS_NAME = "org.hibernate.jpa.HibernatePersistenceProvider";

    private String persistenceUnitName;
    private DatasourceProvider provider;
    private String persistenceProviderClassName = DEFAULT_PERSISTENCE_PROVIDER_CLASS_NAME;
    private List<String> classNames = new ArrayList<>();
    private List<String> mappingFiles = new ArrayList<>();
    private PC propertyConfigurer;

    protected JpaConfigurerBase() {
        provider = this::getDatasource;
    }

    @Override
    public E persistenceUnitName(String name) {
        this.persistenceUnitName = name;
        return self();
    }

    @Override
    public E persistenceProviderClassName(String className) {
        this.persistenceProviderClassName = className;
        return self();
    }

    @Override
    public E datasourceProvider(DatasourceProvider provider) {
        this.provider = provider;
        return self();
    }

    @Override
    public E withEntity(String entityClassName) {
        classNames.add(entityClassName);
        return self();
    }

    @Override
    public E withEntities(String... entityClassNames) {
        this.classNames.addAll(Arrays.asList(entityClassNames));
        return self();
    }

    @Override
    public E withEntity(Class<?> entityClass) {
        return this.withEntity(entityClass.getName());
    }

    @Override
    public E withEntities(Class<?>... entityClasses) {
        return this.withEntities(Stream
                .of(entityClasses)
                .map(Class::getName)
                .toArray(String[]::new));
    }
    @Override
    public E withMapping(String url) {
        this.mappingFiles.add(url);
        return self();
    }

    @Override
    public E withMappings(String... urls) {
        this.mappingFiles.addAll(Arrays.asList(urls));
        return self();
    }

    @Override
    public PC properties() {
        this.propertyConfigurer = createProperties();
        return this.propertyConfigurer;
    }

    @Override
    public EntityManagerFactory buildFactory() {
        BasicPersistenceUnitInfo info = BasicPersistenceUnitInfo.builder()
                .persistenceUnitName(persistenceUnitName)
                .managedClassNames(this.classNames)
                .mappingFileNames(this.mappingFiles)
                .properties(this.propertyConfigurer.toProperties())
                .nonJtaDataSource(this.provider.get())
                .transactionType(PersistenceUnitTransactionType.RESOURCE_LOCAL)
                .sharedCacheMode(SharedCacheMode.UNSPECIFIED)
                .validationMode(ValidationMode.AUTO)
                .persistenceProviderClassName(this.persistenceProviderClassName)
                .build();

        return new EntityManagerFactoryBuilderImpl(
                    new PersistenceUnitInfoDescriptor(info),
                    new HashMap())
                .build();
    }

    protected PC getPropertyConfigurer() {
        return propertyConfigurer;
    }

    protected abstract DataSource getDatasource();

    protected abstract PC createProperties();
    protected abstract E self();

}
