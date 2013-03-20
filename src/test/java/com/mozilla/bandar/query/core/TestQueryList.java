package com.mozilla.bandar.query.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.mozilla.bandar.Constants;

public class TestQueryList {
    final String providerBasePath = Constants.VALID_FILE_PROVIDER_PATH;
    LocalFileProvider provider;

    @Before
    public void setUp() throws Exception {
        provider = new LocalFileProvider(providerBasePath);
    }

    @Test
    public void test() {
        List<QueryProvider> providers = new ArrayList<QueryProvider>(1);
        providers.add(provider);
        QueryList list = new QueryList(providers);
        Map<String, List<String>> queries = list.getQueries();
        assertEquals(1, queries.size());

        boolean threw = false;
        try {
            queries.put("foo", new ArrayList<String>());
        } catch (UnsupportedOperationException e) {
            // Unmodifiable!
            threw = true;
        }

        assertTrue(threw);

        assertTrue(queries.containsKey(provider.getName()));

        assertEquals(provider.getQueryNames(), queries.get(provider.getName()));
    }
}
