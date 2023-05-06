package org.hglteam.testing.jpatesting;

import java.util.Map;
import java.util.Properties;
import java.util.function.Function;

public interface JpaPropertyConfigurer<PC extends JpaPropertyConfigurer<PC,?>, C extends JpaConfigurer<?, PC>> {
    String JAKARTA_PERSISTENCE_JDBC_DRIVER = "jakarta.persistence.jdbc.driver";
    String JAKARTA_PERSISTENCE_JDBC_URL = "jakarta.persistence.jdbc.url";
    String JAKARTA_PERSISTENCE_JDBC_USER = "jakarta.persistence.jdbc.user";
    String JAKARTA_PERSISTENCE_JDBC_PASSWORD = "jakarta.persistence.jdbc.password";
    String JAKARTA_PERSISTENCE_LOCK_TIMEOUT = "jakarta.persistence.lock.timeout";
    String JAKARTA_PERSISTENCE_QUERY_TIMEOUT = "jakarta.persistence.query.timeout";
    String JAKARTA_PERSISTENCE_SCHEMA_GENERATION_CREATE_SCRIPT_SOURCE = "jakarta.persistence.schema-generation.create-script-source";
    String JAKARTA_PERSISTENCE_SCHEMA_GENERATION_DROP_SCRIPT_SOURCE = "jakarta.persistence.schema-generation.drop-script-source";
    String JAKARTA_PERSISTENCE_SQL_LOAD_SCRIPT_SOURCE = "jakarta.persistence.sql-load-script-source";
    String JAKARTA_PERSISTENCE_SCHEMA_GENERATION_DATABASE_ACTION = "jakarta.persistence.schema-generation.database.action";
    String JAKARTA_PERSISTENCE_SCHEMA_GENERATION_SCRIPTS_ACTION = "jakarta.persistence.schema-generation.scripts.action";
    String JAKARTA_PERSISTENCE_SCHEMA_GENERATION_CREATE_SOURCE = "jakarta.persistence.schema-generation.create-source";
    String JAKARTA_PERSISTENCE_SCHEMA_GENERATION_DROP_SOURCE = "jakarta.persistence.schema-generation.drop-source";
    String JAKARTA_PERSISTENCE_SCHEMA_GENERATION_SCRIPTS_CREATE_TARGET = "jakarta.persistence.schema-generation.scripts.create-target";
    String JAKARTA_PERSISTENCE_SCHEMA_GENERATION_SCRIPTS_DROP_TARGET = "jakarta.persistence.schema-generation.scripts.drop-target";

    // jakarta.persistence.jdbc.driver
    PC driver(String driverClassName);
    PC driver(Class<?> driverClassName);
    // jakarta.persistence.jdbc.url
    PC url(String url);
    // jakarta.persistence.jdbc.user
    PC username(String url);
    // jakarta.persistence.jdbc.password
    PC password(String url);

    // jakarta.persistence.lock.timeout
    PC lockTimeout(Long value);
    // jakarta.persistence.query.timeout
    PC queryTimeout(Long value);
    // jakarta.persistence.validation.group.pre-persist
    // PC prePersist();
    // jakarta.persistence.validation.group.pre-update
    // PC preUpdate();
    // jakarta.persistence.validation.group.pre-remove
    // PC preRemove();

    // jakarta.persistence.schema-generation.create-script-source
    PC createScriptSource(String url);
    // jakarta.persistence.schema-generation.drop-script-source
    PC dropScriptSource(String url);
    // jakarta.persistence.sql-load-script-source
    PC sqlLoadScriptSource(String url);

    // jakarta.persistence.schema-generation.database.action
    PC schemaGenerationDatabaseAction(DatabaseAction action);
    // jakarta.persistence.schema-generation.scripts.action
    PC schemaGenerationScriptsAction(ScriptAction scriptAction);
    // jakarta.persistence.schema-generation.create-source
    PC schemaGenerationCreateSource(GenerationSource source);
    // jakarta.persistence.schema-generation.drop-source
    PC schemaGenerationDropSource(GenerationSource source);

    // jakarta.persistence.schema-generation.scripts.create-target
    PC schemaGenerationCreateScriptTarget(String url);
    // jakarta.persistence.schema-generation.scripts.drop-target
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
