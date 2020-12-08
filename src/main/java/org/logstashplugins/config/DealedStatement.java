package org.logstashplugins.config;

import java.util.List;

/**
 * @author weixubin
 * @date 2020-12-05
 */
public class DealedStatement {

    String preparedStatement;

    List<String> fields;

    public String getPreparedStatement() {
        return preparedStatement;
    }

    public void setPreparedStatement(String preparedStatement) {
        this.preparedStatement = preparedStatement;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public DealedStatement(String preparedStatement, List<String> fields) {
        this.preparedStatement = preparedStatement;
        this.fields = fields;
    }
}
