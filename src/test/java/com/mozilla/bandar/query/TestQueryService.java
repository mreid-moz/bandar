package com.mozilla.bandar.query;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.mozilla.bandar.Constants;
import com.mozilla.bandar.health.BaseDirHealthCheck;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

public class TestQueryService {
    private final Environment environment = mock(Environment.class);
    private final QueryService service = new QueryService();
    private final QueryConfiguration config = new QueryConfiguration();
    private final Bootstrap<QueryConfiguration> bootstrap = mock(Bootstrap.class);

    @Before
    public void setUp() throws Exception {
        config.setBasePath(Constants.VALID_FILE_PROVIDER_PATH);
        config.setHdfsPath(Constants.INVALID_HDFS_PROVIDER_PATH);
    }

    @Test
    public void test() throws Exception {
        service.initialize(bootstrap);
        verify(bootstrap).setName("bandar");

        service.run(config, environment);

        verify(environment).addHealthCheck(any(BaseDirHealthCheck.class));
        verify(environment, atLeast(2)).addResource(any(Object.class));
    }
}
