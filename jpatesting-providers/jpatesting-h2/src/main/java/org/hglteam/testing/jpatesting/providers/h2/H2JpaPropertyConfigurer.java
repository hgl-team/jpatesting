package org.hglteam.testing.jpatesting.providers.h2;

import org.h2.Driver;
import org.hglteam.testing.jpatesting.core.JpaPropertyConfigurerBase;

public class H2JpaPropertyConfigurer extends JpaPropertyConfigurerBase<
        H2JpaPropertyConfigurer, H2JpaConfigurer> {

    public H2JpaPropertyConfigurer(H2JpaConfigurer configurer) {
        super(configurer);
        this.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        this.driver(Driver.class);
    }

    @Override
    protected H2JpaPropertyConfigurer self() {
        return this;
    }
}
