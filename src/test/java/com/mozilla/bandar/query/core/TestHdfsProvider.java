package com.mozilla.bandar.query.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.mozilla.bandar.Constants;

public class TestHdfsProvider {
    final String providerBasePath = Constants.INVALID_HDFS_PROVIDER_PATH;
    HDFSProvider provider;

    @Before
    public void setUp() throws Exception {
        provider = new HDFSProvider(providerBasePath);
    }

    @Test
    public void test() {
        assertEquals("hdfs", provider.getName());
        List<String> queryNames = provider.getQueryNames();
        assertNotNull(queryNames);
        assertEquals(1, queryNames.size());

        // TODO: should we make sure it matches the "not supported" message?
    }
}
