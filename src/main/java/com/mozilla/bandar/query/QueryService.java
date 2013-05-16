package com.mozilla.bandar.query;

import com.mozilla.bandar.health.BaseDirHealthCheck;
import com.mozilla.bandar.query.core.HDFSProvider;
import com.mozilla.bandar.query.core.LocalFileProvider;
import com.mozilla.bandar.query.resources.CdaResource;
import com.mozilla.bandar.query.resources.CvbResource;
import com.mozilla.bandar.query.resources.LocalFileResource;
import com.mozilla.bandar.query.resources.LocalFileResultResource;
import com.mozilla.bandar.query.resources.QueryResource;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

public class QueryService extends Service<QueryConfiguration> {

    public static void main(String[] args) throws Exception {
        new QueryService().run(args);
    }

    @Override
    public void initialize(Bootstrap<QueryConfiguration> bootstrap) {
        bootstrap.setName("bandar");
    }

    @Override
    public void run(QueryConfiguration configuration, Environment environment) throws Exception {
        final String basePath = configuration.getBasePath();
        final String hdfsPath = configuration.getHdfsPath();
        final String cvbPath = configuration.getCvbPath();

        // CDA configuration is handled by spring (src/main/resources/cda.spring.xml)
        final CdaResource cdaResource = new CdaResource();

        final LocalFileProvider localFileProvider = new LocalFileProvider(basePath);
        final HDFSProvider hdfsProvider = new HDFSProvider(hdfsPath);
        final CvbResource cvbResource;
        if (cvbPath != null && cvbPath.length() > 0) {
            cvbResource = new CvbResource(cvbPath);
        } else {
            cvbResource = null;
        }
        QueryResource queries = new QueryResource(localFileProvider, hdfsProvider, cdaResource);
        if (cvbResource != null) {
            queries.addProvider(cvbResource);
            environment.addResource(cvbResource);
        }
        environment.addResource(queries);

        environment.addResource(new LocalFileResource(localFileProvider));
        environment.addResource(new LocalFileResultResource(localFileProvider));
        environment.addResource(cdaResource);
        environment.addHealthCheck(new BaseDirHealthCheck(basePath));
    }

}
