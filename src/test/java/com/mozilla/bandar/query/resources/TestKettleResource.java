package com.mozilla.bandar.query.resources;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mozilla.bandar.kettle.KettleResult;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class TestKettleResource {
    Logger logger = LoggerFactory.getLogger(TestKettleResource.class);
    String path = "./src/test/resources/kettle";
    String testEtl = "generate-rows";

    @Test
    public void testKettleResource() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        File kettleBase = new File(path);
        assertTrue(kettleBase.exists());
        assertTrue(kettleBase.isDirectory());
        String fullPath = kettleBase.getCanonicalPath();

        MultivaluedMap<String,String> params = new MultivaluedMapImpl();
        params.add("fooFilter", "Hello");

        UriInfo ui = Mockito.mock(UriInfo.class);
        Mockito.when(ui.getQueryParameters()).thenReturn(params);

        KettleResource resource = new KettleResource(fullPath);
        KettleResult result = new KettleResult(fullPath, testEtl, params);
//        CToolsResponse response = result.getResponse();
        result.write(output);

        String nice = output.toString("UTF-8");
        System.out.println(nice);
        assertTrue(nice.length() > 0);

//        CToolsResponse response = fromJson(nice, CToolsResponse.class);
//
//        List<String> endpoints = resource.getQueryNames();
//        for (String endpoint : endpoints) {
//            // Make sure that the listFiles thing works and gives at least the items in getQueryNames
//            assertTrue(responseContains(response, endpoint + ".ktr", "short_filename")
//                    || responseContains(response, endpoint + ".kjb", "short_filename"));
//        }
    }
}