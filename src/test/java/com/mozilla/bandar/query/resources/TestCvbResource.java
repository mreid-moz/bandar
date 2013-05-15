package com.mozilla.bandar.query.resources;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import org.junit.Test;
import org.pentaho.di.core.exception.KettleException;

public class TestCvbResource {
    String path = "./src/test/resources/cpk/kettle";

    @Test
    public void testCvb1() throws WebApplicationException, IOException, KettleException  {

//        KettleEnvironment.init();
//        String kettleFile = "/listFiles";
        ByteArrayOutputStream output = new ByteArrayOutputStream();
//        String fullPath = new File(path).getCanonicalPath();
//        ICpkEnvironment environment = new DummyCpkEnvironment(fullPath);
//        CpkCoreService service = new CpkCoreService(environment);

//        MultivaluedMap<String,String> params = new MultivaluedMapImpl();
//        params.add("URI", fullPath + "/repo");
//        CvbResult result = new CvbResult(service, "/listFiles", "json", params);
//        result.write(output);

        CvbResource resource = new CvbResource(path);
        StreamingOutput kettleFiles = resource.getKettleFiles();
        kettleFiles.write(output);

        String nice = output.toString("UTF-8");
        System.out.println(nice);
        assertTrue(nice.length() > 0);
    }
}