package org.hglteam.testing.jpatesting.core;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.SharedCacheMode;
import jakarta.persistence.ValidationMode;
import jakarta.persistence.spi.PersistenceUnitTransactionType;
import org.hglteam.testing.jpatesting.DatasourceProvider;
import org.hglteam.testing.jpatesting.JpaConfigurer;
import org.hglteam.testing.jpatesting.ProvisionMode;
import org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl;
import org.hibernate.jpa.boot.internal.PersistenceUnitInfoDescriptor;
import org.junit.jupiter.api.extension.*;

import javax.sql.DataSource;
import java.util.*;
import java.util.stream.Stream;

public abstract class JpaConfigurerBase<PC extends JpaPropertyConfigurerBase<PC>>
        implements JpaConfigurer<PC>,
                AfterAllCallback, AfterEachCallback, BeforeAllCallback, BeforeEachCallback,
                ParameterResolver {
    private static final String DEFAULT_PERSISTENCE_PROVIDER_CLASS_NAME = "org.hibernate.jpa.HibernatePersistenceProvider";

    private String persistenceUnitName;
    private DatasourceProvider provider;
    private ProvisionMode provisionMode = ProvisionMode.GLOBAL;
    private String persistenceProviderClassName = DEFAULT_PERSISTENCE_PROVIDER_CLASS_NAME;
    private final ExtensionContext.Namespace globalNamespace;
    private final List<String> classNames = new ArrayList<>();
    private final List<String> mappingFiles = new ArrayList<>();
    private PC propertyConfigurer;

    protected JpaConfigurerBase() {
        provider = this::getDatasource;
        globalNamespace = ExtensionContext.Namespace.create(this.getClass(), this.hashCode());
    }

    @Override
    public JpaConfigurer<PC> persistenceUnitName(String name) {
        this.persistenceUnitName = name;
        return this;
    }

    @Override
    public JpaConfigurer<PC> provisionMode(ProvisionMode mode) {
        this.provisionMode = mode;
        return this;
    }

    @Override
    public JpaConfigurer<PC> persistenceProviderClassName(String className) {
        this.persistenceProviderClassName = className;
        return this;
    }

    @Override
    public JpaConfigurer<PC> dataSourceProvider(DatasourceProvider provider) {
        this.provider = provider;
        return this;
    }

    @Override
    public JpaConfigurer<PC> withEntity(String entityClassName) {
        classNames.add(entityClassName);
        return this;
    }

    @Override
    public JpaConfigurer<PC> withEntities(String... entityClassNames) {
        this.classNames.addAll(Arrays.asList(entityClassNames));
        return this;
    }

    @Override
    public JpaConfigurer<PC> withEntity(Class<?> entityClass) {
        return this.withEntity(entityClass.getName());
    }

    @Override
    public JpaConfigurer<PC> withEntities(Class<?>... entityClasses) {
        return this.withEntities(Stream
                .of(entityClasses)
                .map(Class::getName)
                .toArray(String[]::new));
    }
    @Override
    public JpaConfigurer<PC> withMapping(String url) {
        this.mappingFiles.add(url);
        return this;
    }

    @Override
    public JpaConfigurer<PC> withMappings(String... urls) {
        this.mappingFiles.addAll(Arrays.asList(urls));
        return this;
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

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        var store = context.getStore(globalNamespace);
        var factory = buildFactory();

        store.put(EntityManagerFactory.class, factory);

        if(ProvisionMode.GLOBAL.equals(provisionMode)) {
            store.put(EntityManager.class, factory.createEntityManager());
        }
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        if(ProvisionMode.PER_TEST.equals(provisionMode)) {
            var namespace = ExtensionContext.Namespace.create(this.getClass(), this.hashCode(), context.getUniqueId());
            var globalStore = context.getStore(globalNamespace);
            var testStore = context.getStore(namespace);
            var factory = globalStore.get(EntityManagerFactory.class, EntityManagerFactory.class);

            testStore.put(EntityManager.class, factory.createEntityManager());
        }
    }

    @Override
    public void afterEach(ExtensionContext context) {
        if(ProvisionMode.PER_TEST.equals(provisionMode)) {
            var namespace = ExtensionContext.Namespace.create(this.getClass(), this.hashCode(), context.getUniqueId());
            var testStore = context.getStore(namespace);
            var manager = testStore.get(EntityManager.class, EntityManager.class);

            manager.close();
        }
    }

    @Override
    public void afterAll(ExtensionContext context) {
        var store = context.getStore(globalNamespace);
        var factory = store.get(EntityManagerFactory.class, EntityManagerFactory.class);

        factory.close();
    }

    @Override
    public Object resolveParameter(
            ParameterContext parameterContext,
            ExtensionContext extensionContext) throws ParameterResolutionException {
        var namespace = ExtensionContext.Namespace.create(this.getClass(), this.hashCode(), extensionContext.getUniqueId());
        var store = extensionContext.getStore(globalNamespace);
        var testStore = extensionContext.getStore(namespace);

        if(parameterContext.getParameter().getType().equals(EntityManager.class)) {
            if(extensionContext.getTestInstance().isEmpty() && ProvisionMode.PER_TEST.equals(provisionMode)) {
                throw new ParameterResolutionException("Cannot provide an instance of EntityManager with the current provision mode.");
            }
            return ProvisionMode.PER_TEST.equals(provisionMode)
                    ? testStore.get(EntityManager.class)
                    : store.get(EntityManager.class);
        }
        if(parameterContext.getParameter().getType().equals(EntityManagerFactory.class)) {
            return ProvisionMode.PER_TEST.equals(provisionMode)
                    ? testStore.get(EntityManagerFactory.class)
                    : store.get(EntityManagerFactory.class);
        }

        return null;
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        if(parameterContext.getParameter().getType().equals(EntityManager.class)) {
            if (extensionContext.getTestInstance().isEmpty() && ProvisionMode.PER_TEST.equals(provisionMode)) {
                throw new ParameterResolutionException("Cannot provide an instance of EntityManager with the current provision mode.");
            }
            return true;
        } else return parameterContext.getParameter().getType().equals(EntityManagerFactory.class);
    }

    protected PC getPropertyConfigurer() {
        return propertyConfigurer;
    }

    protected abstract DataSource getDatasource() throws Exception;

    protected abstract PC createProperties();
}
