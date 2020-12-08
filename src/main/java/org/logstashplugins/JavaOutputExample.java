package org.logstashplugins;

import co.elastic.logstash.api.Configuration;
import co.elastic.logstash.api.Context;
import co.elastic.logstash.api.Event;
import co.elastic.logstash.api.LogstashPlugin;
import co.elastic.logstash.api.Output;
import co.elastic.logstash.api.PluginConfigSpec;
import org.logstash.Timestamp;
import org.logstashplugins.config.DealedStatement;
import org.logstashplugins.config.HikariPool;
import org.logstashplugins.constants.PluginConfigParams;
import org.logstashplugins.utils.Slicer;

import java.sql.*;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Output Plugin
 *
 * @author weixubin
 * @date 2020-12-08
 */
@LogstashPlugin(name = "java_output_example")
public class JavaOutputExample implements Output {

    private final String id;
    private final Integer flushSize;
    private final Integer retryMaxCount;
    private final Integer retryIntervalTime;
    private final CountDownLatch done = new CountDownLatch(1);
    private volatile boolean stopped = false;
    private PreparedStatement pstm = null;
    private String statement;
    private List<String> fields;
    public static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * all plugins must provide a constructor that accepts id, Configuration, and Context
     */
    public JavaOutputExample(final String id, final Configuration config, final Context context) {
        this.id = id;
        this.flushSize = Integer.parseInt(config.get(PluginConfigParams.FLUSH_SIZE));
        this.retryMaxCount = Integer.parseInt(config.get(PluginConfigParams.RETRY_MAX_COUNT));
        this.retryIntervalTime = Integer.parseInt(config.get(PluginConfigParams.RETRY_INTERVAL_TIME));
        this.statement = config.get(PluginConfigParams.STATEMENT);
        // init HikariPool
        HikariPool.setUpPool(config);
    }

    @Override
    public void output(final Collection<Event> events) {
        // divide and rule
        if (events.size() > 0) {
            List<List<Event>> lists = Slicer.fixedGrouping(new ArrayList<>(events), this.flushSize);
            for (List<Event> list : lists) {
                retryingSubmit(list);
            }
        }
    }

    @Override
    public void stop() {
        stopped = true;
        done.countDown();
    }

    @Override
    public void awaitStop() throws InterruptedException {
        done.await();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Collection<PluginConfigSpec<?>> configSchema() {
        // should return a list of all configuration options for this plugin
        return Arrays.asList(
                PluginConfigParams.DRIVER_CLASS,
                PluginConfigParams.CONNECTION_STRING,
                PluginConfigParams.STATEMENT,
                PluginConfigParams.CONNECTION_TIMEOUT,
                PluginConfigParams.MAX_POOL_SIZE,
                PluginConfigParams.FLUSH_SIZE);
    }

    /**
     * deal Statement
     * 1、Find all {value} in the statement, and extract the fields in {} and put them in the list
     * 2、replace {value} to ?
     *
     * @param statement
     * @return
     */
    private DealedStatement dealStatement(String statement) {
        List<String> fields = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\{[\\w]*\\}");
        Matcher matcher = pattern.matcher(statement);
        while (matcher.find()) {
            String e = matcher.group();
            String substring = e.substring(1, e.length() - 1);
            fields.add(substring);
            statement = statement.replace(e, "?");
        }
        DealedStatement ds = new DealedStatement(statement, fields);
        return ds;
    }

    private void paramReplace(PreparedStatement prepStatement, Object param, int i) throws SQLException {
        // PreparedStatement's index is starting with 1
        if (param instanceof Integer) {
            int value = ((Integer) param).intValue();
            prepStatement.setInt(i + 1, value);
        } else if (param instanceof String) {
            String s = (String) param;
            prepStatement.setString(i + 1, s);
        } else if (param instanceof Double) {
            double d = ((Double) param).doubleValue();
            prepStatement.setDouble(i + 1, d);
        } else if (param instanceof Float) {
            float f = ((Float) param).floatValue();
            prepStatement.setFloat(i + 1, f);
        } else if (param instanceof Long) {
            long l = ((Long) param).longValue();
            prepStatement.setLong(i + 1, l);
        } else if (param instanceof Boolean) {
            boolean b = ((Boolean) param).booleanValue();
            prepStatement.setBoolean(i + 1, b);
        } else if (param instanceof org.logstash.Timestamp) {
            // use joda
            prepStatement.setString(i + 1, ((Timestamp) param).getTime().toString(STANDARD_FORMAT));
        }
    }

    /**
     * Retry submit
     */
    private void retryingSubmit(List<Event> events) {
        Integer retryCount = 0;
        while (retryCount < retryMaxCount) {
            if (submit(events)) {
                break;
            } else {
                retryCount++;
                System.err.println("Execution exception，retry count: " + retryCount);
                try {
                    TimeUnit.MILLISECONDS.sleep(retryIntervalTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
    }

    /**
     * Submit
     */
    private boolean submit(List<Event> events) {
        Connection conn = null;
        try {
            conn = HikariPool.getConnection();
            // deal prepareStatement
            DealedStatement ds = dealStatement(statement);
            fields = ds.getFields();
            pstm = conn.prepareStatement(ds.getPreparedStatement());

            for (Event event : events) {
                // param replace
                for (int i = 0; i < fields.size(); i++) {
                    paramReplace(pstm, event.getField(fields.get(i)), i);
                }
                pstm.addBatch();
            }
            // Batch processing
            pstm.executeBatch();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            // close
            if (pstm != null) {
                try {
                    pstm.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
