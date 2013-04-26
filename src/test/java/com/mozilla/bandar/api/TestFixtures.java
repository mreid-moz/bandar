package com.mozilla.bandar.api;

import static com.yammer.dropwizard.testing.JsonHelpers.asJson;
import static com.yammer.dropwizard.testing.JsonHelpers.fromJson;
import static com.yammer.dropwizard.testing.JsonHelpers.jsonFixture;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class TestFixtures {
    CdaResponse reference;

    @Before
    public void setUp() {
        List<CdaResponse.MetaData> metadata = new ArrayList<CdaResponse.MetaData>(3);
        metadata.add(new CdaResponse.MetaData("foo", "String", 0));
        metadata.add(new CdaResponse.MetaData("bar", "Integer", 1));
        metadata.add(new CdaResponse.MetaData("baz", "Boolean", 2));

        Map<String,String> queryInfo = new HashMap<String,String>();
        queryInfo.put("totalRows", "2");

        List<List<Object>> resultset = new ArrayList<List<Object>>(2);
        List<Object> row1 = new ArrayList<Object>(metadata.size());
        List<Object> row2 = new ArrayList<Object>(metadata.size());
        row1.add("Hola");        row1.add(30); row1.add(true);
        row2.add("Where am I?"); row2.add(40); row2.add(true);
        resultset.add(row1);
        resultset.add(row2);

        reference = new CdaResponse(metadata, queryInfo, resultset);
    }

    @Test
    public void cdaResponseSerialization() throws Exception {
        assertEquals(jsonFixture("fixtures/cdaresponse.json"), asJson(reference));
    }

    @Test
    public void cdaResponseDeserialization() throws Exception {
        CdaResponse fixture = fromJson(jsonFixture("fixtures/cdaresponse.json"), CdaResponse.class);
        assertTrue(reference.equals(fixture));
    }
}