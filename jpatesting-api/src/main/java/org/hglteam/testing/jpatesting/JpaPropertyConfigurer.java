package org.hglteam.testing.jpatesting;

import java.util.Map;
import java.util.Properties;
import java.util.function.Function;

public interface JpaPropertyConfigurer<PC extends JpaPropertyConfigurer<PC,?>, C extends JpaConfigurer<?, PC>> {
    public static final String JDBC_DRIVER = "javax.persistence.jdbc.driver";
    public static final String JDBC_URL = "javax.persistence.jdbc.url";
    public static final String JDBC_USER = "javax.persistence.jdbc.user";
    public static final String JDBC_PASSWORD = "javax.persistence.jdbc.password";

    // javax.persistence.jdbc.driver
    PC driver(String driverClassName);
    PC driver(Class<?> driverClassName);
    // javax.persistence.jdbc.url
    PC url(String url);
    // javax.persistence.jdbc.user
    PC username(String url);
    // javax.persistence.jdbc.password
    PC password(String url);

    // javax.persistence.lock.timeout
    PC lockTimeout(Long value);
    // javax.persistence.query.timeout
    PC queryTimeout(Long value);
    // javax.persistence.validation.group.pre-persist
    // PC prePersist();
    // javax.persistence.validation.group.pre-update
    // PC preUpdate();
    // javax.persistence.validation.group.pre-remove
    // PC preRemove();

    // javax.persistence.schema-generation.create-script-source
    PC createScriptSource(String url);
    // javax.persistence.schema-generation.drop-script-source
    PC dropScriptSource(String url);
    // javax.persistence.sql-load-script-source
    PC sqlLoadScriptSource(String url);

    // javax.persistence.schema-generation.database.action
    PC schemaGenerationDatabaseAction(DatabaseAction action);
    // javax.persistence.schema-generation.scripts.action
    PC schemaGenerationScriptsAction(ScriptAction scriptAction);
    // javax.persistence.schema-generation.create-source
    PC schemaGenerationCreateSource(GenerationSource source);
    // javax.persistence.schema-generation.drop-source
    PC schemaGenerationDropSource(GenerationSource source);

    // javax.persistence.schema-generation.scripts.create-target
    PC schemaGenerationCreateScriptTarget(String url);
    // javax.persistence.schema-generation.scripts.drop-target
    PC schemaGenerationDropScriptTarget(String url);
    PC put(String propertyName, Object value);
    C and();
    Map<String, Object> toMap();
    Properties toProperties();

    <T> T get(String property, T defaultValue, Function<Object, T> converterFunction);
    <T> T get(String property, Function<Object, T> converterFunction);

    enum DatabaseAction {
        NONE("none"),
        CREATE("create"),
        DROP_AND_CREATE("drop-and-create"),
        DROP("drop");
        private final String value;

        DatabaseAction(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
    enum ScriptAction {
        NONE("none"),
        CREATE("create"),
        DROP_AND_CREATE("drop-and-create"),
        DROP("drop");
        private final String value;

        ScriptAction(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
    enum GenerationSource {
        METADATA("metadata"),
        SCRIPT("script"),
        METADATA_THEN_SCRIPT("metadata-then-script"),
        SCRIPT_THEN_METADATA("script-then-metadata");
        private final String value;

        GenerationSource(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
