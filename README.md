# jpatesting
It's a simple set of classes to setup JPA on tests.

We provide two implementations out of the box, H2 and postgres databases. 
But is pretty straight-forward to use another vendor datasource. 
If you'd like to contribute to the project feel free to contact us.

## Example using Postgres provider

You can connect to Postgresql database using embedded or
external datasource.

### Embedded datasource

In this example we use [Zonky's embedded postgres](https://github.com/zonkyio/embedded-postgres)
to setup an embedded postgres instance.

``` xml
<dependency>
    <groupId>org.hglteam.testing</groupId>
    <artifactId>jpatesting-postgres</artifactId>
    <version>1.0.2</version>
</dependency>

<!-- Embedded postgres instance and driver -->
<dependency>
    <groupId>io.zonky.test</groupId>
    <artifactId>embedded-postgres</artifactId>
    <version>1.2.7</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.2.12</version>
</dependency>

<!-- You must provide yout JPA implementation in test scope -->
<dependency>
    <groupId>org.hibernate</groupId>
    <artifactId>hibernate-core</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.hibernate</groupId>
    <artifactId>hibernate-entitymanager</artifactId>
    <scope>test</scope>
</dependency>
```

``` java
class PostgresJpaConfigurerTest {
    ...
    
    @RegisterExtension
    public static PreparedDbExtension extension = EmbeddedPostgresExtension.preparedDatabase(e -> { });

}
```

finaly just setup your entity manager factory via the configurer.

``` java 
@BeforeAll
static void emptyEntityManagerSuccess() {
    var configurer = PostgresJpaConfigurer.begin(extension::getTestDatabase)    // pass a datasource provider to the initializer
            .properties()
                .schemaGenerationDatabaseAction(JpaPropertyConfigurer.DatabaseAction.DROP_AND_CREATE)
                .and()
            .persistenceUnitName("emptyEntityManagerSuccess")
            .withEntity(ManagedTestEntity.class);

    emf = configurer.buildFactory();    // creates a EntityManagerFactory
    em = emf.createEntityManager();     // gets your entity manager
}
```

or by using the JPA configurer extension

``` xml
<dependency>
    <groupId>org.hglteam.testing</groupId>
    <artifactId>jpatesting-junit-core</artifactId>
    <version>1.0.2</version>
</dependency>
```

``` java
    ...
    
    @RegisterExtension
    public static PreparedDbExtension extension = EmbeddedPostgresExtension.preparedDatabase(e -> { });
    
    @RegisterExtension
    static JpaConfigurerExtension jpa = new JpaConfigurerExtension(PostgresJpaConfigurer.begin(extension::getTestDatabase)
            .properties()
            .schemaGenerationDatabaseAction(JpaPropertyConfigurer.DatabaseAction.DROP_AND_CREATE)
            .and()
            .persistenceUnitName("emptyEntityManagerSuccess")
            .withEntity(ManagedTestEntity.class));
            
    ...
```

### Configuring the persistence unit

You can configure the persistence unit by adding class by class

``` java
configurer = PostgresJpaConfigurer.begin(extension::getTestDatabase)
        .properties()
            .schemaGenerationDatabaseAction(JpaPropertyConfigurer.DatabaseAction.DROP_AND_CREATE)
            .and()
        .persistenceUnitName("emptyEntityManagerSuccess")
        .withEntity(TestEntity1.class)
        .withEntity(TestEntity2.class)
        ...
        ;
```

or by adding an xml mapping file

``` java
configurer = PostgresJpaConfigurer.begin(extension::getTestDatabase)
        .properties()
            .schemaGenerationDatabaseAction(JpaPropertyConfigurer.DatabaseAction.DROP_AND_CREATE)
            .and()
        .persistenceUnitName("emptyEntityManagerSuccess")
        .withMapping("/META-INF/mappings/entity-mapping-1.xml")
        .withMapping("/META-INF/mappings/entity-mapping-2.xml")
        ...
        ;
```

you can also use a persitence.xml file but atomic class approach is better 
for tests.

### external datasource

If you have to connect to an external datasource, you must provide 
the jdbc connection and credentials.

``` java
configurer = PostgresJpaConfigurer.begin()  // No datasource provider is required within the initializer.
        .properties()
            .schemaGenerationDatabaseAction(JpaPropertyConfigurer.DatabaseAction.DROP_AND_CREATE)
            .url("jdbc:postgresql://localhost:5432/my_external_db")
            .username("my_username")
            .password("my_password")
        .and()
        .persistenceUnitName("emptyEntityManagerSuccess")
        .withMapping("/META-INF/mappings/mapping.xml");
```
