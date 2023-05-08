package org.hglteam.testing.jpatesting.providers.postgres;

import org.hglteam.testing.jpatesting.core.JpaPropertyConfigurerBase;
import org.postgresql.Driver;

public class PostgresJpaPropertyConfigurer extends JpaPropertyConfigurerBase<PostgresJpaPropertyConfigurer> {

    public PostgresJpaPropertyConfigurer(PostgresJpaConfigurer configurer) {
        super(configurer);
        this.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        this.driver(Driver.class);
        this.username("postgres").password("postgres");
    }

    @Override
    protected PostgresJpaPropertyConfigurer self() {
        return this;
    }
}
