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
    <version>${project.version}</version>
</dependency>

<!-- Embedded postgres instance and driver -->
<dependency>
    <groupId>io.zonky.test</groupId>
    <artifactId>embedded-postgres</artifactId>
    <version>${zonky.embeded-postgres.version}</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>${postgresql.version}</version>
</dependency>
```

``` java
class PostgresJpaConfigurerTest {
    ...
    
    @RegisterExtension
    public static PreparedDbExtension extension = EmbeddedPostgresExtension.preparedDatabase(e -> { });
    @RegisterExtension
    public static JpaConfigurer<?> configurer = PostgresJpaConfigurer.begin(extension::getTestDatabase)    // pass a datasource provider to the initializer
            .properties()
                .schemaGenerationDatabaseAction(JpaPropertyConfigurer.DatabaseAction.DROP_AND_CREATE)
                .and()
            .persistenceUnitName("emptyEntityManagerSuccess")
            .withEntity(ManagedTestEntity.class);
}
```

finally just inject your EntityManager instance via parameters.

``` java

@BeforeEach
void setup(EntityManager em) {
    ...
}
 
@Test
void emptyEntityManagerSuccess(EntityManager em) {
    ...
}

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
