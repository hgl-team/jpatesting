package org.hglteam.testing.jpatesting.providers.h2;

import org.h2.jdbcx.JdbcDataSource;
import org.hglteam.testing.jpatesting.JpaPropertyConfigurer;
import org.hglteam.testing.jpatesting.core.JpaConfigurerBase;

import javax.sql.DataSource;

public final class H2JpaConfigurer extends JpaConfigurerBase<H2JpaPropertyConfigurer> {
    private H2JpaConfigurer() { }

    @Override
    protected DataSource getDatasource() {
        JdbcDataSource ds = new JdbcDataSource();

        ds.setUrl(getPropertyConfigurer().get(JpaPropertyConfigurer.JAKARTA_PERSISTENCE_JDBC_URL, Object::toString));
        ds.setUser(getPropertyConfigurer().get(JpaPropertyConfigurer.JAKARTA_PERSISTENCE_JDBC_USER, Object::toString));
        ds.setPassword(getPropertyConfigurer().get(JpaPropertyConfigurer.JAKARTA_PERSISTENCE_JDBC_PASSWORD, Object::toString));

        return ds;
    }

    @Override
    protected H2JpaPropertyConfigurer createProperties() {
        return new H2JpaPropertyConfigurer(this);
    }

    public static H2JpaConfigurer start() {
        return new H2JpaConfigurer();
    }
}
