package com.mozilla.bandar.query.resources;

import static com.yammer.dropwizard.testing.JsonHelpers.fromJson;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriInfo;

import org.junit.Test;
import org.mockito.Mockito;
import org.pentaho.di.core.exception.KettleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mozilla.bandar.api.CToolsResponse;
import com.mozilla.bandar.query.tasks.CvbRefreshTask;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class TestCvbResource {
    Logger logger = LoggerFactory.getLogger(TestCvbResource.class);
    String path = "./src/test/resources/cpk/kettle";

    @Test
    public void testCvbResource() throws WebApplicationException, IOException, KettleException  {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        String fullPath = new File(path).getCanonicalPath();

        MultivaluedMap<String,String> params = new MultivaluedMapImpl();
        params.add("URI", fullPath + "/repo");

        UriInfo ui = Mockito.mock(UriInfo.class);
        Mockito.when(ui.getQueryParameters()).thenReturn(params);

        CvbResource resource = new CvbResource(path);
        StreamingOutput kettleFiles = resource.getKettleResult("listFiles", ui);
        kettleFiles.write(output);

        String nice = output.toString("UTF-8");
        System.out.println(nice);
        assertTrue(nice.length() > 0);

        CToolsResponse response = fromJson(nice, CToolsResponse.class);

        List<String> endpoints = resource.getQueryNames();
        for (String endpoint : endpoints) {
            // Make sure that the listFiles thing works and gives at least the items in getQueryNames
            assertTrue(responseContains(response, endpoint + ".ktr", "short_filename")
                    || responseContains(response, endpoint + ".kjb", "short_filename"));
        }
    }

    @Test
    public void testCvbHdfsAccess() throws WebApplicationException, IOException, KettleException  {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        MultivaluedMap<String,String> params = new MultivaluedMapImpl();
        params.add("URI", "hdfs://admin1.mango.metrics.scl3.mozilla.com:8020/user/mreid/");

        UriInfo ui = Mockito.mock(UriInfo.class);
        Mockito.when(ui.getQueryParameters()).thenReturn(params);

        CvbResource resource = new CvbResource(path);
        StreamingOutput kettleFiles = resource.getKettleResult("listFiles", ui);
        kettleFiles.write(output);

        String nice = output.toString("UTF-8");
        System.out.println(nice);
        assertTrue(nice.length() > 0);

        // FIXME: this doesn't work :(
//        CToolsResponse response = fromJson(nice, CToolsResponse.class);

//        List<String> endpoints = resource.getQueryNames();
//        for (String endpoint : endpoints) {
//            // Make sure that the listFiles thing works and gives at least the items in getQueryNames
//            assertTrue(responseContains(response, endpoint + ".ktr", "short_filename")
//                    || responseContains(response, endpoint + ".kjb", "short_filename"));
//        }
    }

    @Test
    public void testCvbRefreshTask() throws Exception {
        CvbResource resource = new CvbResource(path);
        CvbRefreshTask task = new CvbRefreshTask(resource);
        StringWriter out = new StringWriter();
        PrintWriter writer = new PrintWriter(out);
        task.execute(null, writer);
        String result = out.toString();

        assertTrue(result.contains(CvbRefreshTask.FINISH_MESSAGE));
    }

    private boolean responseContains(CToolsResponse response, String value, String fieldName) {
        // If we have metadata, figure out what column we're looking for.
        int fieldIndex = response.getFieldIndex(fieldName);

        // Otherwise, guess.
        // TODO: should we check all fields?
        if (fieldIndex < 0) fieldIndex = 0;

        List<List<Object>> resultset = response.getResultset();
        for (List<Object> result : resultset) {
            String target = String.valueOf(result.get(fieldIndex));
            if (value.equalsIgnoreCase(target)) {
                logger.debug("Found match: '{}' == '{}'", target, value);
                return true;
            }
        }

        return false;
    }
}