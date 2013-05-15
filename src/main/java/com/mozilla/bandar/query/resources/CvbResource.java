package com.mozilla.bandar.query.resources;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.StreamingOutput;

import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.exception.KettleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.webdetails.cpk.CpkCoreService;
import pt.webdetails.cpk.ICpkEnvironment;

import com.mozilla.bandar.cvb.DummyCpkEnvironment;
import com.mozilla.bandar.query.core.CvbResult;
import com.mozilla.bandar.query.core.QueryProvider;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.yammer.metrics.annotation.Timed;

@Path("/cvb")
public class CvbResource implements QueryProvider {
    Logger logger = LoggerFactory.getLogger(CvbResource.class);
    private final String environmentPath;
    private CpkCoreService service;

    public CvbResource(String path) {
        // Initialize Kettle Environment.  Without it, you get errors like this:
        // Unable to load class for step/plugin with id [GetFileNames].
        // Check if the plugin is available in the plugins subdirectory
        // of the Kettle distribution.
        try {
            KettleEnvironment.init();
        } catch (KettleException e) {
            throw new WebApplicationException(e);
        }

        try {
            environmentPath = new File(path).getCanonicalPath();
            ICpkEnvironment environment = new DummyCpkEnvironment(environmentPath);
            service = new CpkCoreService(environment);
        } catch (IOException e) {
            throw new WebApplicationException(e);
        }
    }

    @GET
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    public StreamingOutput getKettleFiles() {
        MultivaluedMap<String,String> params = new MultivaluedMapImpl();
        params.add("URI", environmentPath + "/repo");
        return new CvbResult(service, "/listFiles", "json", params);
    }

    // TODO: split provider out from resource?
    @Override
    public String getName() {
        return "cvb";
    }

    @Override
    public List<String> getQueryNames() {
        // FIXME
        return null;
    }

    @Override
    public StreamingOutput getQueryResult(String name) {
        return runKettleTask(name, null);
    }


    public StreamingOutput runKettleTask(String kettleFile, MultivaluedMap<String, String> params) {
        return null;
    }
}
