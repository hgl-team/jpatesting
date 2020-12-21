package org.hglteam.testing.jpatesting.core;

import lombok.Getter;
import org.junit.jupiter.api.extension.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class JpaConfigurerExtension
    implements BeforeAllCallback, AfterAllCallback, BeforeEachCallback, AfterEachCallback {
    private static final Logger log = LoggerFactory.getLogger(JpaConfigurerExtension.class);

    private final JpaConfigurerBase<?,?> configurer;
    private final boolean instancePerTest;
    @Getter
    private EntityManagerFactory entityManagerFactory;
    @Getter
    private EntityManager entityManager;

    public JpaConfigurerExtension(JpaConfigurerBase<?, ?> configurer) {
        this(configurer, false);
    }

    public JpaConfigurerExtension(JpaConfigurerBase<?, ?> configurer, boolean instancePerTest) {
        this.configurer = configurer;
        this.instancePerTest = instancePerTest;
    }

    private void closeAndDispose() {
        entityManager.close();
        entityManagerFactory.close();

        entityManager = null;
        entityManagerFactory = null;
        log.info("Entity manager has been disposed.");
    }

    private void setup() {
        entityManagerFactory = configurer.buildFactory();
        entityManager = entityManagerFactory.createEntityManager();

        log.info("Setup entity manager with {} configurer.", configurer);
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        if(!instancePerTest) {
            closeAndDispose();
        }
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {
        if(instancePerTest) {
            closeAndDispose();
        }
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        if(!instancePerTest) {
            setup();
        }
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        if(instancePerTest) {
            setup();
        }
    }
}
