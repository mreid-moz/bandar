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
    
	DntResource dnt;// = new DntResource(DailyDesktopDntData);
	Data d;
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
    	d = new Data();
    	loadDntData(configuration);
    	
    	dnt = new DntResource(d);
    	LOGGER.info("COMPLETE: DONE LOADING DNT DATA");

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
    
    private void loadDntData(QueryConfiguration configuration) {
    	d.readData(Constants.DESKTOP, Constants.DAILY, configuration.getDataPath());
    	d.readData(Constants.DESKTOP, Constants.WEEKLY, configuration.getDataPath());
    	d.readData(Constants.MOBILE, Constants.DAILY, configuration.getDataPath());
    	d.readData(Constants.MOBILE, Constants.WEEKLY, configuration.getDataPath());

    }
}
