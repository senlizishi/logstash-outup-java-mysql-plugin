# Logstash Java MySQL output Plugin  

[![Travis Build Status](https://travis-ci.org/logstash-plugins/logstash-output-java_output_example.svg)](https://travis-ci.org/logstash-plugins/logstash-output-java_output_example)

This is a logstash-outup-java-mysql plugin.It allows you to output to MySQL databases. It provides the retrial mechanism and batch processing ability.

This plugin is written based on [logstash-output-java_output_example](https://github.com/logstash-plugins/logstash-output-java_output_example) and refer to [logstash-output-jdbc](https://github.com/theangryangel/logstash-output-jdbc)'s design. 



* This plugin has not been tested in the production environment.This example only provides an implementation idea.

## Configuration options
| Option                       | Type             | Description                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   | Required? | Default |
| ------                       | ----             | -----------                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   | --------- | ------- |
| driver_class_name            | String           | The default is MySQL driver                                                                                                                                                                                                                                                                                                                                                                                                                                                  | No        |         |
| connection_string            | String           | JDBC connection URL                                                                                                                                                                                                                                                                                                                                                                                                                                                                           | Yes       |         |
| statement                    | String           | SQL statement | Yes       |         |
| max_pool_size                | String           | Maximum number of connections to open to the SQL server at any 1 time                                                                                                                                                                                                                                                                                                                                                                                                                         | No        | 5       |
| connection_timeout           | String           | Number of milliseconds before a SQL connection is closed                                                                                                                                                                                                                                                                                                                                                                                                                                           | No        | 10000   |
| flush_size                   | String           | Batch Process, the number of batch                                                                                                                                                                                                                                                                                                                                                                  | No        | 1000    |
| retry_max_count              | String           | Number of max retries                                                                                                                                                                                                                                                                                                                                                | No        | 3       |
| retry_interval_time          | String           | Number of milliseconds between each retry                                                                                                                                                                                                                                                                                                                                                                                                                                                  | No        | 2000     |

## Development、Running tests and Install plugin
You can read [this page](https://www.toutiao.com/i6902219129931973131/) or [official document](https://www.elastic.co/guide/en/logstash/7.x/java-output-plugin.html#_package_and_deploy_4) to learn how to development、Running tests and Install plugin.

##  Using example
After you install the plugin to logstash, You can write the following to logstash.conf
```sql
output {
  stdout{}
   java_output_example {
        flush_size => 1000
        connection_string  => "jdbc:mysql://am-bp174u81yj0ydbbdh90650.ads.aliyuncs.com:3306/tanwan_datahub_test?user=zeda_loghub_rw&password=NSGtunp4ODUfpeKA"
        statement => "INSERT INTO tanwan_datahub_test.v2_dim_agent_id(
                agent_id,agent_name,platform,insert_time,update_time)
                value({agent_id},{agent_name},{platform},DEFAULT,DEFAULT)"
   }
}
```

