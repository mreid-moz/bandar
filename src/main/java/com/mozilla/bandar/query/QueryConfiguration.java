package com.mozilla.bandar.query;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.config.Configuration;

public class QueryConfiguration extends Configuration {
    @NotEmpty
    @JsonProperty
    private String basePath;
    
    public String getBasePath() { return basePath; }
}
