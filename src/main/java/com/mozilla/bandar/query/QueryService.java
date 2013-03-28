package com.mozilla.bandar.query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mozilla.bandar.constant.Constants;
import com.mozilla.bandar.dnt.Data;
import com.mozilla.bandar.health.BaseDirHealthCheck;
import com.mozilla.bandar.query.core.HDFSProvider;
import com.mozilla.bandar.query.core.LocalFileProvider;
import com.mozilla.bandar.query.resources.DntResource;
import com.mozilla.bandar.query.resources.LocalFileResource;
import com.mozilla.bandar.query.resources.LocalFileResultResource;
import com.mozilla.bandar.query.resources.QueryResource;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

public class QueryService extends Service<QueryConfiguration> {
    private static final Logger LOGGER = LoggerFactory.getLogger(QueryService.class);
    
	Data dddd; //= new Data(Constants.DESKTOP, Constants.DAILY);
	DntResource dnt;// = new DntResource(DailyDesktopDntData);

    public static void main(String[] args) throws Exception {
        new QueryService().run(args);
    }

    @Override
    public void initialize(Bootstrap<QueryConfiguration> bootstrap) {
        bootstrap.setName("bandar");
    }

    @Override
    public void run(QueryConfiguration configuration, Environment environment) throws Exception {
    	LOGGER.info("INIT: LOADING DNT DATA");
    	dnt = new DntResource(new Data(Constants.DESKTOP, Constants.DAILY, configuration.getDataPath()));
    	LOGGER.info("COMPLETE: LOADING DNT DATA");

        final String basePath = configuration.getBasePath();
        final String hdfsPath = configuration.getHdfsPath();
        
        final LocalFileProvider localFileProvider = new LocalFileProvider(basePath);
        final HDFSProvider hdfsProvider = new HDFSProvider(hdfsPath);
        environment.addResource(new QueryResource(localFileProvider, hdfsProvider));
        environment.addResource(new LocalFileResource(localFileProvider));
        environment.addResource(new LocalFileResultResource(localFileProvider));
        environment.addHealthCheck(new BaseDirHealthCheck(basePath));
        
        environment.addResource(dnt);
    }

}
