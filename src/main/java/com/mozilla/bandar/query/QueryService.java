package com.mozilla.bandar.query;

import com.mozilla.bandar.health.BaseDirHealthCheck;
import com.mozilla.bandar.query.resources.QueryResource;
import com.mozilla.bandar.query.resources.QueryResultResource;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

public class QueryService extends Service<QueryConfiguration> {
    
    public static void main(String[] args) throws Exception {
        new QueryService().run(args);
    }

    @Override
    public void initialize(Bootstrap<QueryConfiguration> bootstrap) {
        bootstrap.setName("bandar-query");
    }

    @Override
    public void run(QueryConfiguration configuration, Environment environment) throws Exception {
        final String basePath = configuration.getBasePath();
        environment.addResource(new QueryResource(basePath));
        environment.addResource(new QueryResultResource(basePath));
        environment.addHealthCheck(new BaseDirHealthCheck(basePath));
    }

}
