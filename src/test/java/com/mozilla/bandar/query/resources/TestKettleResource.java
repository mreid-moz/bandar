package com.mozilla.bandar.query.resources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mozilla.bandar.api.CToolsResponse;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class TestKettleResource {
    Logger logger = LoggerFactory.getLogger(TestKettleResource.class);
    String path = "./src/test/resources/kettle";
    String testEtl = "generate-rows";

    @Test
    public void testKettleResource() throws IOException {
        File kettleBase = new File(path);
        assertTrue(kettleBase.exists());
        assertTrue(kettleBase.isDirectory());
        String fullPath = kettleBase.getCanonicalPath();

        MultivaluedMap<String,String> params = new MultivaluedMapImpl();
        params.add("fooFilter", "Hello");

        UriInfo ui = Mockito.mock(UriInfo.class);
        Mockito.when(ui.getQueryParameters()).thenReturn(params);

        // Need this to ensure that Kettle gets initialized.
        KettleResource resource = new KettleResource(fullPath);
        CToolsResponse response = resource.getKettleResult(testEtl, ui);

        assertEquals(3, response.getResultset().size());
    }
}