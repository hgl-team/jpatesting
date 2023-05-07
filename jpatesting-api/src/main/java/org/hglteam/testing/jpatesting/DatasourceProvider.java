package org.hglteam.testing.jpatesting;

import javax.sql.DataSource;

@FunctionalInterface
public interface DatasourceProvider {
    DataSource get() throws Exception;
}
