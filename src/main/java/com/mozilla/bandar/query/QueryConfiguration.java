package com.mozilla.bandar.query;

import java.util.Map;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.config.Configuration;

public class QueryConfiguration extends Configuration {
    @NotEmpty
    @JsonProperty
    private String basePath;

    @JsonProperty
    private String hdfsPath;

    @JsonProperty
    private Map<String,String> cdaPaths;

    public String getBasePath() { return basePath; }
    public String getHdfsPath() { return hdfsPath; }
    public Map<String,String> getCdaPaths() { return cdaPaths; }

    public void setBasePath(String basePath) { this.basePath = basePath; }
    public void setHdfsPath(String hdfsPath) { this.hdfsPath = hdfsPath; }
    public void setCdaPath(Map<String,String> cdaPaths) { this.cdaPaths = cdaPaths; }
}
