package org.logstashplugins;

import co.elastic.logstash.api.Configuration;
import co.elastic.logstash.api.Event;
import org.junit.Test;
import org.logstash.plugins.ConfigurationImpl;
import org.logstashplugins.constants.PluginConfigParams;

import java.util.*;

public class JavaOutputExampleTest {

    @Test
    public void testJavaOutputExample() {
        String driverClass = "com.mysql.jdbc.Driver";
        String connectionString = "jdbc:mysql://XXXXXXXX:3306/datahub_test?user=XXXXXX&password=XXXXXXX&rewriteBatchedStatements=true";
        String statement = "INSERT INTO datahub_test.v2_dim_agent_id(\n" +
                "\tmedia_type_id,\n" +
                "\tmedia_type_name,\n" +
                "\tagent_group_id,\n" +
                "\tagent_group_name,\n" +
                "\tagent_id,\n" +
                "\tagent_name,\n" +
                "\tplatform,\n" +
                "\tagent_leader,\n" +
                "\tagent_leader_start_time,\n" +
                "\tagent_leader_end_time,\n" +
                "\tinsert_time,\n" +
                "\tupdate_time,\n" +
                "\taccount_id\n" +
                ")value(\n" +
                "\t\tDEFAULT,\n" +
                "\t\tDEFAULT,\n" +
                "\t\t{agent_group_id},\n" +
                "\t\t{agent_group_name},\n" +
                "\t\t{agent_id},\n" +
                "\t\t{agent_name},\n" +
                "\t\t{platform},\n" +
                "\t\t{agent_leader},\n" +
                "\t\t{agent_leader_start_time},\n" +
                "\t\t{agent_leader_end_time},\n" +
                "\t\tDEFAULT,\n" +
                "\t\tDEFAULT,\n" +
                "\t\tDEFAULT\n" +
                ")";

        // 接收配置
        Map<String, Object> configValues = new HashMap<>();
        configValues.put(PluginConfigParams.DRIVER_CLASS.name(), driverClass);
        configValues.put(PluginConfigParams.CONNECTION_STRING.name(), connectionString);
        configValues.put(PluginConfigParams.STATEMENT.name(), statement);
        configValues.put(PluginConfigParams.FLUSH_SIZE.name(),"2");
        Configuration config = new ConfigurationImpl(configValues);
        JavaOutputExample output = new JavaOutputExample("test-id", config, null);

        // 造数据
        Collection<Event> events = new ArrayList<>();
        Event e = new org.logstash.Event();
        e.setField("agent_leader_start_time", new Date());
        e.setField("agent_leader_end_time", new Date());
        e.setField("agent_group_id", 2);
        e.setField("agent_group_name", "wxb");
        e.setField("agent_id", 114481);
        e.setField("agent_name", "wxb");
        e.setField("platform", "TW");
        e.setField("agent_leader", "wxb");
        events.add(e);

        Event e2 = new org.logstash.Event();
        e2.setField("agent_leader_start_time", new Date());
        e2.setField("agent_leader_end_time", new Date());
        e2.setField("agent_group_id", 3);
        e2.setField("agent_group_name", "wxb2");
        e2.setField("agent_id", 114482);
        e2.setField("agent_name", "wxb2");
        e2.setField("platform", "TW");
        e2.setField("agent_leader", "wxb2");
        events.add(e2);

        Event e3 = new org.logstash.Event();
        e3.setField("agent_leader_start_time", new Date());
        e3.setField("agent_leader_end_time", new Date());
        e3.setField("agent_group_id", 4);
        e3.setField("agent_group_name", "wxb3");
        e3.setField("agent_id", 114483);
        e3.setField("agent_name", "wxb3");
        e3.setField("platform", "TW");
        e3.setField("agent_leader", "wxb3");
        events.add(e3);

        output.output(events);
    }
}
