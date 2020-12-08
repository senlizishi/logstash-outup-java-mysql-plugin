package org.logstashplugins.config;

import co.elastic.logstash.api.Configuration;
import com.zaxxer.hikari.HikariDataSource;
import org.logstashplugins.constants.PluginConfigParams;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Hikari Pool
 *
 * @author weixubin
 * @date 2020-12-05
 */
public class HikariPool {

    private static HikariDataSource dataSource = null;

    /**
     * init pool
     */
    public static void setUpPool(Configuration configuration) {
        dataSource = new HikariDataSource();
        dataSource.setDriverClassName(configuration.get(PluginConfigParams.DRIVER_CLASS));
        dataSource.setJdbcUrl(configuration.get(PluginConfigParams.CONNECTION_STRING));
        dataSource.setAutoCommit(true);
        dataSource.setConnectionTimeout(Long.parseLong(configuration.get(PluginConfigParams.CONNECTION_TIMEOUT)));
        dataSource.setMaximumPoolSize(Integer.parseInt(configuration.get(PluginConfigParams.MAX_POOL_SIZE)));
    }

    /**
     * getConnection
     */
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
