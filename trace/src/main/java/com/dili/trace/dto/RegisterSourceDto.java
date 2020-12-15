package com.dili.trace.dto;

public class RegisterSourceDto {
    private String sourceId;
    private String sourceName;
    private Integer registerSource;
    private String query;

    public Integer getRegisterSource() {
        return registerSource;
    }

    public void setRegisterSource(Integer registerSource) {
        this.registerSource = registerSource;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }
}
