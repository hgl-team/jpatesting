package org.hglteam.testing.jpatesting.core;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;
import java.net.URL;
import java.util.List;
import java.util.Properties;

@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class BasicPersistenceUnitInfo implements PersistenceUnitInfo {
    private String persistenceUnitName;
    private String persistenceProviderClassName;
    private PersistenceUnitTransactionType transactionType;
    private DataSource jtaDataSource;
    private DataSource nonJtaDataSource;
    private List<String> mappingFileNames;
    private List<URL> jarFileUrls;
    private URL persistenceUnitRootUrl;
    private List<String> managedClassNames;
    private boolean excludeUnlistedClasses;
    private SharedCacheMode sharedCacheMode;
    private ValidationMode validationMode;
    private Properties properties;
    private String persistenceXMLSchemaVersion;
    private List<ClassTransformer> transformers;

    @Override
    public boolean excludeUnlistedClasses() {
        return this.isExcludeUnlistedClasses();
    }

    @Override
    public ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    @Override
    public void addTransformer(ClassTransformer classTransformer) {
        this.transformers.add(classTransformer);
    }

    @Override
    public ClassLoader getNewTempClassLoader() {
        return null;
    }
}
