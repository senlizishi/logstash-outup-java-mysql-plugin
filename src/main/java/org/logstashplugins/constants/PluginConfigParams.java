package org.logstashplugins.constants;

import co.elastic.logstash.api.PluginConfigSpec;

/**
 * Configurable parameters
 * @author weixubin
 * @date 2020-12-07
 */
public class PluginConfigParams {

    /**
     * Driver Class
     */
    public static final PluginConfigSpec<String> DRIVER_CLASS =
            PluginConfigSpec.stringSetting("driver_class_name", "com.mysql.jdbc.Driver");
    /**
     * Connection URL
     * eg. jdbc:mysql://XXXXX:3306/datahub_test?user=XXXX&password=XXXXX
     */
    public static final PluginConfigSpec<String> CONNECTION_STRING =
            PluginConfigSpec.stringSetting("connection_string", "", false, true);

    /**
     * SQL statement
     */
    public static final PluginConfigSpec<String> STATEMENT =
            PluginConfigSpec.stringSetting("statement", "", false, true);

    /**
     *  Number of milliseconds before a SQL connection is closed
     */
    public static final PluginConfigSpec<String> CONNECTION_TIMEOUT =
            PluginConfigSpec.stringSetting("connection_timeout", "10000");

    /**
     * Maximum number of connections to open to the SQL server at any 1 time
     */
    public static final PluginConfigSpec<String> MAX_POOL_SIZE =
            PluginConfigSpec.stringSetting("max_pool_size", "5");

    /**
     * Batch Process, the number of batch
     */
    public static final PluginConfigSpec<String> FLUSH_SIZE =
            PluginConfigSpec.stringSetting("flush_size", "1000");

    /**
     * Number of max retries
     */
    public static final PluginConfigSpec<String> RETRY_MAX_COUNT =
            PluginConfigSpec.stringSetting("retry_max_count", "3");

    /**
     * Number of milliseconds between each retry
     */
    public static final PluginConfigSpec<String> RETRY_INTERVAL_TIME =
            PluginConfigSpec.stringSetting("retry_interval_time", "2000");

}
