package com.mozilla.bandar.query.resources;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;

import org.junit.Test;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.exception.KettleException;

import pt.webdetails.cpk.CpkCoreService;
import pt.webdetails.cpk.ICpkEnvironment;

import com.mozilla.bandar.cvb.DummyCpkEnvironment;
import com.mozilla.bandar.query.core.CvbResult;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class TestCvbResource {
    String path = "./src/test/resources/cpk/kettle";

    @Test
    public void testCvb1() throws WebApplicationException, IOException, KettleException  {
        KettleEnvironment.init();
        String kettleFile = "/listFiles";
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        String fullPath = new File(path).getCanonicalPath();
        ICpkEnvironment environment = new DummyCpkEnvironment(fullPath);
        CpkCoreService service = new CpkCoreService(environment);
//        Map<String, ICommonParameterProvider> providers = new HashMap<String, ICommonParameterProvider>();
//        ICommonParameterProvider pathProvider = new CommonParameterProvider();
//        ICommonParameterProvider requestProvider = new CommonParameterProvider();
//        pathProvider.put("path", kettleFile);//kjb or ktr
//        pathProvider.put("outputstream", output);
//        pathProvider.put("httpresponse", null);
//        requestProvider.put("request","unnecessary value?");
//        requestProvider.put("paramURI", "/tmp/bandar/cpk/repo");
//        requestProvider.put("paramarg2", "value2");
//        requestProvider.put("paramarg3", "value3");
//        providers.put("path", pathProvider);
//        providers.put("request", requestProvider);
//        try {
//            service.createContent(providers);
//        } catch (Exception e) {
//            throw new WebApplicationException(e);
//        }

        MultivaluedMap<String,String> params = new MultivaluedMapImpl();
        params.add("URI", fullPath + "/repo");
        CvbResult result = new CvbResult(service, "/listFiles", "json", params);
        result.write(output);

        String nice = output.toString("UTF-8");
        System.out.println(nice);
        assertTrue(nice.length() > 0);
    }
}