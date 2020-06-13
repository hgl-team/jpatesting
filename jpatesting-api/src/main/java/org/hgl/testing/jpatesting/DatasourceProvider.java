package org.hgl.testing.jpatesting;

import javax.sql.DataSource;
import java.util.function.Supplier;

@FunctionalInterface
public interface DatasourceProvider extends Supplier<DataSource> { }
