package org.hgl.testing.jpatesting.providers.postgres;

import org.hgl.testing.jpatesting.DatasourceProvider;
import org.hgl.testing.jpatesting.JpaPropertyConfigurer;
import org.hgl.testing.jpatesting.core.JpaConfigurerBase;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;

public final class PostgresJpaConfigurer extends JpaConfigurerBase<
        PostgresJpaConfigurer, PostgresJpaPropertyConfigurer> {
    private PostgresJpaConfigurer() { }
    private PostgresJpaConfigurer(DatasourceProvider provider) {
        datasourceProvider(provider);
    }

    @Override
    protected DataSource getDatasource() {
        PGSimpleDataSource ds = new PGSimpleDataSource();

        ds.setUrl(getPropertyConfigurer().get(JpaPropertyConfigurer.JDBC_URL, Object::toString));
        ds.setUser(getPropertyConfigurer().get(JpaPropertyConfigurer.JDBC_USER, Object::toString));
        ds.setPassword(getPropertyConfigurer().get(JpaPropertyConfigurer.JDBC_PASSWORD, Object::toString));

        return ds;
    }

    @Override
    protected PostgresJpaPropertyConfigurer createProperties() {
        return new PostgresJpaPropertyConfigurer(self());
    }

    @Override
    protected PostgresJpaConfigurer self() {
        return this;
    }

    public static PostgresJpaConfigurer begin() {
        return new PostgresJpaConfigurer();
    }
    public static PostgresJpaConfigurer begin(DatasourceProvider datasourceProvider) {
        return new PostgresJpaConfigurer(datasourceProvider);
    }
}
