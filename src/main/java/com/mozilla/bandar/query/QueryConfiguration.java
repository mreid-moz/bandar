package com.mozilla.bandar.query;

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
    private String cvbPath;

    public String getBasePath() { return basePath; }
    public String getHdfsPath() { return hdfsPath; }
    public String getCvbPath()  { return cvbPath; }

    public void setBasePath(String basePath) { this.basePath = basePath; }
    public void setHdfsPath(String hdfsPath) { this.hdfsPath = hdfsPath; }
    public void setCvbPath(String cvbPath)   { this.cvbPath = cvbPath; }
}
