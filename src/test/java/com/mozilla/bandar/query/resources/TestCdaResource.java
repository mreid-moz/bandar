package com.mozilla.bandar.query.resources;

import static com.yammer.dropwizard.testing.JsonHelpers.fromJson;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriInfo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mozilla.bandar.api.CdaResponse;
import com.mozilla.bandar.query.core.LocalFileProvider;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class TestCdaResource {
    String cdaName = "sample";
    String cdaPath = "/home/mark/mozilla/github/bandar/src/test/resources/cda";
    Map<String,String> cdaPathMap = new HashMap<String,String>(1);
    String cdaFile = "sample-kettle";
    Logger logger = LoggerFactory.getLogger(TestCdaResource.class);
    LocalFileProvider provider;

    @Before
    public void setUp() {
        cdaPathMap.put(cdaName, cdaPath);
    }

    @Test
    public void testCdaResult() throws WebApplicationException, IOException {
        CdaResource cdaResource = new CdaResource();
        List<String> types = Arrays.asList("json", "xml", "html", "csv");
        MultivaluedMap<String,String> params = new MultivaluedMapImpl();
        params.add("fooFilter", "Hello");
        params.add("barFilter", "20");
        UriInfo ui = Mockito.mock(UriInfo.class);
        Mockito.when(ui.getQueryParameters()).thenReturn(params);
        for (String type : types)
        {
            StreamingOutput result;
            if ("json".equals(type))
                result = cdaResource.getJson(cdaFile, ui);
            else
                result = cdaResource.getByType(cdaFile, type, ui);

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            result.write(output);

            String nice = output.toString("UTF-8");
            System.err.println("Found " + nice.length() + " characters for type " + type + ":");
            System.err.println(nice);
            assertTrue(nice.length() > 0);
            if ("json".equals(type)) {
                CdaResponse response = fromJson(nice, CdaResponse.class);
                assertTrue(response.getResultset().size() == 2);
                assertEquals("Hola",        response.getResultset().get(0).get(0));
                assertEquals(30,            response.getResultset().get(0).get(1));
                assertEquals(Boolean.TRUE,  response.getResultset().get(0).get(2));
                assertEquals("Where am I?", response.getResultset().get(1).get(0));
                assertEquals(40,            response.getResultset().get(1).get(1));
                assertEquals(Boolean.TRUE,  response.getResultset().get(1).get(2));
            }
        }
    }

    @Test
    public void testCdaParams() throws WebApplicationException, IOException {
        CdaResource cdaResource = new CdaResource();

        StreamingOutput result;
        result = cdaResource.getParameters(cdaFile);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        result.write(output);

        String nice = output.toString("UTF-8");
        System.err.println("Found these parameters for " + cdaFile + ":");
        System.err.println(nice);
        assertTrue(nice.length() > 0);
        CdaResponse response = fromJson(nice, CdaResponse.class);
        assertEquals(2, response.getResultset().size());
    }

    @Test
    public void testCdaQueries() throws WebApplicationException, IOException {
        CdaResource cdaResource = new CdaResource();

        StreamingOutput result;
        result = cdaResource.getQueries(cdaFile);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        result.write(output);

        String nice = output.toString("UTF-8");
        System.err.println("Found these queries for " + cdaFile + ":");
        System.err.println(nice);
        assertTrue(nice.length() > 0);
    }

    @Test
    public void testCdaFiles() throws WebApplicationException, IOException {
        CdaResource cdaResource = new CdaResource();

        StreamingOutput result;
        result = cdaResource.getCdaFiles();

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        result.write(output);

        String nice = output.toString("UTF-8");
        System.err.println("Found these files:");
        System.err.println(nice);
        assertTrue(nice.length() > 0);
        CdaResponse response = fromJson(nice, CdaResponse.class);
        assertTrue(response.getResultset().size() > 0);
    }
}
