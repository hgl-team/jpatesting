package org.hgl.testing.jpatesting.providers.h2;

import org.h2.jdbcx.JdbcDataSource;
import org.hgl.testing.jpatesting.JpaPropertyConfigurer;
import org.hgl.testing.jpatesting.core.JpaConfigurerBase;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Optional;

public final class H2JpaConfigurer extends JpaConfigurerBase<
        H2JpaConfigurer, H2JpaPropertyConfigurer> {
    private H2JpaConfigurer() { }

    @Override
    protected DataSource getDatasource() {
        JdbcDataSource ds = new JdbcDataSource();

        ds.setUrl(getPropertyConfigurer().get(JpaPropertyConfigurer.JDBC_URL, Object::toString));
        ds.setUser(getPropertyConfigurer().get(JpaPropertyConfigurer.JDBC_USER, Object::toString));
        ds.setPassword(getPropertyConfigurer().get(JpaPropertyConfigurer.JDBC_PASSWORD, Object::toString));

        return ds;
    }

    @Override
    protected H2JpaPropertyConfigurer createProperties() {
        return new H2JpaPropertyConfigurer(self());
    }

    @Override
    protected H2JpaConfigurer self() {
        return this;
    }

    public static H2JpaConfigurer start() {
        return new H2JpaConfigurer();
    }
}
