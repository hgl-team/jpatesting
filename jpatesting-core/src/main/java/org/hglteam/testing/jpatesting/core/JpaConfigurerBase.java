package org.hglteam.testing.jpatesting.core;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.SharedCacheMode;
import jakarta.persistence.ValidationMode;
import jakarta.persistence.spi.PersistenceUnitTransactionType;
import org.hglteam.testing.jpatesting.DatasourceProvider;
import org.hglteam.testing.jpatesting.JpaConfigurer;
import org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl;
import org.hibernate.jpa.boot.internal.PersistenceUnitInfoDescriptor;

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
    private final List<String> classNames = new ArrayList<>();
    private final List<String> mappingFiles = new ArrayList<>();
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
    public E dataSourceProvider(DatasourceProvider provider) {
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
        this.propertyConfigurer = Optional.ofNullable(this.propertyConfigurer)
                .orElseGet(this::createProperties);
        return this.propertyConfigurer;
    }

    @Override
    public EntityManagerFactory buildFactory() throws Exception {
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
                    new HashMap<>())
                .build();
    }

    protected PC getPropertyConfigurer() {
        return propertyConfigurer;
    }

    protected abstract DataSource getDatasource() throws Exception;

    protected abstract PC createProperties();
    protected abstract E self();

}
