package org.hgl.testing.jpatesting.providers.postgres;

import org.hgl.testing.jpatesting.core.JpaPropertyConfigurerBase;
import org.postgresql.Driver;

public class PostgresJpaPropertyConfigurer extends JpaPropertyConfigurerBase<
        PostgresJpaPropertyConfigurer, PostgresJpaConfigurer> {

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
