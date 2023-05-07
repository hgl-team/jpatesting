package org.hglteam.testing.jpatesting.core;

import org.hglteam.testing.jpatesting.JpaPropertyConfigurer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;

public abstract class JpaPropertyConfigurerBase<
        PC extends JpaPropertyConfigurerBase<PC, ?>,
        C extends JpaConfigurerBase<?, PC>>
        implements JpaPropertyConfigurer<PC,C> {

    private final C configurer;
    private final Map<String, Object> propertyMap = new HashMap<>();

    protected JpaPropertyConfigurerBase(C configurer) {
        this.configurer = configurer;
    }

    @Override
    public PC driver(String driverClassName) {
        return this.put(JAKARTA_PERSISTENCE_JDBC_DRIVER, driverClassName);
    }
    @Override
    public PC driver(Class<?> driverClassName) {
        return driver(driverClassName.getName());
    }
    @Override
    public PC url(String url) {
        return this.put(JAKARTA_PERSISTENCE_JDBC_URL, url);
    }
    @Override
    public PC username(String username) {
        return this.put(JAKARTA_PERSISTENCE_JDBC_USER, username);
    }
    @Override
    public PC password(String password) {
        return this.put(JAKARTA_PERSISTENCE_JDBC_PASSWORD, password);
    }
    @Override
    public PC lockTimeout(Long value) {
        return this.put(JAKARTA_PERSISTENCE_LOCK_TIMEOUT, value);
    }
    @Override
    public PC queryTimeout(Long value) {
        return this.put(JAKARTA_PERSISTENCE_QUERY_TIMEOUT, value);
    }

    @Override
    public PC createScriptSource(String url) {
        return this.put(JAKARTA_PERSISTENCE_SCHEMA_GENERATION_CREATE_SCRIPT_SOURCE, url);
    }

    @Override
    public PC dropScriptSource(String url) {
        return this.put(JAKARTA_PERSISTENCE_SCHEMA_GENERATION_DROP_SCRIPT_SOURCE, url);
    }

    @Override
    public PC sqlLoadScriptSource(String url) {
        return this.put(JAKARTA_PERSISTENCE_SQL_LOAD_SCRIPT_SOURCE, url);
    }

    @Override
    public PC schemaGenerationDatabaseAction(DatabaseAction action) {
        return this.put(JAKARTA_PERSISTENCE_SCHEMA_GENERATION_DATABASE_ACTION, action.getValue());
    }

    @Override
    public PC schemaGenerationScriptsAction(ScriptAction scriptAction) {
        return this.put(JAKARTA_PERSISTENCE_SCHEMA_GENERATION_SCRIPTS_ACTION, scriptAction.getValue());
    }

    @Override
    public PC schemaGenerationCreateSource(GenerationSource source) {
        return this.put(JAKARTA_PERSISTENCE_SCHEMA_GENERATION_CREATE_SOURCE, source.getValue());
    }

    @Override
    public PC schemaGenerationDropSource(GenerationSource source) {
        return this.put(JAKARTA_PERSISTENCE_SCHEMA_GENERATION_DROP_SOURCE, source.getValue());
    }

    @Override
    public PC schemaGenerationCreateScriptTarget(String url) {
        return this.put(JAKARTA_PERSISTENCE_SCHEMA_GENERATION_SCRIPTS_CREATE_TARGET, url);
    }

    @Override
    public PC schemaGenerationDropScriptTarget(String url) {
        return this.put(JAKARTA_PERSISTENCE_SCHEMA_GENERATION_SCRIPTS_DROP_TARGET, url);
    }

    @Override
    public PC clearAll() {
        this.propertyMap.clear();
        return self();
    }

    @Override
    public C and() {
        return configurer;
    }

    @Override
    public Map<String, Object> toMap() {
        return Map.copyOf(propertyMap);
    }

    @Override
    public Properties toProperties() {
        var properties = new Properties();
        properties.putAll(this.propertyMap);
        return properties;
    }

    @Override
    public PC put(String property, Object value) {
        this.propertyMap.put(property, value);
        return self();
    }

    @Override
    public PC remove(String propertyName) {
        this.propertyMap.remove(propertyName);
        return self();
    }

    @Override
    public <T> T get(String property, T defaultValue, Function<Object, T> converterFunction) {
        return Optional.of(property)
                .map(propertyMap::get)
                .map(converterFunction)
                .orElse(defaultValue);
    }

    @Override
    public <T> T get(String property, Function<Object, T> converterFunction) {
        return Optional.of(property)
                .map(propertyMap::get)
                .map(converterFunction)
                .orElse(null);
    }

    protected abstract PC self();
}
